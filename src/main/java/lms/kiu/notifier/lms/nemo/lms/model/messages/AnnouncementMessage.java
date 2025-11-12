package lms.kiu.notifier.lms.nemo.lms.model.messages;

import static lms.kiu.notifier.lms.nemo.lms.util.MessageUtils.cropMessage;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.jsoup.Jsoup;


@Data
@Builder
public class AnnouncementMessage {

  private String url;
  private String courseName;
  private String message;
  private LocalDateTime time;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Post:\n");

    if (courseName != null) {
      sb.append("  courseName: ").append(courseName).append("\n");
    }
    if (time != null) {
      sb.append("  time: ").append(time.toLocalDate()).append("\n");
    }
    if (url != null) {
      sb.append("  url: ").append(url).append("\n");
    }
    if (message != null) {
      sb.append("  message:\n    ").append(message).append("\n");
    }

    return sb.toString();
  }


  public static class AnnouncementMessageBuilder {

    public AnnouncementMessageBuilder url(int courseId, int groupId) {
      this.url = String.format(
          "https://lms.kiu.edu.ge/#/lms/courses/%d/group/%d",
          courseId, groupId
      );
      return this;
    }

    public AnnouncementMessageBuilder message(String message) {
      if (message != null) {
        this.message = cropMessage(Jsoup.parse(message).wholeText(), 3800);
      }
      return this;
    }
  }
}
