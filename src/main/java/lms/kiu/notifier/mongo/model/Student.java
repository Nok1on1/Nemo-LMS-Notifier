package lms.kiu.notifier.mongo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Represents a registered student linked to a specific {@code telegramId}. <br/> This Entity stores
 * LMS {@code studentToken} used to replicate student in Playwright Service. <br/> Related
 */
@Document(collection = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

  List<String> enrolledCourseIds;

  @Id
  private String id;

  // Same As ChatId
  @Indexed(unique = true, sparse = true)
  private Long telegramId;
  // Token Used To Replicate Student
  private String studentToken;
  // Last time a student checked News
  @Builder.Default
  private LocalDateTime lastCheck = LocalDateTime.now().minusMonths(3);
  // Student info shouldn't be stored permanently
  @Indexed(expireAfter = "7d")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @JsonProperty("studentId")
  private int studentId;

  @JsonProperty("groupId")
  private int groupId;

  @JsonProperty("eduId")
  private int eduId;
}