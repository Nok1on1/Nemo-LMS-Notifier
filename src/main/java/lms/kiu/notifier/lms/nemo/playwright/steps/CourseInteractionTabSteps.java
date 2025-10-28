package lms.kiu.notifier.lms.nemo.playwright.steps;

import static lms.kiu.notifier.lms.nemo.playwright.data.Constants.PLAYWRIGHT_THREAD_SLEEP_TIME;
import static lms.kiu.notifier.lms.nemo.playwright.util.MessageUtils.cropMessages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lms.kiu.notifier.lms.nemo.playwright.data.Constants;
import lms.kiu.notifier.lms.nemo.playwright.model.Post;
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

  public List<Post> getNewMessages(LocalDateTime lastCheck) {

    log.info("Playwright - Retrieving New Messages...");

    try {
      Thread.sleep(PLAYWRIGHT_THREAD_SLEEP_TIME);
    } catch (InterruptedException e) {
      return new LinkedList<>();
    }


    int postsCount = courseInteractionTabpage.messagesLocator.all().size();

    if (postsCount == 0) {
      return new LinkedList<>();
    }

    int postMessageLimit = Constants.TELEGRAM_CHAT_LIMIT / postsCount;

    List<Post> posts = new LinkedList<>();

    for (var messageLocator : courseInteractionTabpage.messagesLocator.all()) {
      LocalDateTime messageDate = getTime(messageLocator);

      String limitedMessage = cropMessages(getMessage(messageLocator), postMessageLimit);

      if (messageDate.isAfter(lastCheck)) {
        posts.add(
            Post.builder()
                .url(page.url())
                .date(messageDate)
                .message(limitedMessage)
                .build());
      }

    }

    return posts;
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

  private String getMessage(Locator messageLocator) {
    return messageLocator
        .locator(".announcement-title > div:nth-child(2)")
        .innerText();
  }


}
