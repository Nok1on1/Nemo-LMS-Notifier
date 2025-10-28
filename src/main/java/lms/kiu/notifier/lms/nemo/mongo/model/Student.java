package lms.kiu.notifier.lms.nemo.mongo.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lms.kiu.notifier.lms.nemo.playwright.entry.InitPlaywright;
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
 * {@link InitPlaywright}
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
  // Last time student checked News
  @Builder.Default
  private LocalDateTime lastCheck = LocalDateTime.now().minusMonths(3);
  // Student info shouldn't be stored permanently
  @Indexed(expireAfter = "7d")
  @Builder.Default
  private Instant createdAt = Instant.now();
}