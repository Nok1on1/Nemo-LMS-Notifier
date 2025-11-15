package lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProgramViewItem{

	@JsonProperty("studentId")
	private int studentId;

	@JsonProperty("specId")
	private int specId;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("sumOfCredit")
	private int sumOfCredit;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("gpa")
	private Object gpa;

	@JsonProperty("id")
	private int id;

	@JsonProperty("credit")
	private int credit;

	@JsonProperty("programCredit")
	private int programCredit;

	@JsonProperty("eduId")
	private int eduId;

	@JsonProperty("sumOfAllCredits")
	private int sumOfAllCredits;
}