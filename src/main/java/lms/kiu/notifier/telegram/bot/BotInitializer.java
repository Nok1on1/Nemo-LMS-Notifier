package lms.kiu.notifier.telegram.bot;

import jakarta.annotation.PreDestroy;
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
  private TelegramBotsLongPollingApplication botsApplication;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Override
  public void run(String... args) {
    log.info(">>> Initializing Telegram bot...");

    try {
      botsApplication = new TelegramBotsLongPollingApplication();

      // Register callback for onRegister
      kiuNemoBot.onRegister();

      log.info(">>> Bot registered with Telegram...");

      // Register bot with long polling
      botsApplication.registerBot(botToken, kiuNemoBot);

      log.info(">>> Bot fully initialized and waiting for messages!");

    } catch (TelegramApiException e) {
      log.error("Failed to initialize bot", e);
      throw new RuntimeException("Bot initialization failed", e);
    }
  }

  @PreDestroy
  public void shutdown() {
    log.info(">>> Shutting down bot...");

    try {
      if (botsApplication != null) {
        // First, unregister the bot to stop receiving updates
        log.info(">>> Unregistering bot from Telegram...");
        botsApplication.unregisterBot(botToken);

        // Give time for any in-flight updates to complete
        Thread.sleep(1000);

        // Close the long polling application
        log.info(">>> Closing bot application...");
        botsApplication.close();

        // Give time before database closure
        Thread.sleep(500);

        log.info(">>> Bot shutdown complete");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Shutdown interrupted", e);
    } catch (Exception e) {
      log.error("Error during bot shutdown", e);
    } finally {
      // Ensure database is closed even if errors occur
      try {
        if (kiuNemoBot.getDb() != null) {
          log.info(">>> Closing bot database...");
          kiuNemoBot.getDb().close();
          log.info(">>> Database closed successfully");
        }
      } catch (Exception e) {
        log.error("Error closing database", e);
      }
    }
  }
}