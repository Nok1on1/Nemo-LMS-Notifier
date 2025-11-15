package lms.kiu.notifier.lms.api.model.request.subrequests.studentTables;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class DatesItem {

  @JsonProperty("date")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  // this is redundant... Do better LMS
  @JsonProperty("d")
  private int d = 0;
}