package lms.kiu.notifier.lms.api.model.request;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lms.kiu.notifier.lms.api.model.request.subrequests.studentTables.DatesItem;
import lombok.Data;


@Data
public class StudentTablesRequest {

  @JsonProperty("studentId")
  private int studentId;

  @JsonProperty("groupId")
  private int groupId;

  @JsonProperty("dates")
  private List<DatesItem> dates;

  @JsonProperty("eduId")
  private int eduId;
}