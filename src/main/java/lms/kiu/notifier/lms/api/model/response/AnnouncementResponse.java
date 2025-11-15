package lms.kiu.notifier.lms.api.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lms.kiu.notifier.lms.api.model.response.subresponses.announcemet.DataItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementResponse{

	@JsonProperty("result")
	private String result;

	@JsonProperty("editor")
	private boolean editor;

	@JsonProperty("data")
	private List<DataItem> data;
}