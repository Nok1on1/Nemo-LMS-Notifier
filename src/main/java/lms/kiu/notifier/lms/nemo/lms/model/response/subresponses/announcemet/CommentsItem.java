package lms.kiu.notifier.lms.nemo.lms.model.response.subresponses.announcemet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsItem{

	@JsonProperty("professor")
	private String professor;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("student")
	private Student student;

	@JsonProperty("announcement_id")
	private int announcementId;

	@JsonProperty("student_id")
	private int studentId;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("id")
	private int id;

	@JsonProperty("prof_id")
	private Object profId;
}