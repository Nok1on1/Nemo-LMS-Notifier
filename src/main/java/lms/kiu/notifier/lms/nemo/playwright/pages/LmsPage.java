package lms.kiu.notifier.lms.nemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LmsPage {

  Page page;

  public Locator coursesLocator;

  public LmsPage(Page page) {
    this.page = page;

    coursesLocator = page.locator(".course");
  }
}
