package lms.kiu.notifier.lms.api.model.response.subresponses.announcemet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataItem {

  @JsonProperty("comments")
  private List<Object> comments;

  @JsonProperty("updated_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;

  @JsonProperty("group_id")
  private int groupId;

  @JsonProperty("created_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonProperty("id")
  private int id;

  @JsonProperty("course_group")
  private CourseGroup courseGroup;

  @JsonProperty("title")
  private String title;

  @JsonProperty("file_urls")
  private List<Object> fileUrls;
}