package lms.kiu.notifier.mongo.repository;

import lms.kiu.notifier.mongo.model.BugReport;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugReportRepository extends ReactiveMongoRepository<BugReport, String> {}
