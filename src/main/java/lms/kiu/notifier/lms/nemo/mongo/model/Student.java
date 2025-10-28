package lms.kiu.notifier.lms.nemo.mongo.model;

import java.time.Instant;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;


/**
 * Represents a registered student linked to a specific {@code telegramId}.
 * <br/>
 * This Entity stores LMS {@code studentToken} used to replicate student in Playwright Service.
 * <br/>
 * Related {@link lms.kiu.notifier.lms.nemo.playwright.entry.PlaywrightEntry}
 */
@Document(collection = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

  @Id
  private String id;

  // Same As ChatId
  @Indexed(unique = true)
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