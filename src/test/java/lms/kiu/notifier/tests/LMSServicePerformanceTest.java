package lms.kiu.notifier.tests;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import lms.kiu.notifier.tests.telegram.bot.KiuNemoBot;
import lms.kiu.notifier.tests.telegram.bot.service.BotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
class LMSServicePerformanceTest {

  @Autowired
  private KiuNemoBot kiuNemoBot;

  @Autowired
  private BotService botService;

  @Value("${telegram.admin.id}")
  private long telegramID;

  private static final int CONCURRENT_USERS = 15;

  private final Map<String, List<Long>> timings = new ConcurrentHashMap<>();

  @Test
  void analyzeBottlenecks() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(CONCURRENT_USERS);
    AtomicInteger successCount = new AtomicInteger(0);

    long testStart = System.currentTimeMillis();

    System.out.println("\n=== Bottleneck Analysis ===");
    System.out.println("Users: " + CONCURRENT_USERS);
    System.out.print("Running: ");

    Flux.range(1, CONCURRENT_USERS)
        .parallel()
        .runOn(Schedulers.parallel())
        .flatMap(requestNum -> runSingleRequest(successCount)
            .timeout(Duration.ofSeconds(30))
            .doOnSuccess(v -> System.out.print("."))
            .onErrorResume(e -> {
              System.out.print("x");
              return Mono.empty();
            })
            .doFinally(s -> latch.countDown()))
        .sequential()
        .subscribe();

    latch.await(120, TimeUnit.SECONDS);
    long testDuration = System.currentTimeMillis() - testStart;

    System.out.println("\n\n=== Summary ===");
    printSummary(testDuration, successCount.get());
  }

  private Mono<Void> runSingleRequest(AtomicInteger successCount) {
    long start = System.currentTimeMillis();

    long rewindStart = System.currentTimeMillis();
    return Mono.fromFuture(botService.rewindLastCheck(kiuNemoBot, telegramID, "5", "days"))
        .doOnSuccess(v -> record("rewind", System.currentTimeMillis() - rewindStart))
        .then(Mono.defer(() -> {
          long newsStart = System.currentTimeMillis();
          return botService.sendNewsAsync(kiuNemoBot, telegramID)
              .doOnSuccess(v -> record("news", System.currentTimeMillis() - newsStart));
        }))
        .doOnSuccess(v -> {
          record("total", System.currentTimeMillis() - start);
          successCount.incrementAndGet();
        })
        .then();
  }

  private void record(String key, long value) {
    timings.computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>()))
        .add(value);
  }

  private void printSummary(long duration, int success) {
    System.out.println("Total duration: " + duration + " ms (" + duration / 1000.0 + "s)");
    System.out.println("Success: " + success + "/" + CONCURRENT_USERS);

    printOp("rewind", "Rewind");
    printOp("news", "News");
    printOp("total", "Total");
  }

  private void printOp(String key, String label) {
    List<Long> list = timings.get(key);
    if (list == null || list.isEmpty()) {
      System.out.println(label + ": no data");
      return;
    }
    long avg = (long) list.stream().mapToLong(Long::longValue).average().orElse(0);
    long min = list.stream().mapToLong(Long::longValue).min().orElse(0);
    long max = list.stream().mapToLong(Long::longValue).max().orElse(0);

    System.out.printf("%-10s  min=%4dms  max=%4dms  avg=%4dms%n",
        label, min, max, avg);
  }
}
