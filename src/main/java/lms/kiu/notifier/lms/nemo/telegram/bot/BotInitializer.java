package lms.kiu.notifier.lms.nemo.telegram.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer implements CommandLineRunner {
  private final KiuNemoBot kiuNemoBot;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Override
  public void run(String... args) {
    log.info(">>> Initializing Telegram bot...");

    try {
      TelegramBotsLongPollingApplication botsApplication =
          new TelegramBotsLongPollingApplication();

      // Register callback for onRegister
      kiuNemoBot.onRegister();

      log.info(">>> Bot registered with Telegram...");

      // Register bot with long polling
      botsApplication.registerBot(botToken, kiuNemoBot);

      log.info(">>> Bot fully initialized and waiting for messages!");

      // Keep the application running
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          log.info(">>> Shutting down bot...");
          botsApplication.close();
        } catch (Exception e) {
          log.error("Error during bot shutdown", e);
        }
      }));

    } catch (TelegramApiException e) {
      log.error("Failed to initialize bot", e);
      throw new RuntimeException("Bot initialization failed", e);
    }
  }
}
