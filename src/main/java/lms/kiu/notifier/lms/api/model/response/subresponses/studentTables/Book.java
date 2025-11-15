package lms.kiu.notifier.lms.api.model.response.subresponses.studentTables;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book{

	@JsonProperty("code")
	private Object code;

	@JsonProperty("hidden")
	private int hidden;

	@JsonProperty("langCode")
	private String langCode;

	@JsonProperty("description")
	private String description;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("syllabuses")
	private int syllabuses;

	@JsonProperty("type")
	private String type;

	@JsonProperty("testing_word_answer")
	private int testingWordAnswer;

	@JsonProperty("ruleCheck")
	private int ruleCheck;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("creditLimit")
	private Object creditLimit;

	@JsonProperty("id")
	private int id;

	@JsonProperty("credit")
	private int credit;

	@JsonProperty("lang")
	private Object lang;

	@JsonProperty("version")
	private Object version;

	@JsonProperty("deleted_at")
	private Object deletedAt;

	@JsonProperty("question_count")
	private int questionCount;

	@JsonProperty("componentCount")
	private int componentCount;

	@JsonProperty("conditionStatus")
	private int conditionStatus;

	@JsonProperty("schoolIds")
	private List<Integer> schoolIds;

	@JsonProperty("name")
	private String name;

	@JsonProperty("gpaLimit")
	private int gpaLimit;

	@JsonProperty("specialIds")
	private List<Integer> specialIds;

	@JsonProperty("ruleStudents")
	private String ruleStudents;

	@JsonProperty("status")
	private String status;
}