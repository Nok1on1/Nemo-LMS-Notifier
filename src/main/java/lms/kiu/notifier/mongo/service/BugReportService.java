package lms.kiu.notifier.mongo.service;

import lms.kiu.notifier.mongo.model.BugReport;
import lms.kiu.notifier.mongo.repository.BugReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BugReportService {
  private final BugReportRepository repo;

  public Mono<BugReport> save(BugReport bugReport) {
    return repo.save(bugReport);
  }
}
