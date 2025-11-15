package lms.kiu.notifier.lms.api.model.response.subresponses.assignment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lms.kiu.notifier.lms.api.util.FlexibleListDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataItem {

  @JsonProperty("end_date")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @JsonProperty("week")
  private String week;

  @JsonProperty("est_list_group_component_id")
  private int estListGroupComponentId;

  @JsonProperty("end_time")
  private String endTime;

  @JsonProperty("co_id")
  private int coId;

  @JsonProperty("student_answer")
  private Object studentAnswer;

  @JsonProperty("description")
  private String description;

  @JsonProperty("created_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonProperty("title")
  private String title;

  @JsonProperty("file_urls")
  @JsonDeserialize(using = FlexibleListDeserializer.class)
  private List<String> fileUrls;

  @JsonProperty("student_comment")
  private Object studentComment;

  @JsonProperty("start_time")
  private String startTime;

  @JsonProperty("updated_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;

  @JsonProperty("group_id")
  private int groupId;

  @JsonProperty("files")
  private Object files;

  @JsonProperty("id")
  private int id;

  @JsonProperty("in_testing")
  private int inTesting;

  @JsonProperty("student_files")
  private Object studentFiles;

  @JsonProperty("key")
  private int key;

  @JsonProperty("status")
  private String status;

  @JsonProperty("start_date")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;
}