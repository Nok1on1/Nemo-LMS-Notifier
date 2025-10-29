package lms.kiu.notifier.lms.nemo.telegram.bot.config;


import lms.kiu.notifier.lms.nemo.lms.service.LMSService;
import lms.kiu.notifier.lms.nemo.mongo.service.CourseService;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lms.kiu.notifier.lms.nemo.telegram.bot.service.BotService;
import lms.kiu.notifier.lms.nemo.telegram.bot.service.NewsScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
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
  public BotService botService(StudentService studentService, CourseService courseService,
      TextEncryptor textEncryptor, LMSService lmsService) {
    return new BotService(studentService, courseService, textEncryptor, lmsService);
  }

  @Bean
  public NewsScheduler newsScheduler(AbilityBot kiuNemoBot,
      StudentService studentService,
      BotService botService) {
    return new NewsScheduler(kiuNemoBot, studentService, botService);
  }
}