package lms.kiu.notifier.lms.api.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCoursesInfoRequest {

  @JsonProperty("registrationGroupId")
  private int registrationGroupId;

  @JsonProperty("learning_year")
  private String learningYear;
}