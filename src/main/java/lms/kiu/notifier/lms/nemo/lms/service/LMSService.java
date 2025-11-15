package lms.kiu.notifier.lms.nemo.lms.service;

import lms.kiu.notifier.lms.nemo.data.Constants;
import lms.kiu.notifier.lms.nemo.lms.model.messages.AnnouncementMessage;
import lms.kiu.notifier.lms.nemo.lms.model.messages.AssignmentMessage;
import lms.kiu.notifier.lms.nemo.lms.model.response.AnnouncementResponse;
import lms.kiu.notifier.lms.nemo.lms.model.response.AssignmentResponse;
import lms.kiu.notifier.lms.nemo.mongo.service.CourseService;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

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


  public Flux<AnnouncementMessage> checkNewAnnouncements(Long telegramId) {
    return studentService.findByTelegramId(telegramId)
        .flatMapMany(student -> {
          if (student == null) {
            return Flux.empty();
          }

          String studentToken = textEncryptor.decrypt(student.getStudentToken());

          return Flux.fromIterable(student.getEnrolledCourseIds())
              .flatMap(courseService::getAnnouncementRequest)
              .flatMap(courseRequest ->
                  webClient.post()
                      .uri(Constants.ANNOUNCEMENT_LIST_URL_PATH)
                      .header("Authorization", "Bearer " + studentToken)
                      .bodyValue(courseRequest)
                      .retrieve()
                      .bodyToFlux(AnnouncementResponse.class)
                      .onErrorResume(error -> {
                        log.error("Error fetching announcements: {}", error.getMessage());
                        return Flux.empty();
                      })
              )
              .flatMap(res -> Flux.fromIterable(res.getData()))
              .filter(data -> data.getUpdatedAt().isAfter(student.getLastCheck()))
              .map(data -> AnnouncementMessage.builder()
                  .courseName(data.getCourseGroup().getName())
                  .url(data.getCourseGroup().getListId(), data.getCourseGroup().getId())
                  .time(data.getUpdatedAt())
                  .message(data.getTitle())
                  .build()
              ).subscribeOn(Schedulers.boundedElastic());
        });
  }

  public Flux<AssignmentMessage> checkNewAssignments(Long telegramId) {
    return studentService.findByTelegramId(telegramId)
        .flatMapMany(student -> {
          String studentToken = textEncryptor.decrypt(student.getStudentToken());

          return Flux.fromIterable(student.getEnrolledCourseIds())
              .flatMap(courseService::getCourseNameAndAssignmentRequest)
              .flatMap(courseAndRequest ->
                  webClient.post()
                      .uri(Constants.ASSIGNMENT_LIST_URL_PATH)
                      .header("Authorization", "Bearer " + studentToken)
                      .bodyValue(courseAndRequest.getT2())
                      .retrieve()
                      .bodyToFlux(AssignmentResponse.class)
                      .flatMap(res -> Flux.fromIterable(res.getData())
                          .filter(data -> data.getUpdatedAt().isAfter(student.getLastCheck()))
                          .map(dataItem -> AssignmentMessage.builder()
                              .courseName(courseAndRequest.getT1())
                              .endDate(dataItem.getEndDate())
                              .title(dataItem.getTitle())
                              .embeddedFileLinks(dataItem.getFileUrls())
                              .build())
                      )
              );
        });
  }

}
