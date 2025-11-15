package lms.kiu.notifier.lms.api.model.response.subresponses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Special{

	@JsonProperty("restricted_minors")
	private Object restrictedMinors;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("hidden")
	private String hidden;

	@JsonProperty("rotationName")
	private Object rotationName;

	@JsonProperty("schoolId")
	private int schoolId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("maxSemester")
	private int maxSemester;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("id")
	private int id;

	@JsonProperty("altName")
	private String altName;

	@JsonProperty("version")
	private String version;
}