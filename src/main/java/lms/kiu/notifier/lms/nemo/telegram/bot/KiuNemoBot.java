package lms.kiu.notifier.lms.nemo.telegram.bot;

import static lms.kiu.notifier.lms.nemo.data.Constants.HELP_MESSAGE;
import static lms.kiu.notifier.lms.nemo.data.Constants.REGISTRATION_HELPER_MESSAGE;
import static lms.kiu.notifier.lms.nemo.data.Constants.WELCOME_MESSAGE;
import static org.telegram.telegrambots.abilitybots.api.objects.Locality.ALL;
import static org.telegram.telegrambots.abilitybots.api.objects.Privacy.PUBLIC;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;
import lms.kiu.notifier.lms.nemo.data.Constants;
import lms.kiu.notifier.lms.nemo.lms.service.LMSService;
import lms.kiu.notifier.lms.nemo.telegram.bot.service.BotService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * Main Telegram bot controller for KIU LMS notification system.
 * <p>
 * This bot provides commands for student registration, course initialization, and checking new
 * announcements from the LMS system. All commands are processed asynchronously to ensure responsive
 * bot behavior.
 * <p>
 * Available commands:
 * <ul>
 *   <li>/start - Display welcome message and registration instructions</li>
 *   <li>"register token" - Initiate student registration flow</li>
 *   <li>/init_student - Initialize student courses via Playwright automation</li>
 *   <li>/check_news - Check for new announcements and homework across enrolled courses</li>
 * </ul>
 *
 * @see BotService
 * @see LMSService
 */
@Slf4j
@Component
public class KiuNemoBot extends AbilityBot {

  @Value("${telegram.admin.id}")
  private static long adminID;

  private final TelegramClient telegramClient;
  private final BotService botService;
  private final LMSService lmsService;

  public KiuNemoBot(
      TelegramClient telegramClient,
      String botUsername,
      BotService botService, LMSService lmsService) {

    super(telegramClient, botUsername);
    this.telegramClient = telegramClient;
    this.botService = botService;
    this.lmsService = lmsService;
  }

  public Ability start() {
    return Ability.builder()
        .name("start")
        .info("Start the bot and view instructions")
        .privacy(PUBLIC)
        .locality(ALL)
        .action(ctx -> {
          silent.send(WELCOME_MESSAGE, ctx.chatId());
        })
        .build();
  }

  public Ability defaultMessage() {
    return Ability.builder()
        .name(DEFAULT)
        .flag(Flag.MESSAGE)
        .privacy(PUBLIC)
        .locality(ALL)
        .input(0)
        .action(ctx -> silent.send(HELP_MESSAGE, ctx.chatId()))
        .build();
  }

  /**
   * Creates the student registration conversation flow.
   * <p>
   * Flow steps:
   * <ol>
   *   <li>User sends "register token"</li>
   *   <li>Bot sends instruction image showing how to copy token</li>
   *   <li>User uploads token.txt file</li>
   *   <li>Bot validates and processes the registration</li>
   * </ol>
   *
   * @return ReplyFlow configuration for registration process
   */
  public ReplyFlow registrationFlow() {
    return ReplyFlow.builder(db)
        .action((bot, upd) -> {
          InputStream imageStream = getClass().getResourceAsStream("/copyToken.png");

          SendPhoto sendPhoto = SendPhoto.builder()
              .chatId(upd.getMessage().getChatId().toString())
              .photo(new InputFile(imageStream, "copyToken.png"))
              .caption(REGISTRATION_HELPER_MESSAGE)
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

  /**
   * Handles file uploads during registration.
   * <p>
   * Validates the uploaded file (must be .txt, max 10KB) and processes the student token for
   * registration. Processing is done asynchronously to avoid blocking the bot.
   *
   * @return Reply configuration for file handling
   */
  private Reply fileReceived() {
    return Reply.of((bot, upd) -> {
      Document document = upd.getMessage().getDocument();
      Long chatId = upd.getMessage().getChatId();

      String fileId = document.getFileId();
      String fileName = document.getFileName();
      Long fileSize = document.getFileSize();

      log.info("Received file - ID: {}, Name: {}, Size: {} bytes", fileId, fileName, fileSize);

      if (!fileName.endsWith(".txt")) {
        silent.send("File is not .txt extension. Start Again!", chatId);
        return;
      }
      if (fileSize > 10 * 1024) {
        silent.send("File is too big for it to be a Token, stop breaking things!", chatId);
        return;
      }

      silent.send("Processing your registration...", chatId);

      try {
        botService.processRegistrationAsync(chatId, fileId, bot.getTelegramClient())
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

  /**
   * Handles the /init_student command.
   * <p>
   * Launches Playwright automation to navigate the LMS system and extract all enrolled courses for
   * the student. This process takes approximately 1 minute and runs asynchronously.
   * <p>
   * Prerequisites: Student must be registered via the registration flow first.
   *
   * @return Ability configuration for student initialization
   */
  public Ability initializeStudent() {
    return Ability.builder()
        .name("init_student")
        .info(
            "navigate the LMS system and extract all enrolled courses for later use in checking news")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long chatId = ctx.chatId();

          silent.send(String.format(
                  "Starting up Playwright automation Script on: %s \n this will take up to 1 minute...",
                  Constants.MAIN_URL),
              chatId);

          // Process asynchronously
          botService.initializeStudentAsync(chatId, telegramClient, silent)
              .thenAccept(courseNames -> {
                if (!courseNames.isEmpty()) {
                  silent.send(String.format("Courses Added: %s", courseNames.toString()), chatId);
                }
              })
              .exceptionally(ex -> {
                log.error("Error initializing student for chatId: {}", chatId, ex);
                silent.send("Failed to initialize student. Please try again later.", chatId);
                return null;
              });
        })
        .build();
  }

  /**
   * Handles the /check_news command.
   * <p>
   * Checks all enrolled courses for new announcements and assignments since the student's last
   * check. Sends individual messages for each new announcement found.
   * <p>
   * Prerequisites: Student must be registered and initialized.
   *
   * @return Ability configuration for checking news
   */
  public Ability check_news() {
    return Ability.builder().name("check_news")
        .info("checks all of the courses' Announcements and Assignments")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long chatId = ctx.chatId();

          silent.send("Sending check requests...", chatId);

          lmsService.checkNewAnnouncements(chatId).thenAccept(announcementList -> {
            if (announcementList.isEmpty()) {
              silent.send("no new Announcements", chatId);
            }
            for (var announcementMessage : announcementList) {
              silent.send(announcementMessage.toString(), chatId);
            }
          }).exceptionally(ex -> {
            {
              log.error("Error checking news for chatId: {}", chatId, ex);
              silent.send("Failed to check news. Please try again later.", chatId);
              return null;
            }
          });

          lmsService.checkNewAssignments(chatId).thenAccept(assignmentList -> {
            if (assignmentList.isEmpty()) {
              silent.send("no new Assignments", chatId);
            }
            for (var assignment : assignmentList) {
              silent.send(assignment.toString(), chatId);
            }
          }).exceptionally(ex -> {
            {
              log.error("Error checking assignment for chatId: {}", chatId, ex);
              silent.send("Failed to check news. Please try again later.", chatId);
              return null;
            }
          });

        }).build();
  }

  @NotNull
  private Predicate<Update> hasMessageWith(String msg) {
    return upd -> upd.getMessage().getText().equalsIgnoreCase(msg);
  }

  @Override
  public long creatorId() {
    return adminID;
  }
}
