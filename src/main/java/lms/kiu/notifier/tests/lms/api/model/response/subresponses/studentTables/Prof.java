package lms.kiu.notifier.tests.lms.api.model.response.subresponses.studentTables;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Prof{

	@JsonProperty("nameEng")
	private String nameEng;

	@JsonProperty("isZoomLive")
	private int isZoomLive;

	@JsonProperty("name")
	private String name;

	@JsonProperty("jsonGuard")
	private String jsonGuard;

	@JsonProperty("zoomLink")
	private Object zoomLink;
}