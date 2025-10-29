package lms.kiu.notifier.lms.nemo.telegram.bot.service;

import java.time.LocalDateTime;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@AllArgsConstructor
public class NewsScheduler {

  private final AbilityBot kiuNemoBot;
  private final StudentService studentService;
  private final BotService botService;

  @Async
  @Schedules({
      @Scheduled(cron = "0 0 11 * * *", zone = "Asia/Tbilisi"),
      @Scheduled(cron = "0 0 16 * * *", zone = "Asia/Tbilisi"),
      @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Tbilisi")
  })
  public void scheduleNewsNotifications() {
    LocalDateTime minus3Hours = LocalDateTime.now().minusHours(3);

    studentService.findStudentsByLastCheckBefore(minus3Hours)
        .flatMap(student ->
            Flux.just(botService.sendNews(kiuNemoBot, student.getTelegramId()))
        )
        .then()
        .block();
  }
}
