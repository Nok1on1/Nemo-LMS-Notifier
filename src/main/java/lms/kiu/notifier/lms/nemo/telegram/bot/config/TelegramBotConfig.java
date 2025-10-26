package lms.kiu.notifier.lms.nemo.telegram.bot.config;


import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lms.kiu.notifier.lms.nemo.telegram.bot.KiuNemoBot;
import lms.kiu.notifier.lms.nemo.telegram.bot.service.AsyncBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramBotConfig {

  @Value("${telegram.bot.username}")
  String botUsername;
  @Value("${telegram.bot.token}")
  private String botToken;

  @Bean
  public String botUsername() {
    return botUsername;
  }

  @Bean
  public TelegramClient telegramClient() {
    return new OkHttpTelegramClient(botToken);
  }

  @Bean
  public AsyncBotService asyncBotService(StudentService studentService) {
    return new AsyncBotService(studentService);
  }

  @Bean
  public KiuNemoBot kiuNemoBot(
      TelegramClient telegramClient,
      AsyncBotService asyncBotService) {
    return new KiuNemoBot(
        telegramClient,
        botUsername,
        asyncBotService
    );
  }
}