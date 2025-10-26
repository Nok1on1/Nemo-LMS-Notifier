package lms.kiu.notifier.lms.nemo.playwright.steps;

import static lms.kiu.notifier.lms.nemo.playwright.data.Constants.PLAYWRIGHT_THREAD_SLEEP_TIME;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lms.kiu.notifier.lms.nemo.playwright.pages.CourseInteractionTabPage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CourseInteractionTabSteps {

  private static final Pattern timePattern = Pattern.compile(
      "(\\d{1,2}/\\d{1,2}/\\d{2}, \\d{2}:\\d{2})");
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy, HH:mm");

  Page page;
  CourseInteractionTabPage courseInteractionTabpage;


  public CourseInteractionTabSteps(Page page) {
    this.page = page;
    courseInteractionTabpage = new CourseInteractionTabPage(page);
  }

  public Map<String, Integer> getNewMessages(LocalDateTime lastCheck) {
    int newMessageCounter = 0;

    log.info("Playwright - Retrieving New Messages...");

    try {
      Thread.sleep(PLAYWRIGHT_THREAD_SLEEP_TIME);
    } catch (InterruptedException e) {
      return Map.of();
    }

    for (var message : courseInteractionTabpage.messagesLocator.all()) {
      if (getTime(message).isAfter(lastCheck)) {
        newMessageCounter++;
      }
    }

    if (newMessageCounter > 0) {
      log.info("Playwright - Retrieved {} Messages", newMessageCounter);
      return Map.of(page.url(), newMessageCounter);
    } else {
      return Map.of();
    }
  }


  private LocalDateTime getTime(Locator message) {
    String time = message.locator(".time-ago").innerText().replace("(Edited)", "").trim();

    Matcher m = timePattern.matcher(time);
    if (m.find()) {
      return LocalDateTime.parse(m.group(1), formatter);
    } else {
      throw new IllegalArgumentException("Could not parse date from: " + time);
    }
  }


}
