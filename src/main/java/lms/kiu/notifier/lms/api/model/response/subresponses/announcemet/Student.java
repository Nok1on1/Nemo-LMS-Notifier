package lms.kiu.notifier.lms.api.model.response.subresponses.announcemet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student{

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("firstNameEng")
	private String firstNameEng;

	@JsonProperty("id")
	private int id;

	@JsonProperty("lastNameEng")
	private String lastNameEng;

	@JsonProperty("photo_code")
	private String photoCode;
}