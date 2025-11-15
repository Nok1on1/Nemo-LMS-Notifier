package lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public class School{

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("rotationName")
	private Object rotationName;

	@JsonProperty("name")
	private String name;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("id")
	private int id;

	@JsonProperty("altName")
	private String altName;

	@JsonProperty("version")
	private String version;
}