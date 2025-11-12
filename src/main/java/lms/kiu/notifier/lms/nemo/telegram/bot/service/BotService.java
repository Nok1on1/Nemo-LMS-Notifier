package lms.kiu.notifier.lms.nemo.telegram.bot.service;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static lms.kiu.notifier.lms.nemo.data.Constants.INVALID_TIME_PERIOD;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lms.kiu.notifier.lms.nemo.data.Constants;
import lms.kiu.notifier.lms.nemo.lms.model.messages.AnnouncementMessage;
import lms.kiu.notifier.lms.nemo.lms.model.messages.AssignmentMessage;
import lms.kiu.notifier.lms.nemo.lms.service.LMSService;
import lms.kiu.notifier.lms.nemo.mongo.model.Student;
import lms.kiu.notifier.lms.nemo.mongo.service.CourseService;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lms.kiu.notifier.lms.nemo.playwright.entry.StudentInitializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {

  private final StudentService studentService;
  private final CourseService courseService;
  private final TextEncryptor textEncryptor;
  private final LMSService lmsService;

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
   * @param chatId         Telegram chat ID of the student
   * @param telegramClient client for sending Telegram messages
   * @param silent         sender for non-intrusive messages
   * @return CompletableFuture containing set of initialized course names
   */
  @Async
  public CompletableFuture<Set<String>> initializeStudentAsync(Long chatId,
      TelegramClient telegramClient, SilentSender silent) {

    log.info("Starting async initialization for chatId: {}", chatId);

    try {
      SendChatAction chatAction = new SendChatAction(chatId.toString(), "typing");
      telegramClient.execute(chatAction);
    } catch (TelegramApiException e) {
      log.error("Failed to send typing action", e);
    }

    Student student = studentService.findByTelegramId(chatId).block();

    if (student == null) {
      log.warn("Student not found for chatId: {}", chatId);

      silent.send("It looks like you haven't registered yet.", chatId);
      silent.send("type /start for instructions", chatId);

      return completedFuture(new HashSet<>());
    }

    student.setStudentToken(textEncryptor.decrypt(student.getStudentToken()));

    log.info("Student found: {}", student.getTelegramId());

    Set<String> courseTitles = new StudentInitializer(student, studentService,
        courseService).initializeStudent().getCourseNames();

    silent.send("initialization complete!", chatId);

    return completedFuture(courseTitles);
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
   * @param telegramId     Telegram chat ID of the student
   * @param fileId         Telegram file ID of the uploaded token file
   * @param telegramClient client for downloading files from Telegram
   * @return CompletableFuture containing registration result with success/failure message
   * @throws TelegramApiException if file download fails
   * @throws IOException          if file reading fails
   */
  @Async("asyncTelegramBot")
  public CompletableFuture<String> processRegistrationAsync(long telegramId,
      String fileId,
      TelegramClient telegramClient) throws TelegramApiException, IOException {

    if (studentService.findByTelegramId(telegramId).block() != null) {
      studentService.deleteStudentByTelegramId(telegramId).block();
    }

    log.info("Processing registration - chatId: {}, fileId: {}", telegramId, fileId);

    File getFile = telegramClient.execute(new GetFile(fileId));
    java.io.File file = telegramClient.downloadFile(getFile);
    String encryptedToken = textEncryptor.encrypt(Files.readString(file.toPath()).trim());

    Student student = Student.builder().telegramId(telegramId).studentToken(encryptedToken).build();

    return studentService.save(student).map(savedStudent -> {
      log.info("Student registered successfully for chatId: {}", savedStudent.getTelegramId());
      return "✅ Registration successful!";
    }).toFuture().thenApply(result -> {
      if (result == null) {
        throw new IllegalStateException("Failed to save student data.");
      }
      return result;
    });
  }

  @Async
  public CompletableFuture<Student> sendNews(AbilityBot kiuNemoBot, long telegramId) {
    kiuNemoBot.getSilent().send("Sending check requests...", telegramId);

    return Mono.zip(Mono.fromFuture(() -> lmsService.checkNewAssignments(telegramId)),
        Mono.fromFuture(() -> lmsService.checkNewAnnouncements(telegramId))).flatMap(tuple -> {
      List<AssignmentMessage> assignments = tuple.getT1();
      List<AnnouncementMessage> announcements = tuple.getT2();

      if (assignments.isEmpty()) {
        kiuNemoBot.getSilent().send("No new assignments", telegramId);
      } else {
        for (var assignment : assignments) {
          kiuNemoBot.getSilent().send(assignment.toString(), telegramId);
        }

      }

      if (announcements.isEmpty()) {
        kiuNemoBot.getSilent().send("No new announcements", telegramId);
      } else {
        for (var announcement : announcements) {
          kiuNemoBot.getSilent().send(announcement.toString(), telegramId);
        }
      }

      return studentService.updateLastCheck(telegramId);
    }).onErrorResume(ex -> {
      log.error("Error checking news for chatId: {}", telegramId);
      kiuNemoBot.getSilent().send(Constants.FAILED_CHECKING_NEWS, telegramId);
      return Mono.empty();
    }).toFuture();
  }

  @Async
  public void rewindLastCheck(AbilityBot kiuNemoBot, long telegramId, String timePeriodNum,
      String timeUnit) {
    long rewindDays;

    try {
      rewindDays = Long.parseLong(timePeriodNum);
    } catch (NumberFormatException e) {
      kiuNemoBot.getSilent()
          .send(INVALID_TIME_PERIOD,
              telegramId);
      return;
    }

    switch (timeUnit.toLowerCase()) {
      case "months" -> rewindDays *= 30;
      case "weeks" -> rewindDays *= 7;
      case "days" -> {
      }
      case "hours" -> rewindDays /= 24;
      default -> {
        kiuNemoBot.getSilent()
            .send(Constants.INVALID_TIME_UNIT, telegramId);
        return;
      }
    }

    if (rewindDays <= 0) {
      kiuNemoBot.getSilent()
          .send(Constants.NEGATIVE_TIME_PERIOD_ERROR, telegramId);
      return;
    }

    LocalDateTime timeRewind = LocalDateTime.now().minusDays(rewindDays);
    studentService.modifyLastCheck(telegramId, timeRewind).block();

    kiuNemoBot.getSilent()
        .send(String.format("✅ Last check rewinded by %d %s\n" +
                "📅 Current last check: %s",
            rewindDays, "days", timeRewind), telegramId);
  }
}