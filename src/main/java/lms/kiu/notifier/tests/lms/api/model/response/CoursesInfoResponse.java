package lms.kiu.notifier.tests.lms.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.getCoursesInfo.DataItem;
import lombok.Data;

@Data
public class CoursesInfoResponse {

	@JsonProperty("result")
	private String result;

	@JsonProperty("data")
	private List<DataItem> data;
}