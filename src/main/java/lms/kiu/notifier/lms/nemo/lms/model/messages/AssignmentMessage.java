package lms.kiu.notifier.lms.nemo.lms.model.messages;

import static lms.kiu.notifier.lms.nemo.lms.util.MessageUtils.cropMessage;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.jsoup.Jsoup;

@Data
@Builder
public class AssignmentMessage {

  private String courseName;

  private String title;

  private String description;

  private LocalDate endDate;

  private List<String> embeddedFileLinks;

  @Override
  public String toString() {
    return String.format("""
        Assignment:
          end Date: %s
          courseName: %s
          title: %s
          description:
            %s
          embeddedFileLinks: %s
        """, endDate.toString(), courseName, title, description, embeddedFileLinks);
  }

  public static class AssignmentMessageBuilder {

    public AssignmentMessageBuilder description(String description) {
      if (description != null) {
        this.description = cropMessage(Jsoup.parse(description).wholeText(), 3000);
      }

      return this;
    }

    public AssignmentMessageBuilder title(String title) {
      if (title != null) {
        this.title = cropMessage(title, 3000);
      }

      return this;
    }
  }
}
