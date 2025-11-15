package lms.kiu.notifier.tests.lms.api.model.response.subresponses.announcemet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professor{

	@JsonProperty("birthday")
	private String birthday;

	@JsonProperty("end_date")
	private Object endDate;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("isZoomLive")
	private int isZoomLive;

	@JsonProperty("mail")
	private String mail;

	@JsonProperty("data")
	private Object data;

	@JsonProperty("color")
	private Object color;

	@JsonProperty("tanamdeboba")
	private Object tanamdeboba;

	@JsonProperty("spec")
	private Object spec;

	@JsonProperty("assessment")
	private String assessment;

	@JsonProperty("password")
	private String password;

	@JsonProperty("old_password")
	private Object oldPassword;

	@JsonProperty("sqesi")
	private String sqesi;

	@JsonProperty("load")
	private Object load;

	@JsonProperty("school")
	private Object school;

	@JsonProperty("tmp")
	private Object tmp;

	@JsonProperty("tel")
	private String tel;

	@JsonProperty("id")
	private int id;

	@JsonProperty("zoomLink")
	private Object zoomLink;

	@JsonProperty("moqalaqeoba")
	private String moqalaqeoba;

	@JsonProperty("email")
	private Object email;

	@JsonProperty("start_date")
	private Object startDate;

	@JsonProperty("mailPassword")
	private Object mailPassword;

	@JsonProperty("image")
	private String image;

	@JsonProperty("address")
	private String address;

	@JsonProperty("read")
	private Object read;

	@JsonProperty("zoomOnline")
	private Object zoomOnline;

	@JsonProperty("pi_num1")
	private String piNum1;

	@JsonProperty("el_learning_link")
	private Object elLearningLink;

	@JsonProperty("pi_num2")
	private String piNum2;

	@JsonProperty("citizenship")
	private Object citizenship;

	@JsonProperty("firstNameEng")
	private String firstNameEng;

	@JsonProperty("temporaryCode")
	private Object temporaryCode;

	@JsonProperty("lastNameEng")
	private String lastNameEng;

	@JsonProperty("photo_code")
	private Object photoCode;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("nameEng")
	private String nameEng;

	@JsonProperty("zoomId")
	private Object zoomId;

	@JsonProperty("upload_silabus")
	private String uploadSilabus;

	@JsonProperty("name")
	private String name;

	@JsonProperty("jsonGuard")
	private String jsonGuard;

	@JsonProperty("online")
	private Object online;

	@JsonProperty("tanamdeboba2")
	private Object tanamdeboba2;

	@JsonProperty("username")
	private String username;

	@JsonProperty("status")
	private Object status;

	@JsonProperty("name_en")
	private Object nameEn;
}