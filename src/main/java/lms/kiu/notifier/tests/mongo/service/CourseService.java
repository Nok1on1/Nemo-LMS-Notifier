package lms.kiu.notifier.tests.mongo.service;

import lms.kiu.notifier.tests.lms.api.model.request.AnnouncementRequest;
import lms.kiu.notifier.tests.lms.api.model.request.AssignmentRequest;
import lms.kiu.notifier.tests.mongo.model.Course;
import lms.kiu.notifier.tests.mongo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository repo;

  public Mono<Course> save(Course course) {
    return repo.findCourseByCourseIdAndGroupId(course.getCourseId(), course.getGroupId())
        .flatMap(Mono::just)
        .switchIfEmpty(repo.save(course));
  }

  public Mono<Course> findCourseById(String id) {
    return repo.findCourseById(id);
  }

  public Mono<Course> findCourseByCourseIdAndGroupId(int courseId, int groupId) {
    return repo.findCourseByCourseIdAndGroupId(courseId, groupId);
  }

  public Mono<AnnouncementRequest> getAnnouncementRequest(String id) {
    return findCourseById(id)
        .map(course -> new AnnouncementRequest(course.getCourseId(), course.getGroupId()))
        .doOnError(error -> log.error("Error getting announcement request for course id {}: {}",
            id, error.getMessage()))
        .onErrorResume(error -> {
          log.warn("Skipping course id {} due to error", id);
          return Mono.empty();
        });
  }

  public Mono<Tuple2<String, AssignmentRequest>> getCourseNameAndAssignmentRequest(String id) {
    return findCourseById(id)
        .map(course -> Tuples.of(course.getCourseName(),
            new AssignmentRequest(course.getCourseId(), course.getGroupId())))
        .onErrorResume(error -> {
          log.warn("Skipping course id {} due to error: {}", id, error.getMessage());
          return Mono.empty();
        });
  }
}

