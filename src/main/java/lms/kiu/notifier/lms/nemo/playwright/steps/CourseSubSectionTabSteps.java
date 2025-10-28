package lms.kiu.notifier.lms.nemo.playwright.steps;

import com.microsoft.playwright.Page;
import lms.kiu.notifier.lms.nemo.playwright.pages.CourseSubSectionTabPage;

public class CourseSubSectionTabSteps {

  Page page;
  CourseSubSectionTabPage courseSubSectionTabPage;

  public CourseSubSectionTabSteps(Page page) {
    this.page = page;
    courseSubSectionTabPage = new CourseSubSectionTabPage(page);
  }

  public void goToInteraction() {
    courseSubSectionTabPage.interactionTab.click();
  }
}
