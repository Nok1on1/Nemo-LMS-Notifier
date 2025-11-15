package lms.kiu.notifier.tests.lms.api.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard.EducationsItem;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard.ProgramViewItem;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard.ProgramsItem;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard.Student;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard.View;
import lombok.Data;

@Data
public class DashboardResponse {

  @JsonProperty("result")
  private String result;

  @JsonProperty("view")
  private View view;

  @JsonProperty("educationInfo")
  private Object educationInfo;

  @JsonProperty("student")
  private Student student;

  @JsonProperty("programView")
  private List<ProgramViewItem> programView;

  @JsonProperty("educations")
  private List<EducationsItem> educations;

  @JsonProperty("programs")
  private List<ProgramsItem> programs;
}