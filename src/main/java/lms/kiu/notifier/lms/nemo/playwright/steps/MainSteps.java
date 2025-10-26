package lms.kiu.notifier.lms.nemo.playwright.steps;

import com.microsoft.playwright.Page;
import lms.kiu.notifier.lms.nemo.playwright.pages.MainPage;

public class MainSteps {

  MainPage mainPage;
  Page page;

  public MainSteps(Page page) {
    mainPage = new MainPage(page);
    this.page = page;
  }

  public void goToLms() {
    mainPage.lmsButton.click();
  }
}
