package lms.kiu.notifier.lms.nemo.playwright.steps;

import static lms.kiu.notifier.lms.nemo.playwright.data.Constants.PLAYWRIGHT_THREAD_SLEEP_TIME;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.Iterator;
import lms.kiu.notifier.lms.nemo.playwright.pages.CoursePage;

public class CourseSteps {

  Page page;
  CoursePage coursePage;
  Iterator<Locator> iterator;

  public CourseSteps(Page page) {
    this.page = page;
    coursePage = new CoursePage(page);
  }

  public void buildSubSectionIterator() {
    try {
      Thread.sleep(PLAYWRIGHT_THREAD_SLEEP_TIME);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    iterator = coursePage.subsectionsLocator.all().iterator();
  }

  public boolean goToNextSubSection() {
    if (iterator.hasNext()) {
      iterator.next().click();
      return true;
    } else {
      return false;
    }
  }
  public boolean hasNextSubSection(){
    return iterator.hasNext();
  }
}
