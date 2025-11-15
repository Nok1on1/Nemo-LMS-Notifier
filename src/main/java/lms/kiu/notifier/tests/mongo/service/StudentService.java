package lms.kiu.notifier.tests.mongo.service;

import com.mongodb.client.result.UpdateResult;
import java.time.LocalDateTime;
import lms.kiu.notifier.tests.mongo.model.Course;
import lms.kiu.notifier.tests.mongo.model.Student;
import lms.kiu.notifier.tests.mongo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository repo;
  private final ReactiveMongoTemplate template;

  public Mono<Student> save(Student student) {
    return repo.save(student);
  }

  public Mono<Student> findByTelegramId(Long telegramId) {
    return repo.findByTelegramId(telegramId);
  }

  public Mono<Void> deleteById(String id) {
    return repo.deleteById(id);
  }

  public Mono<Boolean> isRegistered(Long telegramId) {
    return repo.findByTelegramId(telegramId).hasElement();
  }

  public Mono<Student> updateStudentToken(Long telegramId, String token) {
    Query query = new Query(Criteria.where("telegramId").is(telegramId));

    Update update = new Update().set("studentToken", token);

    return template.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),
        Student.class);
  }

  public Mono<UpdateResult> updateLastCheck(Long telegramId) {
    Query query = Query.query(Criteria.where("telegramId").is(telegramId));
    Update update = new Update().set("lastCheck", LocalDateTime.now());
    return template.updateFirst(query, update, "students");
  }

  public Mono<UpdateResult> modifyLastCheck(Long telegramId, LocalDateTime lastCheck) {
    Query query = Query.query(Criteria.where("telegramId").is(telegramId));
    Update update = new Update().set("lastCheck", lastCheck);
    return template.updateFirst(query, update, "students");
  }

  public Mono<UpdateResult> addCourse(Long telegramId, Course courseData) {
    Query query = Query.query(Criteria.where("telegramId").is(telegramId));
    Update update = new Update().addToSet("enrolledCourseIds", courseData.getId());
    return template.updateFirst(query, update, "students");
  }


  public Flux<Student> findStudentsByLastCheckBefore(LocalDateTime lastCheckBefore) {
    return repo.findStudentsByLastCheckBefore(lastCheckBefore);
  }

  public Mono<Void> deleteStudentByTelegramId(Long telegramId) {
    return repo.deleteStudentByTelegramId(telegramId);
  }
}
