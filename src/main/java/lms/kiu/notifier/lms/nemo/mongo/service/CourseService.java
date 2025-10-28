package lms.kiu.notifier.lms.nemo.mongo.service;

import lms.kiu.notifier.lms.nemo.lms.model.request.AnnouncementRequest;
import lms.kiu.notifier.lms.nemo.lms.model.request.AssignmentRequest;
import lms.kiu.notifier.lms.nemo.mongo.model.Course;
import lms.kiu.notifier.lms.nemo.mongo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository repo;

  public Mono<Course> save(Course course) {
    return repo.save(course);
  }

  public Mono<Course> findCourseById(String id) {
    return repo.findCourseById(id);
  }

  public Mono<Course> findCourseByCourseIdAndGroupId(int courseId, int groupId) {
    return repo.findCourseByCourseIdAndGroupId(courseId, groupId);
  }

  public Mono<AnnouncementRequest> getAnnouncementRequest(String id) {
    return findCourseById(id)
        .doOnNext(course -> log.debug("Creating announcement request for course: {}, group: {}",
            course.getCourseId(), course.getGroupId()))
        .map(course -> new AnnouncementRequest(course.getCourseId(), course.getGroupId()))
        .doOnError(error -> log.error("Error getting announcement request for course id {}: {}",
            id, error.getMessage()))
        .onErrorResume(error -> {
          log.warn("Skipping course id {} due to error", id);
          return Mono.empty();
        });
  }

  public Mono<AssignmentRequest> getAssignmentRequest(String id) {
    return findCourseById(id)
        .doOnNext(course -> log.debug("Creating assignment request for course: {}, group: {}",
            course.getCourseId(), course.getGroupId()))
        .map(course -> new AssignmentRequest(course.getCourseId(), course.getGroupId()))
        .doOnError(error -> log.error("Error getting assignment request for course id {}: {}",
            id, error.getMessage()))
        .onErrorResume(error -> {
          log.warn("Skipping course id {} due to error", id);
          return Mono.empty();
        });
  }
}
