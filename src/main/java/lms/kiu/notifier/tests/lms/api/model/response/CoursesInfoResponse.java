package lms.kiu.notifier.tests.lms.api.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lms.kiu.notifier.tests.lms.api.model.response.subresponses.getCoursesInfo.DataItem;
import lombok.Data;

@Data
public class CoursesInfoResponse {

	@JsonProperty("result")
	private String result;

	@JsonProperty("data")
	private List<DataItem> data;
}