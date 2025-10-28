package lms.kiu.notifier.lms.nemo.telegram.bot.service;

import static java.util.concurrent.CompletableFuture.completedFuture;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {

  private final StudentService studentService;
  private final CourseService courseService;
  private final TextEncryptor textEncryptor;

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
   * @param chatId Telegram chat ID of the student
   * @param telegramClient client for sending Telegram messages
   * @param silent sender for non-intrusive messages
   * @return CompletableFuture containing set of initialized course names
   */
  @Async
  public CompletableFuture<Set<String>> initializeStudentAsync(
      Long chatId,
      TelegramClient telegramClient,
      SilentSender silent) {

    log.info("Starting async initialization for chatId: {}", chatId);

    try {
      SendChatAction chatAction = new SendChatAction(chatId.toString(), "typing");
      telegramClient.execute(chatAction);
    } catch (TelegramApiException e) {
      log.error("Failed to send typing action", e);
    }

    Student student = studentService
        .findByTelegramId(chatId)
        .block();


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
   * @param chatId Telegram chat ID of the student
   * @param fileId Telegram file ID of the uploaded token file
   * @param telegramClient client for downloading files from Telegram
   * @return CompletableFuture containing registration result with success/failure message
   * @throws TelegramApiException if file download fails
   * @throws IOException if file reading fails
   */
  @Async("asyncTelegramBot")
  public CompletableFuture<RegistrationResult> processRegistrationAsync(Long chatId,
      String fileId, TelegramClient telegramClient)
      throws TelegramApiException, IOException {

    log.info("Processing registration - chatId: {}, fileId: {}", chatId, fileId);

    File getFile = telegramClient.execute(new GetFile(fileId));
    java.io.File file = telegramClient.downloadFile(getFile);
    String encryptedToken = textEncryptor.encrypt(Files.readString(file.toPath()).trim());


    Student student = Student.builder()
        .telegramId(chatId)
        .studentToken(encryptedToken)
        .build();

    return studentService.save(student)
        .map(savedStudent -> {
          log.info("Student registered successfully for chatId: {}", savedStudent.getTelegramId());
          return new RegistrationResult("✅ Registration successful!");
        })
        .toFuture()
        .thenApply(result -> {
          if (result == null) {
            throw new IllegalStateException("Failed to save student data.");
          }
          return result;
        });
  }

  @Getter
  public static class RegistrationResult {

    private final String message;

    public RegistrationResult(String message) {
      this.message = message;
    }

  }
}