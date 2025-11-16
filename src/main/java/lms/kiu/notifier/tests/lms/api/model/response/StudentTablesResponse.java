package lms.kiu.notifier.tests.lms.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.studentTables.DataItemItem;
import lombok.Data;

@Data
public class StudentTablesResponse {

  @JsonProperty("result")
  private String result;

  @JsonProperty("data")
  private List<List<DataItemItem>> data;

  @JsonProperty("time")
  private int time;
}
