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
  private String message;
  private LocalDateTime time;

  @Override
  public String toString() {
    return String.format("""
        Post:
         time: %s
         url: %s
         message:
          %s
        """, time.toLocalDate(), url, message);
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
