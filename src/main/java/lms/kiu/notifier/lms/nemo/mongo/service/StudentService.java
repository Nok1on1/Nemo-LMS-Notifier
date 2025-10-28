package lms.kiu.notifier.lms.nemo.mongo.service;

import lms.kiu.notifier.lms.nemo.mongo.model.Student;
import lms.kiu.notifier.lms.nemo.mongo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    Query query = new Query(Criteria.where("telegram_id").is(telegramId));

    Update update = new Update().set("studentToken", token);

    return template.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Student.class);
  }
}
