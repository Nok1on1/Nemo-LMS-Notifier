package lms.kiu.notifier.tests.lms.api.model.response.subresponses.studentTables;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataItemItem{

	@JsonProperty("item")
	private Item item;

	@JsonProperty("data")
	private Data data;

	@JsonProperty("type")
	private String type;

	@JsonProperty("prof")
	private Prof prof;
}