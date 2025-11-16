package lms.kiu.notifier.lms.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lms.kiu.notifier.lms.api.model.response.subresponses.registrationGroupId.ListItem;
import lombok.Data;

@Data
public class RegistrationGroupIdResponse {

  @JsonProperty("list")
  private List<ListItem> list;

  @JsonProperty("selectedGroupId")
  private int selectedGroupId;
}