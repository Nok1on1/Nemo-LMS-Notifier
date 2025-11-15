package lms.kiu.notifier.lms.api.model.response.subresponses.registrationGroupId;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ListItem{

	@JsonProperty("virtualGroupCount")
	private Object virtualGroupCount;

	@JsonProperty("year")
	private int year;

	@JsonProperty("studentCanCancel")
	private int studentCanCancel;

	@JsonProperty("show")
	private int show;

	@JsonProperty("semesterCredit")
	private Object semesterCredit;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("canCancelContentration")
	private int canCancelContentration;

	@JsonProperty("canCancelMinor")
	private int canCancelMinor;

	@JsonProperty("nashromi_registration")
	private int nashromiRegistration;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("limit")
	private Object limit;

	@JsonProperty("freshman_reg")
	private int freshmanReg;

	@JsonProperty("season")
	private String season;

	@JsonProperty("concentrationType")
	private String concentrationType;

	@JsonProperty("id")
	private int id;

	@JsonProperty("seasonYear")
	private String seasonYear;

	@JsonProperty("semesterCreditIsLimited")
	private Object semesterCreditIsLimited;

	@JsonProperty("timeDemand")
	private int timeDemand;

	@JsonProperty("cache")
	private int cache;

	@JsonProperty("visible")
	private int visible;

	@JsonProperty("nashromi_cancel")
	private int nashromiCancel;

	@JsonProperty("canRegister")
	private int canRegister;

	@JsonProperty("canRegisterContentration")
	private int canRegisterContentration;

	@JsonProperty("semesterLimit")
	private Object semesterLimit;

	@JsonProperty("praktika_registration")
	private int praktikaRegistration;

	@JsonProperty("yearCreditLimit")
	private Object yearCreditLimit;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("exchange")
	private int exchange;

	@JsonProperty("programs")
	private List<Object> programs;

	@JsonProperty("yearCreditIsLimited")
	private Object yearCreditIsLimited;

	@JsonProperty("praktika_cancel")
	private int praktikaCancel;

	@JsonProperty("finalRegistrationSuccess")
	private int finalRegistrationSuccess;

	@JsonProperty("canRegisterMinor")
	private int canRegisterMinor;

	@JsonProperty("status")
	private int status;
}