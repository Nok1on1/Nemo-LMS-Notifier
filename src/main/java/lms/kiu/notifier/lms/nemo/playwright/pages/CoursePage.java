package lms.kiu.notifier.lms.nemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CoursePage {
  Page page;

  public Locator subsectionsLocator;

  public CoursePage(Page page) {
    this.page = page;
    subsectionsLocator = page.locator(".course");
  }
}
