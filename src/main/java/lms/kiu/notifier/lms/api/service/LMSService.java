package lms.kiu.notifier.lms.api.service;

import lms.kiu.notifier.data.Constants;
import lms.kiu.notifier.lms.api.model.messages.AnnouncementMessage;
import lms.kiu.notifier.lms.api.model.messages.AssignmentMessage;
import lms.kiu.notifier.lms.api.model.request.GetCoursesInfoRequest;
import lms.kiu.notifier.lms.api.model.response.AnnouncementResponse;
import lms.kiu.notifier.lms.api.model.response.AssignmentResponse;
import lms.kiu.notifier.lms.api.model.response.DashboardResponse;
import lms.kiu.notifier.lms.api.model.response.CoursesInfoResponse;
import lms.kiu.notifier.lms.api.model.response.RegistrationGroupIdResponse;
import lms.kiu.notifier.mongo.service.CourseService;
import lms.kiu.notifier.mongo.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .exchangeStrategies(ExchangeStrategies.builder()
          .codecs(configurer -> configurer
              .defaultCodecs()
              .maxInMemorySize(10 * 1024 * 1024)) // 10MB buffer size
          .build())
      .build();

  public Mono<RegistrationGroupIdResponse> getRegistrationGroupIdResponse(String studentToken) {
    return webClient.post()
        .uri(Constants.REGISTRATION_GROUP_ID_URL_PATH)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + studentToken)
        .retrieve().bodyToMono(RegistrationGroupIdResponse.class);
  }

  public Flux<AnnouncementMessage> checkNewAnnouncements(long telegramId) {
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

  public Mono<CoursesInfoResponse> getStudentCoursesInfo(String studentToken,
      GetCoursesInfoRequest getCoursesInfoReq) {
    return webClient.post()
        .uri(Constants.COURSES_INFO_URL_PATH)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + studentToken)
        .bodyValue(getCoursesInfoReq)
        .retrieve()
        .bodyToMono(CoursesInfoResponse.class)
        .subscribeOn(Schedulers.boundedElastic());
  }

  public Mono<DashboardResponse> getStudentDashboard(String studentToken) {
    return webClient.get()
        .uri(Constants.STUDENT_DASHBOARD_URL_PATH)
        .header(HttpHeaders.AUTHORIZATION, "bearer" + studentToken)
        .retrieve().bodyToMono(DashboardResponse.class)
        .subscribeOn(Schedulers.boundedElastic());
  }
}
