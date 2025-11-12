package lms.kiu.notifier.lms.nemo.lms.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lms.kiu.notifier.lms.nemo.data.Constants;
import lms.kiu.notifier.lms.nemo.lms.model.messages.AnnouncementMessage;
import lms.kiu.notifier.lms.nemo.lms.model.messages.AssignmentMessage;
import lms.kiu.notifier.lms.nemo.lms.model.response.AnnouncementResponse;
import lms.kiu.notifier.lms.nemo.lms.model.response.AssignmentResponse;
import lms.kiu.notifier.lms.nemo.mongo.model.Student;
import lms.kiu.notifier.lms.nemo.mongo.service.CourseService;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LMSService {

  private final StudentService studentService;
  private final CourseService courseService;
  private final TextEncryptor textEncryptor;

  private final WebClient webClient = WebClient.builder()
      .baseUrl(Constants.MAIN_URL)
      .exchangeStrategies(ExchangeStrategies.builder()
          .codecs(configurer -> configurer
              .defaultCodecs()
              .maxInMemorySize(10 * 1024 * 1024)) // 10MB buffer size
          .build())
      .build();


  @Async
  public CompletableFuture<List<AnnouncementMessage>> checkNewAnnouncements(Long telegramId) {

    Student student = studentService.findByTelegramId(telegramId).block();

    if (student == null) {
      return CompletableFuture.completedFuture(null);
    }

    String studentToken = textEncryptor.decrypt(student.getStudentToken());

    return
        Flux.fromIterable(student.getEnrolledCourseIds())
            .flatMap(courseService::getAnnouncementRequest)
            .flatMap(courseRequest ->
                webClient.post()
                    .uri("student/lms/learningCourses/group/announcementList")
                    .header("Authorization", "Bearer " + studentToken)
                    .bodyValue(courseRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                            .doOnNext(body -> log.error("Server error for announcements: {}", body))
                            .then(Mono.error(new RuntimeException("Server returned 500 error"))))
                    .bodyToFlux(AnnouncementResponse.class)
                    .onErrorResume(error -> {
                      log.error("Error fetching announcements for course: {}", error.getMessage());
                      return Flux.empty();
                    }))
            .flatMap(res -> Flux.fromIterable(res.getData()))
            .filter(data -> data.getUpdatedAt().isAfter(student.getLastCheck()))
            .map(data -> AnnouncementMessage.builder()
                .courseName(data.getCourseGroup().getName())
                .url(data.getCourseGroup().getListId(), data.getCourseGroup().getId())
                .time(data.getUpdatedAt())
                .message(data.getTitle()).build()).collectList().toFuture();
  }

  @Async
  public CompletableFuture<List<AssignmentMessage>> checkNewAssignments(Long telegramId) {
    Student student = studentService.findByTelegramId(telegramId).block();

    if (student == null) {
      return CompletableFuture.completedFuture(null);
    }

    String studentToken = textEncryptor.decrypt(student.getStudentToken());

    return Flux.fromIterable(
            student.getEnrolledCourseIds())
        .flatMap(
            courseService::getCourseNameAndAssignmentRequest)
        .flatMap(CourseNameAndRequest ->
            webClient.post()
                .uri("student/lms/learningCourses/group/getAssignmentList")
                .header("Authorization", "Bearer " + studentToken)
                .bodyValue(CourseNameAndRequest.getT2())
                .retrieve()
                .bodyToFlux(AssignmentResponse.class)
                .flatMap(
                    res -> Flux.fromIterable(res.getData())
                        .filter(data -> data.getUpdatedAt().isAfter(student.getLastCheck()))
                        .map(dataItem -> AssignmentMessage.builder()
                            .courseName(CourseNameAndRequest.getT1())
                            .endDate(dataItem.getEndDate())
                            .title(dataItem.getTitle())
                            .embeddedFileLinks(dataItem.getFileUrls())
                            .build())
                )
        ).collectList().toFuture();
  }
}
