package lms.kiu.notifier.lms.api.model.response.subresponses.getCoursesInfo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataItem{

	@JsonProperty("listId")
	private int listId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("altName")
	private String altName;

	@JsonProperty("items")
	private List<ItemsItem> items;

	@JsonProperty("notifications")
	private int notifications;
}