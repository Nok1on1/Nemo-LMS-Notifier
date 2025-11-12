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
    StringBuilder sb = new StringBuilder("Assignment:\n");

    if (endDate != null) {
      sb.append("  endDate: ").append(endDate).append("\n");
    }
    if (courseName != null) {
      sb.append("  courseName: ").append(courseName).append("\n");
    }
    if (title != null) {
      sb.append("  title: ").append(title).append("\n");
    }
    if (description != null) {
      sb.append("  description:\n    ").append(description).append("\n");
    }
    if (embeddedFileLinks != null && !embeddedFileLinks.isEmpty()) {
      sb.append("  embeddedFileLinks: ").append(embeddedFileLinks).append("\n");
    }
    return sb.toString();
  }


  public static class AssignmentMessageBuilder {

    public AssignmentMessageBuilder description(String description) {
      this.description = cropMessage(Jsoup.parse(description).wholeText(), 3000);
      return this;
    }

    public AssignmentMessageBuilder title(String title) {
      this.title = cropMessage(title, 3000);
      return this;
    }
  }
}
