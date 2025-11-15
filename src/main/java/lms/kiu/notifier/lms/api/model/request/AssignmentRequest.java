package lms.kiu.notifier.lms.api.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequest {

  @JsonProperty("id")
  private int courseId;

  @JsonProperty("group_id")
  private int groupId;

  @JsonProperty("user_type")
  private String userType = "student";

  public AssignmentRequest(int courseId, int groupId) {
    this.groupId = groupId;
    this.courseId = courseId;
  }
}
