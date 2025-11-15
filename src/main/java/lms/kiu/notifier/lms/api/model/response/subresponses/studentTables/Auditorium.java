package lms.kiu.notifier.lms.api.model.response.subresponses.studentTables;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Auditorium{

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("name")
	private String name;

	@JsonProperty("limit")
	private int limit;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("id")
	private int id;

	@JsonProperty("zoomLink")
	private Object zoomLink;

	@JsonProperty("status")
	private String status;
}