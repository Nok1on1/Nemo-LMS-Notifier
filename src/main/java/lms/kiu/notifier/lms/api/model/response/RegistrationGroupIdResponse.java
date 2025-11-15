package lms.kiu.notifier.lms.api.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lms.kiu.notifier.lms.api.model.response.subresponses.registrationGroupId.ListItem;
import lombok.Data;

@Data
public class RegistrationGroupIdResponse {

  @JsonProperty("list")
  private List<ListItem> list;

  @JsonProperty("selectedGroupId")
  private int selectedGroupId;
}