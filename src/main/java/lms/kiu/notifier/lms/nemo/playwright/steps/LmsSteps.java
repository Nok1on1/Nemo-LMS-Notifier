package lms.kiu.notifier.lms.nemo.playwright.steps;

import static lms.kiu.notifier.lms.nemo.data.Constants.PLAYWRIGHT_THREAD_SLEEP_TIME;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.Iterator;
import lms.kiu.notifier.lms.nemo.playwright.pages.LmsPage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LmsSteps {

  LmsPage lmsPage;
  Page page;
  Iterator<Locator> courseIterator;

  public LmsSteps(Page page) {
    this.page = page;
    lmsPage = new LmsPage(page);
  }

  public void buildCourseIterator() {
    try {
      Thread.sleep(PLAYWRIGHT_THREAD_SLEEP_TIME);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    courseIterator = lmsPage.coursesLocator.all().iterator();
    log.info(lmsPage.coursesLocator.all().size() + " courses found");
  }

  public Locator getNextCourse() {
    Locator nextCourse = null;
    if (courseIterator.hasNext()) {
      nextCourse = courseIterator.next();
    }
    return nextCourse;
  }
}
