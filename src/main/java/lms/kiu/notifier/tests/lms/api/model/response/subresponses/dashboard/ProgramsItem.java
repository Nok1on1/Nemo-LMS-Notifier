package lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProgramsItem{

	@JsonProperty("year")
	private String year;

	@JsonProperty("level")
	private String level;

	@JsonProperty("description")
	private Object description;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("type")
	private int type;

	@JsonProperty("specialId")
	private int specialId;

	@JsonProperty("eduId")
	private int eduId;

	@JsonProperty("nameEng")
	private String nameEng;

	@JsonProperty("view")
	private int view;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("schoolId")
	private int schoolId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private int id;

	@JsonProperty("status")
	private String status;
}