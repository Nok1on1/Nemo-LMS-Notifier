package lms.kiu.notifier.tests.lms.api.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.assignment.DataItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponse{

	@JsonProperty("result")
	private String result;

	@JsonProperty("editor")
	private boolean editor;

	@JsonProperty("data")
	private List<DataItem> data;

	@JsonProperty("sumInTesting")
	private int sumInTesting;

	@JsonProperty("type")
	private String type;

	@JsonProperty("notifications")
	private int notifications;
}