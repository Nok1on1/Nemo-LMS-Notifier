package lms.kiu.notifier.mongo.repository;

import lms.kiu.notifier.mongo.model.Course;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CourseRepository extends ReactiveMongoRepository<Course, String> {

  Mono<Course> findCourseByCourseIdAndGroupId(int courseId, int groupId);

  Mono<Course> findCourseById(String id);
}
