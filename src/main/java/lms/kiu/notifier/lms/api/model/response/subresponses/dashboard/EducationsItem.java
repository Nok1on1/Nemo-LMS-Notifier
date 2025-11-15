package lms.kiu.notifier.lms.api.model.response.subresponses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EducationsItem{

	@JsonProperty("student_id_key")
	private Object studentIdKey;

	@JsonProperty("concentrationGroup")
	private Object concentrationGroup;

	@JsonProperty("learningYear")
	private String learningYear;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("choose")
	private Object choose;

	@JsonProperty("studentId")
	private int studentId;

	@JsonProperty("medalist")
	private String medalist;

	@JsonProperty("learningFile")
	private String learningFile;

	@JsonProperty("score")
	private Object score;

	@JsonProperty("personalFile")
	private String personalFile;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("school")
	private School school;

	@JsonProperty("schoolId")
	private int schoolId;

	@JsonProperty("minorProgramId")
	private int minorProgramId;

	@JsonProperty("id")
	private int id;

	@JsonProperty("lang")
	private Object lang;

	@JsonProperty("concentrationModule")
	private Object concentrationModule;

	@JsonProperty("licenseFile")
	private String licenseFile;

	@JsonProperty("specId")
	private int specId;

	@JsonProperty("student_id_key2")
	private Object studentIdKey2;

	@JsonProperty("method")
	private int method;

	@JsonProperty("commandStopFile")
	private String commandStopFile;

	@JsonProperty("level")
	private int level;

	@JsonProperty("erasmus")
	private int erasmus;

	@JsonProperty("syncKey")
	private int syncKey;

	@JsonProperty("mobileYear")
	private Object mobileYear;

	@JsonProperty("photo")
	private String photo;

	@JsonProperty("learningSeason")
	private Object learningSeason;

	@JsonProperty("special")
	private Special special;

	@JsonProperty("additionalStatus")
	private Object additionalStatus;

	@JsonProperty("langLevel")
	private Object langLevel;

	@JsonProperty("langChange")
	private Object langChange;

	@JsonProperty("commandStartFile")
	private String commandStartFile;

	@JsonProperty("semester")
	private int semester;

	@JsonProperty("concentrationLangGroup")
	private Object concentrationLangGroup;

	@JsonProperty("militaryFile")
	private String militaryFile;

	@JsonProperty("grant")
	private Object grant;

	@JsonProperty("programId")
	private int programId;

	@JsonProperty("status")
	private String status;

	@JsonProperty("specialChange")
	private Object specialChange;
}