package lms.kiu.notifier.telegram.bot.service;

import static lms.kiu.notifier.data.Constants.INVALID_TIME_PERIOD_ERROR;
import static lms.kiu.notifier.data.Constants.REGISTRATION_HELPER_MESSAGE;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import lms.kiu.notifier.data.Constants;
import lms.kiu.notifier.lms.api.model.request.GetCoursesInfoRequest;
import lms.kiu.notifier.lms.api.service.LMSService;
import lms.kiu.notifier.mongo.model.Course;
import lms.kiu.notifier.mongo.model.Student;
import lms.kiu.notifier.mongo.service.CourseService;
import lms.kiu.notifier.mongo.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.telegrambots.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {

  private final StudentService studentService;
  private final CourseService courseService;
  private final TextEncryptor textEncryptor;
  private final LMSService lmsService;

  @Async
  public void sendRegistrationInstructions(BaseAbilityBot kiuNemoBot, long chatId) {
    InputStream imageStream = getClass().getResourceAsStream("/CopyToken.png");

    try (imageStream) {
      try {
        if (imageStream == null) {
          kiuNemoBot.getSilent().send("Could not load registration instructions", chatId);
          return;
        }
        SendPhoto sendPhoto = SendPhoto.builder().chatId(chatId)
            .photo(new InputFile(imageStream, "copyToken.png")).caption(REGISTRATION_HELPER_MESSAGE)
            .build();
        kiuNemoBot.getTelegramClient().execute(sendPhoto);
      } catch (TelegramApiException e) {
        kiuNemoBot.getSilent().send("failed to fetch photo", chatId);
      }
    } catch (IOException e) {
      log.error("Error closing image stream", e);
    }
  }


  /**
   * Initializes a student's enrolled courses asynchronously using Playwright automation.
   * <p>
   * This method:
   * <ol>
   *   <li>Verifies student exists in database</li>
   *   <li>Decrypts student token for LMS authentication</li>
   *   <li>Launches Playwright to scrape enrolled courses from LMS</li>
   *   <li>Stores course data in database</li>
   *   <li>Returns set of course names</li>
   * </ol>
   * <p>
   * Shows typing indicator to user during processing. If student not found,
   * sends registration instructions and returns empty set.
   *
   * @param ctx context given by AbilityBot Action
   * @return CompletableFuture containing set of initialized course names
   */
  public Mono<Void> initializeStudentAsync(MessageContext ctx) {
    SilentSender silent = ctx.bot().getSilent();
    TelegramClient telegramClient = ctx.bot().getTelegramClient();
    Long chatId = ctx.chatId();

    silent.send(String.format(
        "Starting up Playwright automation Script on: %s \n this will take up to 1 minute...",
        Constants.MAIN_URL), chatId);

    try {
      SendChatAction chatAction = new SendChatAction(chatId.toString(), "typing");
      telegramClient.execute(chatAction);
    } catch (TelegramApiException e) {
      log.error("Failed to send typing action", e);
    }

    return studentService.findByTelegramId(chatId).switchIfEmpty(Mono.fromRunnable(() -> {
      silent.send("It looks like you haven't registered yet.", chatId);
      silent.send("type /start for instructions", chatId);
    }).then(Mono.empty())).flatMap(student -> {
      String decryptedToken = textEncryptor.decrypt(student.getStudentToken());

      return lmsService.getRegistrationGroupIdResponse(decryptedToken)
          .flatMap(GroupIdRes -> {
            var getCoursesInfoReq = GetCoursesInfoRequest.builder()
                .learningYear(GroupIdRes.getList().getFirst().getSeasonYear())
                .registrationGroupId(GroupIdRes.getSelectedGroupId()).build();

            return lmsService.getStudentCoursesInfo(decryptedToken, getCoursesInfoReq);
          })
          .flatMapMany(CoursesInfoRes ->
              Flux.fromIterable(CoursesInfoRes.getData())
                  .flatMap(data -> Flux.fromIterable(data.getItems()))
                  .map(item -> Course.builder()
                      .courseName(item.getName())
                      .courseId(item.getListId())
                      .groupId(item.getCourseGroupId())
                      .build())
          )
          .flatMap(courseService::save)
          .flatMap(course -> studentService.addCourse(student.getTelegramId(), course))
          .then();
    });
  }

  /**
   * Processes student registration asynchronously by validating and storing student token.
   * <p>
   * Process flow:
   * <ol>
   *   <li>Downloads token file from Telegram</li>
   *   <li>Reads and encrypts the token</li>
   *   <li>Creates new student record with encrypted token</li>
   *   <li>Saves to database</li>
   *   <li>Returns registration result</li>
   * </ol>
   *
   * @param bot BotInstance
   * @param upd update from Telegram
   * @return registered student
   */
  public Mono<Student> processRegistrationAsync(BaseAbilityBot bot, Update upd) {
    if (!upd.hasMessage()) {
      return Mono.empty();
    }

    Message message = upd.getMessage();
    long telegramId = message.getChatId();
    SilentSender silent = bot.getSilent();
    TelegramClient telegramClient = bot.getTelegramClient();

    if (!message.hasDocument()) {
      silent.send("Please send a valid file!", telegramId);
      return Mono.empty();
    }

    Document document = message.getDocument();
    String fileId = document.getFileId();
    String fileName = document.getFileName();
    Long fileSize = document.getFileSize();

    log.info("Received file - ID: {}, Name: {}, Size: {} bytes", fileId, fileName, fileSize);

    if (!fileName.endsWith(".txt")) {
      silent.send(Constants.FILE_NOT_TXT_ERROR, telegramId);
      return Mono.empty();
    }
    if (fileSize > 10 * 1024) {
      silent.send("File is too big for it to be a Token, stop breaking things!", telegramId);
      return Mono.empty();
    }

    silent.send("Processing your registration...", telegramId);

    return studentService.findByTelegramId(telegramId)
        .flatMap(x -> studentService.deleteStudentByTelegramId(telegramId))
        .then(Mono.fromCallable(() -> {
          File getFile = telegramClient.execute(new GetFile(fileId));
          java.io.File file = telegramClient.downloadFile(getFile);
          return Files.readString(file.toPath()).trim();
        }).subscribeOn(Schedulers.boundedElastic()))
        .map(textEncryptor::encrypt)
        .flatMap(encToken -> studentService.save(
            Student.builder().telegramId(telegramId).studentToken(encToken).build()));
  }

  public Mono<Student> sendNewsAsync(AbilityBot bot, long telegramId) {
    return Flux.concat(lmsService.checkNewAssignments(telegramId),
            lmsService.checkNewAnnouncements(telegramId)).switchIfEmpty(
            Mono.fromRunnable(() -> bot.getSilent().send("No new news found!", telegramId)))
        .doFirst(() -> bot.getSilent().send("Checking for new news...", telegramId))
        .doOnNext(msg -> bot.getSilent().send(msg.toString(), telegramId))
        .then(studentService.updateLastCheck(telegramId)).doOnError(ex -> {
          log.error("Error checking news for chatId: {}", telegramId, ex);
          bot.getSilent().send(Constants.FAILED_CHECKING_NEWS_ERROR, telegramId);
        });
  }

  @Async
  public void rewindLastCheck(AbilityBot kiuNemoBot, long telegramId, String timePeriodNum,
      String timeUnit) {
    long rewindDays;

    try {
      rewindDays = Long.parseLong(timePeriodNum);
    } catch (NumberFormatException e) {
      kiuNemoBot.getSilent().send(INVALID_TIME_PERIOD_ERROR, telegramId);
      return;
    }

    switch (timeUnit.toLowerCase()) {
      case "months" -> rewindDays *= 30;
      case "weeks" -> rewindDays *= 7;
      case "days" -> {
      }
      case "hours" -> rewindDays /= 24;
      default -> {
        kiuNemoBot.getSilent().send(Constants.INVALID_TIME_UNIT_ERROR, telegramId);
        return;
      }
    }

    if (rewindDays <= 0) {
      kiuNemoBot.getSilent().send(Constants.NEGATIVE_TIME_PERIOD_ERROR, telegramId);
      return;
    }

    LocalDateTime timeRewind = LocalDateTime.now().minusDays(rewindDays);

    long finalRewindDays = rewindDays;
    studentService.modifyLastCheck(telegramId, timeRewind).doOnSuccess(v -> kiuNemoBot.getSilent()
        .send(String.format("✅ Last check rewound by %d %s\n" + "📅 Current last check: %s",
            finalRewindDays, "days", timeRewind), telegramId)).subscribe();
  }
}