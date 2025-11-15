package lms.kiu.notifier.lms.api.model.response.subresponses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Student{

	@JsonProperty("registrationTime")
	private Object registrationTime;

	@JsonProperty("country")
	private String country;

	@JsonProperty("adminAuthUserId")
	private int adminAuthUserId;

	@JsonProperty("mail")
	private String mail;

	@JsonProperty("phoneSecond")
	private String phoneSecond;

	@JsonProperty("unblockGroupsValidation")
	private int unblockGroupsValidation;

	@JsonProperty("api_token")
	private Object apiToken;

	@JsonProperty("ban")
	private String ban;

	@JsonProperty("isRehabilitant")
	private int isRehabilitant;

	@JsonProperty("country2")
	private Object country2;

	@JsonProperty("isAdminAuth")
	private int isAdminAuth;

	@JsonProperty("militaryPlace_region")
	private int militaryPlaceRegion;

	@JsonProperty("password")
	private Object password;

	@JsonProperty("address_region")
	private Object addressRegion;

	@JsonProperty("tmp")
	private String tmp;

	@JsonProperty("defaultCurrency")
	private int defaultCurrency;

	@JsonProperty("questionnaireStatus")
	private Object questionnaireStatus;

	@JsonProperty("mailPersonal")
	private String mailPersonal;

	@JsonProperty("dualCitizen")
	private int dualCitizen;

	@JsonProperty("id")
	private int id;

	@JsonProperty("tag")
	private String tag;

	@JsonProperty("emergencyContact")
	private String emergencyContact;

	@JsonProperty("ipAddress")
	private Object ipAddress;

	@JsonProperty("temporaryCode")
	private Object temporaryCode;

	@JsonProperty("personalNumber")
	private String personalNumber;

	@JsonProperty("parentPhoneNumber")
	private Object parentPhoneNumber;

	@JsonProperty("lastNameEng")
	private String lastNameEng;

	@JsonProperty("version")
	private Object version;

	@JsonProperty("banReason")
	private Object banReason;

	@JsonProperty("fireToken")
	private Object fireToken;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("parentFirstName")
	private Object parentFirstName;

	@JsonProperty("emergencyContactPhone")
	private String emergencyContactPhone;

	@JsonProperty("address_municipality")
	private Object addressMunicipality;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("restrictFree")
	private int restrictFree;

	@JsonProperty("questionnaireComment")
	private Object questionnaireComment;

	@JsonProperty("registerStatus")
	private String registerStatus;

	@JsonProperty("examScore")
	private Object examScore;

	@JsonProperty("addressJuridical_region")
	private Object addressJuridicalRegion;

	@JsonProperty("skipFinance")
	private int skipFinance;

	@JsonProperty("endTest")
	private int endTest;

	@JsonProperty("birthday")
	private String birthday;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("agentId")
	private Object agentId;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("needTestingAccess")
	private int needTestingAccess;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("parentPersonalNumber")
	private Object parentPersonalNumber;

	@JsonProperty("militaryPlace")
	private String militaryPlace;

	@JsonProperty("moreInfo")
	private Object moreInfo;

	@JsonProperty("addressJuridical")
	private String addressJuridical;

	@JsonProperty("militaryPlace_municipality")
	private int militaryPlaceMunicipality;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("cacheKey")
	private String cacheKey;

	@JsonProperty("group_for_registration")
	private Object groupForRegistration;

	@JsonProperty("lang")
	private String lang;

	@JsonProperty("hasSeenScholarship")
	private Object hasSeenScholarship;

	@JsonProperty("wtoStepAuth")
	private int wtoStepAuth;

	@JsonProperty("mailPassword")
	private String mailPassword;

	@JsonProperty("educationalInstitution_municipality")
	private int educationalInstitutionMunicipality;

	@JsonProperty("medicMaxCredit")
	private String medicMaxCredit;

	@JsonProperty("address")
	private String address;

	@JsonProperty("parentLastName")
	private Object parentLastName;

	@JsonProperty("financeVerified")
	private int financeVerified;

	@JsonProperty("isSpecialNeeds")
	private Object isSpecialNeeds;

	@JsonProperty("syncKey")
	private Object syncKey;

	@JsonProperty("verified")
	private int verified;

	@JsonProperty("firstNameEng")
	private String firstNameEng;

	@JsonProperty("loginIpAddress")
	private Object loginIpAddress;

	@JsonProperty("examNumber")
	private Object examNumber;

	@JsonProperty("addressJuridical_municipality")
	private Object addressJuridicalMunicipality;

	@JsonProperty("photo_code")
	private String photoCode;

	@JsonProperty("commandVerified")
	private int commandVerified;

	@JsonProperty("educationalInstitution")
	private int educationalInstitution;

	@JsonProperty("personalCardNum")
	private String personalCardNum;

	@JsonProperty("educationalInstitution_region")
	private int educationalInstitutionRegion;

	@JsonProperty("regionId")
	private Object regionId;

	@JsonProperty("refugee")
	private int refugee;

	@JsonProperty("comment")
	private Object comment;

	@JsonProperty("username")
	private Object username;

	@JsonProperty("isPersonWithLearningDisabilities")
	private Object isPersonWithLearningDisabilities;
}