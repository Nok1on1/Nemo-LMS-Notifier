package lms.kiu.notifier.lms.nemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CourseInteractionTabPage {

  Page page;

  public Locator messagesLocator;

  public CourseInteractionTabPage(Page page) {
    this.page = page;

    messagesLocator = page.locator(".interaction-announcements-wrapper > div");
  }

}
