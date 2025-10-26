package lms.kiu.notifier.lms.nemo.telegram.bot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import lms.kiu.notifier.lms.nemo.mongo.model.Student;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lms.kiu.notifier.lms.nemo.playwright.entry.PlaywrightEntry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
public class AsyncBotService {

  private final StudentService studentService;

  @Async
  public CompletableFuture<HashMap<String, Integer>> checkNewsAsync(
      Long chatId,
      TelegramClient telegramClient,
      SilentSender silent) {

    log.info("Starting async news check for chatId: {}", chatId);

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

      return CompletableFuture.completedFuture(new HashMap<>());
    }

    log.info("Student found: {}", student.getTelegramId());

    HashMap<String, Integer> newsResults = new PlaywrightEntry(student,
        studentService).fetchNewMessages();

    silent.send("News check complete! Found " + newsResults.size() + " updates.", chatId);

    return CompletableFuture.completedFuture(newsResults);
  }

  @Async("asyncTelegramBot")
  public CompletableFuture<RegistrationResult> processRegistrationAsync(Long chatId,
      String fileId, TelegramClient telegramClient)
      throws TelegramApiException, IOException {

    File getFile = telegramClient.execute(new GetFile(fileId));
    java.io.File file = telegramClient.downloadFile(getFile);
    String studentToken = Files.readString(file.toPath()).trim();

    Student student = Student.builder()
        .telegramId(chatId)
        .studentToken(studentToken)
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