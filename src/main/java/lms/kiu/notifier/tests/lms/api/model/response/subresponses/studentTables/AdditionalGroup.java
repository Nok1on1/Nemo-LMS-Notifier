package lms.kiu.notifier.tests.lms.api.model.response.subresponses.studentTables;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdditionalGroup{

	@JsonProperty("id")
	private int id;

	@JsonProperty("parentId")
	private int parentId;
}