package lms.kiu.notifier.lms.nemo.playwright.entry;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.Page.ReloadOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import lms.kiu.notifier.lms.nemo.mongo.model.Student;
import lms.kiu.notifier.lms.nemo.mongo.service.CourseService;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lms.kiu.notifier.lms.nemo.data.Constants;
import lms.kiu.notifier.lms.nemo.playwright.steps.CourseSteps;
import lms.kiu.notifier.lms.nemo.playwright.steps.CourseSubSectionTabSteps;
import lms.kiu.notifier.lms.nemo.playwright.steps.LmsSteps;
import lms.kiu.notifier.lms.nemo.playwright.steps.MainSteps;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for Playwright-based LMS automation.
 * <p>
 * Manages browser lifecycle, authentication, and provides navigation step objects
 * for interacting with the LMS system. Subclasses implement specific automation workflows
 * using the initialized Playwright components.
 */
@Slf4j
public abstract class InitPlaywright {

  private final Playwright playwright;
  private final Browser browser;
  private final BrowserContext browserContext;
  protected final Page page;

  protected final StudentService studentService;
  protected final CourseService courseService;

  protected final Student student;

  protected final MainSteps mainSteps;
  protected final LmsSteps lmsSteps;
  protected final CourseSteps courseSteps;
  protected final CourseSubSectionTabSteps courseSubSectionTabSteps;

  public InitPlaywright(Student student, StudentService studentService,
      CourseService courseService) {
    this.student = student;
    this.studentService = studentService;
    this.courseService = courseService;

    try {
      String token = student.getStudentToken().trim().replaceAll("[\\r\\n\\t]", "");

      if (token.isEmpty()) {
        throw new Exception("Student token is empty");
      }

      log.info("Starting playwright...");

      playwright = Playwright.create();

      browser = playwright.chromium()
          .launch(new LaunchOptions().setHeadless(true).setTimeout(6000));

      browserContext = browser.newContext();
      page = browserContext.newPage();

      log.info("Playwright Launched...");

      authenticateWithToken(token);

      // Initialize step classes
      mainSteps = new MainSteps(page);
      lmsSteps = new LmsSteps(page);
      courseSteps = new CourseSteps(page);
      courseSubSectionTabSteps = new CourseSubSectionTabSteps(page);

    } catch (Exception e) {
      cleanup();
      throw new RuntimeException(
          "Failed to start Playwright for Telegram ID: " + student.getTelegramId(), e);
    }
  }

  private void authenticateWithToken(String token) {
    // Navigate to base URL
    NavigateOptions navOptions = new NavigateOptions();
    navOptions.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);

    page.navigate(Constants.MAIN_URL, navOptions);
    page.waitForTimeout(1000);

    // this simulates Authorization without headaches
    // definitely better and faster than using mail, password and 2FA
    page.evaluate("(token) => localStorage.setItem('Student-Token', token)", token);

    log.info("Token Set in LocalStorage");

    // Reload to apply Authentication
    ReloadOptions reloadOptions = new ReloadOptions();
    reloadOptions.setWaitUntil(WaitUntilState.DOMCONTENTLOADED);
    page.reload(reloadOptions);

    page.waitForTimeout(1500);
  }

  /**
   * Deletes all Playwright's instance on current Thread
   */
  protected void cleanup() {
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
