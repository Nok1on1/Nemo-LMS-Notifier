package lms.kiu.notifier.telegram.bot.service;

import java.time.LocalDateTime;
import lms.kiu.notifier.mongo.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class NewsScheduler {

  private final AbilityBot kiuNemoBot;
  private final StudentService studentService;
  private final BotService botService;

  @Scheduled(cron = "0 0 11 * * *", zone = "Asia/Tbilisi")
  @Scheduled(cron = "0 0 16 * * *", zone = "Asia/Tbilisi")
  @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Tbilisi")
  public void scheduleNewsNotifications() {
    LocalDateTime minus3Hours = LocalDateTime.now().minusHours(3);

    studentService.findStudentsByLastCheckBefore(minus3Hours)
        .flatMap(student ->
            botService.sendNewsAsync(kiuNemoBot, student.getTelegramId())
                .onErrorResume(ex -> {
                  log.error("Failed sending news to student{}", student.getTelegramId());
                  return Mono.empty();
                })
        )
        .subscribe();
  }

}
