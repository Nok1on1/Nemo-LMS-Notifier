package lms.kiu.notifier.lms.nemo.playwright.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Post {

  String url;
  LocalDateTime date;
  String message;

  @Override
  public String toString() {
    return "Post: " + "\n"
        + " url: " + url + "\n"
        + " date: " + date.toLocalDate() + "\n"
        + "  " + message + "\n";
  }
}
