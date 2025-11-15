package lms.kiu.notifier.lms.nemo.telegram.bot;

import static lms.kiu.notifier.lms.nemo.data.Constants.ABOUT_MESSAGE;
import static lms.kiu.notifier.lms.nemo.data.Constants.HELP_MESSAGE;
import static lms.kiu.notifier.lms.nemo.data.Constants.WELCOME_MESSAGE;
import static lms.kiu.notifier.lms.nemo.utils.MessageUtils.hasMessageWith;
import static org.telegram.telegrambots.abilitybots.api.objects.Locality.ALL;
import static org.telegram.telegrambots.abilitybots.api.objects.Privacy.PUBLIC;

import lms.kiu.notifier.lms.nemo.lms.service.LMSService;
import lms.kiu.notifier.lms.nemo.telegram.bot.service.BotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.abilitybots.api.objects.Flag;
import org.telegram.telegrambots.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.abilitybots.api.objects.ReplyFlow;
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
  private long adminID;

  private final BotService botService;

  public KiuNemoBot(TelegramClient telegramClient, String botUsername, BotService botService) {

    super(telegramClient, botUsername);
    this.telegramClient = telegramClient;
    this.botService = botService;
  }

  public Ability start() {
    return Ability.builder().name("start").info("Start the bot and view instructions")
        .privacy(PUBLIC).locality(ALL).action(ctx -> silent.send(WELCOME_MESSAGE, ctx.chatId()))
        .build();
  }

  public Ability about() {
    return Ability.builder().name("about").privacy(PUBLIC).locality(ALL)
        .action(ctx -> silent.send(ABOUT_MESSAGE, ctx.chatId())).build();
  }

  public Ability defaultMessage() {
    return Ability.builder().name(DEFAULT).flag(Flag.MESSAGE).privacy(PUBLIC).locality(ALL).input(0)
        .action(ctx -> silent.send(HELP_MESSAGE, ctx.chatId())).build();
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
    return ReplyFlow.builder(db).action(
            (bot, upd) -> botService.sendRegistrationInstructions(bot, upd.getMessage().getChatId()))
        .onlyIf(hasMessageWith("register token")).next(fileReceived()).build();
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
      botService.processRegistrationAsync(bot, upd)
          .doOnSuccess(s -> silent.send("✅ Registration successful!", upd.getMessage().getChatId()))
          .doOnError(e -> silent.send("Error, Something went wrong!", upd.getMessage().getChatId()))
          .then().subscribe();
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
          botService.initializeStudentAsync(ctx)
              .subscribe(courseNames -> {
                silent.send("Courses Added:\n", ctx.chatId());
                StringBuilder stringBuilder = new StringBuilder();
                courseNames.forEach(courseName ->
                    stringBuilder.append(courseName).append("\n"));
                ctx.bot().getSilent().send(stringBuilder.toString(), ctx.chatId());
              }, error -> {
                log.error("Error initializing student", error);
                ctx.bot().getSilent()
                    .send("An error occurred during initialization.", ctx.chatId());
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
  public Ability checkNews() {
    return Ability.builder().name("check_news")
        .info("checks all of the courses' Announcements and Assignments").locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> botService.sendNewsAsync(this, ctx.chatId()).then().subscribe()).build();
  }

  public Ability rewindCheckNews() {
    return Ability.builder().name("check_news_from").info(
            "Get news from a specific time period in the past.\nUsage: /check_news_from <number> <hours|days|weeks|months>\nExample: /check_news_from 2 days")
        .locality(ALL).input(2).privacy(PUBLIC).action(ctx -> {
          botService.rewindLastCheck(this, ctx.chatId(), ctx.firstArg(), ctx.secondArg());
          botService.sendNewsAsync(this, ctx.chatId()).subscribe();
        }).build();
  }

  @Override
  public long creatorId() {
    return adminID;
  }
}
