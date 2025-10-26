package lms.kiu.notifier.lms.nemo.telegram.bot;

import static org.telegram.telegrambots.abilitybots.api.objects.Locality.ALL;
import static org.telegram.telegrambots.abilitybots.api.objects.Privacy.PUBLIC;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;
import lms.kiu.notifier.lms.nemo.playwright.data.Constants;
import lms.kiu.notifier.lms.nemo.telegram.bot.service.AsyncBotService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.abilitybots.api.objects.Flag;
import org.telegram.telegrambots.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.abilitybots.api.objects.ReplyFlow;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class KiuNemoBot extends AbilityBot {

  private final TelegramClient telegramClient;
  private final AsyncBotService asyncBotService;

  public KiuNemoBot(
      TelegramClient telegramClient,
      String botUsername,
      AsyncBotService asyncBotService) {

    super(telegramClient, botUsername);
    this.telegramClient = telegramClient;
    this.asyncBotService = asyncBotService;
  }

  public Ability start() {
    return Ability.builder()
        .name("start")
        .privacy(PUBLIC)
        .locality(ALL)
        .action(ctx -> silent.send("start by writing \"register token\"", ctx.chatId()))
        .build();
  }

  public ReplyFlow registrationFlow() {
    return ReplyFlow.builder(db)
        .action((bot, upd) -> {
          InputStream imageStream = getClass().getResourceAsStream("/copyToken.png");

          SendPhoto sendPhoto = SendPhoto.builder()
              .chatId(upd.getMessage().getChatId().toString())
              .photo(new InputFile(imageStream, "copyToken.png"))
              .caption("Send the Student-Token value as token.txt file")
              .build();

          try {
            bot.getTelegramClient().execute(sendPhoto);
          } catch (TelegramApiException e) {
            silent.send("failed to fetch photo", upd.getMessage().getChatId());
          }
        })
        .onlyIf(hasMessageWith("register token"))
        .next(fileReceived())
        .build();
  }

  private Reply fileReceived() {
    return Reply.of((bot, upd) -> {
      Document document = upd.getMessage().getDocument();
      Long chatId = upd.getMessage().getChatId();

      String fileId = document.getFileId();
      String fileName = document.getFileName();
      Long fileSize = document.getFileSize();

      log.info("Received file - ID: {}, Name: {}, Size: {} bytes", fileId, fileName, fileSize);

      // Validation
      if (!fileName.endsWith(".txt")) {
        silent.send("File is not .txt extension. Start Again!", chatId);
        return;
      }
      if (fileSize > 10 * 1024) {
        silent.send("File is too big for it to be a Token, stop breaking things!", chatId);
        return;
      }

      // Send immediate acknowledgment
      silent.send("Processing your registration...", chatId);

      try {
        asyncBotService.processRegistrationAsync(chatId, fileId, bot.getTelegramClient())
            .thenAccept(result -> {
              silent.send(result.getMessage(), chatId);
            })
            .exceptionally(ex -> {
              log.error("Error in async registration", ex);
              silent.send("Registration failed. Please try again.", chatId);
              return null;
            });

      } catch (TelegramApiException | IOException e) {
        log.error("Error processing file", e);
        silent.send("Can't Read the File", chatId);
      }

    }, Flag.DOCUMENT);
  }

  public Ability checkNews() {
    return Ability.builder()
        .name("check_news")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long chatId = ctx.chatId();

          // Send immediate acknowledgment
          silent.send("Checking News on " + Constants.MAIN_URL, chatId);

          // Process asynchronously
          asyncBotService.checkNewsAsync(chatId, telegramClient, silent)
              .thenAccept(result -> {
                if (!result.isEmpty()) {
                  silent.send("New Messages On Following Links", chatId);
                }
                for (var entry : result.entrySet()) {
                  silent.send(
                      String.format("%d Message(s) on URL: %s",
                          entry.getValue(), entry.getKey()),
                      chatId
                  );
                }
              })
              .exceptionally(ex -> {
                log.error("Error checking news for chatId: {}", chatId, ex);
                silent.send("Failed to check news. Please try again later.", chatId);
                return null;
              });
        })
        .build();
  }

  @NotNull
  private Predicate<Update> hasMessageWith(String msg) {
    return upd -> upd.getMessage().getText().equalsIgnoreCase(msg);
  }

  @Override
  public long creatorId() {
    return 5381177672L;
  }
}
