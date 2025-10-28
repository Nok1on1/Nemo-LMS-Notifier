package lms.kiu.notifier.lms.nemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CourseSubSectionTabPage {

  public Locator interactionTab;
  public Locator workspaceTab;
  public Locator Materials;
  Page page;

  public CourseSubSectionTabPage(Page page) {
    this.page = page;
    interactionTab = page.locator(".mat-mdc-tab-labels > div").first();
    workspaceTab = page.locator(".mat-mdc-tab-labels > div").nth(1);
    Materials = page.locator(".mat-mdc-tab-labels > div").nth(2);
  }
}
