package lms.kiu.notifier.lms.nemo.mongo.repository;

import lms.kiu.notifier.lms.nemo.mongo.model.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StudentRepository extends ReactiveMongoRepository<Student, String> {

  Mono<Student> findByTelegramId(Long telegramId);

}
