package lms.kiu.notifier.lms.api.model.response.subresponses.announcemet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseGroup{

	@JsonProperty("seminar_ids")
	private String seminarIds;

	@JsonProperty("professor")
	private Professor professor;

	@JsonProperty("list_id")
	private int listId;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("name")
	private String name;

	@JsonProperty("group_ids")
	private Object groupIds;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("id")
	private int id;

	@JsonProperty("type")
	private String type;

	@JsonProperty("prof_id")
	private String profId;

	@JsonProperty("status")
	private String status;
}