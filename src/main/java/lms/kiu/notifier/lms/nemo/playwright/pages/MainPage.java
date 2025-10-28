package lms.kiu.notifier.lms.nemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;


public class MainPage {

  Page page;

  public Locator lmsButton;

  public MainPage(Page page) {
    this.page = page;

    lmsButton = page.locator(".dashboard-item-wrapper:has(div.title:text(\"LMS\"))");
  }
}
