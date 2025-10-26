package lms.kiu.notifier.lms.nemo.playwright.entry;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import lms.kiu.notifier.lms.nemo.mongo.model.Student;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lms.kiu.notifier.lms.nemo.playwright.data.Constants;
import lms.kiu.notifier.lms.nemo.playwright.steps.CourseInteractionTabSteps;
import lms.kiu.notifier.lms.nemo.playwright.steps.CourseSteps;
import lms.kiu.notifier.lms.nemo.playwright.steps.CourseSubSectionTabSteps;
import lms.kiu.notifier.lms.nemo.playwright.steps.LmsSteps;
import lms.kiu.notifier.lms.nemo.playwright.steps.MainSteps;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Playwright entry.
 */
@Slf4j
public class PlaywrightEntry {

  private final Playwright playwright;
  private final Browser browser;
  private final BrowserContext browserContext;
  private final Page page;

  private final StudentService studentService;

  private final Student student;

  private final MainSteps mainSteps;
  private final LmsSteps lmsSteps;
  private final CourseSteps courseSteps;
  private final CourseSubSectionTabSteps courseSubSectionTabSteps;
  private final CourseInteractionTabSteps courseInteractionTabSteps;

  public PlaywrightEntry(Student student, StudentService studentService) {
    this.student = student;
    this.studentService = studentService;

    try {
      log.info("Starting playwright...");

      playwright = Playwright.create();

      browser = playwright.chromium()
          .launch(new LaunchOptions().setHeadless(true).setTimeout(6000));

      String tokenValue = student.getStudentToken();

      if (tokenValue == null || tokenValue.isEmpty()) {
        throw new IllegalStateException(
            "Student token cannot be null or empty for Telegram ID: " + student.getTelegramId());
      }

      // Clean the token
      tokenValue = tokenValue.trim().replaceAll("[\\r\\n\\t]", "");

      // Create browser context and page
      browserContext = browser.newContext();
      page = browserContext.newPage();

      log.info("Playwright Launched...");

      // Navigate to base URL
      NavigateOptions navOptions = new NavigateOptions();
      navOptions.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);

      page.navigate(Constants.MAIN_URL, navOptions);
      page.waitForTimeout(1000);

      // this simulates Authorization without headaches
      // definitely better and faster than using mail, password and 2FA
      final String token = tokenValue;
      page.evaluate("(token) => localStorage.setItem('Student-Token', token)", token);

      log.info("Token Set in LocalStorage");

      // Reload to apply Authentication
      Page.ReloadOptions reloadOptions = new Page.ReloadOptions();
      reloadOptions.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
      page.reload(reloadOptions);

      page.waitForTimeout(1500);

      // Initialize step classes
      mainSteps = new MainSteps(page);
      lmsSteps = new LmsSteps(page);
      courseSteps = new CourseSteps(page);
      courseSubSectionTabSteps = new CourseSubSectionTabSteps(page);
      courseInteractionTabSteps = new CourseInteractionTabSteps(page);

    } catch (Exception e) {
      cleanup();
      throw new RuntimeException(
          "Failed to start Playwright for Telegram ID: " + student.getTelegramId(), e);
    }
  }

  /**
   * Fetch new messages From LMS subjects' interaction pages
   *
   * @return Hashmap: key - (URL of Messages' Page), value - (Number of New Messages)
   */
  public HashMap<String, Integer> fetchNewMessages() {
    HashMap<String, Integer> newMessages = new HashMap<>();

    LocalDateTime lastCheck = Objects.requireNonNull(studentService
            .findByTelegramId(student.getTelegramId())
            .block())
        .getLastCheck();

    // LMS -> Course -> Subsection -> Interaction
    // Depth First Traversal
    try {
      page.navigate(Constants.MAIN_PAGE);
      mainSteps.goToLms();

      lmsSteps.buildCourseIterator();

      Locator course;

      while (true) {
        course = lmsSteps.goToNextCourse();

        if (course == null) {
          break;
        }

        courseSteps.buildSubSectionIterator();

        while (courseSteps.goToNextSubSection()) {

          courseSubSectionTabSteps.goToInteraction();

          newMessages.putAll(courseInteractionTabSteps.getNewMessages(lastCheck));

          page.goBack();

          if (courseSteps.hasNextSubSection()) {
            course.click();
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "Error fetching messages for Telegram ID: " + student.getTelegramId(), e);
    } finally {
      cleanup();
    }

    return newMessages;
  }

  /**
   * Deletes all Playwright's instance on current Thread
   */
  private void cleanup() {
    log.info("Playwright - Cleanup...");
    try {
      if (page != null) {
        page.close();
      }
      if (browserContext != null) {
        browserContext.close();
      }
      if (browser != null) {
        browser.close();
      }
      if (playwright != null) {
        playwright.close();
      }
      log.info("Playwright - Cleanup Done");
    } catch (Exception e) {
      System.err.println("Error during Playwright cleanup: " + e.getMessage());
    }
  }
}
