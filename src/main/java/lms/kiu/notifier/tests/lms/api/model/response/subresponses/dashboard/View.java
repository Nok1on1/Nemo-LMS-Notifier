package lms.kiu.notifier.tests.lms.api.model.response.subresponses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class View{

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("eduId")
	private int eduId;

	@JsonProperty("specialId")
	private int specialId;

	@JsonProperty("averageScore")
	private int averageScore;

	@JsonProperty("financeGraphPermanentlyAutumn")
	private String financeGraphPermanentlyAutumn;

	@JsonProperty("financeGraphPermanentlyReSpring")
	private String financeGraphPermanentlyReSpring;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("bankTransactPerm")
	private int bankTransactPerm;

	@JsonProperty("financeGraphKeySpring")
	private Object financeGraphKeySpring;

	@JsonProperty("defaultCurrency")
	private int defaultCurrency;

	@JsonProperty("schoolId")
	private int schoolId;

	@JsonProperty("specialName")
	private String specialName;

	@JsonProperty("gpa")
	private Object gpa;

	@JsonProperty("minorProgramId")
	private int minorProgramId;

	@JsonProperty("id")
	private int id;

	@JsonProperty("schoolName")
	private String schoolName;

	@JsonProperty("credit")
	private int credit;

	@JsonProperty("programCredit")
	private int programCredit;

	@JsonProperty("averageResult")
	private String averageResult;

	@JsonProperty("financeGraphTypeSpring")
	private String financeGraphTypeSpring;

	@JsonProperty("financeGraphKeyReSpring")
	private Object financeGraphKeyReSpring;

	@JsonProperty("financeVerified")
	private int financeVerified;

	@JsonProperty("showInfoDialog")
	private int showInfoDialog;

	@JsonProperty("financeGraphTypeAutumn")
	private String financeGraphTypeAutumn;

	@JsonProperty("personalNumber")
	private String personalNumber;

	@JsonProperty("financeGraphPermanentlySpring")
	private String financeGraphPermanentlySpring;

	@JsonProperty("financeGraphId")
	private Object financeGraphId;

	@JsonProperty("allowBuyCredit")
	private int allowBuyCredit;

	@JsonProperty("special")
	private Special special;

	@JsonProperty("name")
	private String name;

	@JsonProperty("statementId")
	private Object statementId;

	@JsonProperty("semester")
	private int semester;

	@JsonProperty("financeGraphKeyAutumn")
	private Object financeGraphKeyAutumn;

	@JsonProperty("programId")
	private int programId;

	@JsonProperty("status")
	private String status;

	@JsonProperty("financeGraphTypeReSpring")
	private String financeGraphTypeReSpring;
}