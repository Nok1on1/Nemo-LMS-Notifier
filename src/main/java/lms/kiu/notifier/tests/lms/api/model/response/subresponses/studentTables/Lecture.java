package lms.kiu.notifier.tests.lms.api.model.response.subresponses.studentTables;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lecture{

	@JsonProperty("curationFromDate")
	private String curationFromDate;

	@JsonProperty("endDate")
	private String endDate;

	@JsonProperty("groupId")
	private int groupId;

	@JsonProperty("type")
	private String type;

	@JsonProperty("space")
	private int space;

	@JsonProperty("listId")
	private int listId;

	@JsonProperty("nashromi_praqtika_status")
	private String nashromiPraqtikaStatus;

	@JsonProperty("limit")
	private int limit;

	@JsonProperty("id")
	private int id;

	@JsonProperty("day")
	private int day;

	@JsonProperty("attached_lectures")
	private Object attachedLectures;

	@JsonProperty("childGroupDate")
	private String childGroupDate;

	@JsonProperty("visibility")
	private int visibility;

	@JsonProperty("calendarExceptType")
	private String calendarExceptType;

	@JsonProperty("ipAddress")
	private long ipAddress;

	@JsonProperty("registration_book_group_calendar")
	private Object registrationBookGroupCalendar;

	@JsonProperty("curationDuration")
	private String curationDuration;

	@JsonProperty("registration_book_group_remove_calendar")
	private Object registrationBookGroupRemoveCalendar;

	@JsonProperty("additional_group")
	private AdditionalGroup additionalGroup;

	@JsonProperty("profName")
	private String profName;

	@JsonProperty("groupName")
	private Object groupName;

	@JsonProperty("groupComment")
	private String groupComment;

	@JsonProperty("lectureSeminar")
	private int lectureSeminar;

	@JsonProperty("curationToDate")
	private String curationToDate;

	@JsonProperty("startDate")
	private String startDate;

	@JsonProperty("status")
	private int status;

	@JsonProperty("auditorium")
	private Auditorium auditorium;

	@JsonProperty("tagId")
	private int tagId;

	@JsonProperty("sameProf")
	private int sameProf;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("registration_book_group_except_calendar")
	private Object registrationBookGroupExceptCalendar;

	@JsonProperty("profId")
	private String profId;

	@JsonProperty("duration")
	private int duration;

	@JsonProperty("userLevel")
	private String userLevel;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("attached_seminars")
	private List<Object> attachedSeminars;

	@JsonProperty("curation")
	private int curation;

	@JsonProperty("auditories")
	private int auditories;

	@JsonProperty("h")
	private int h;

	@JsonProperty("isPilot")
	private int isPilot;

	@JsonProperty("withoutList")
	private int withoutList;

	@JsonProperty("minLimit")
	private int minLimit;

	@JsonProperty("groups_for_registration")
	private List<Object> groupsForRegistration;

	@JsonProperty("registration_book_group_except_calendars")
	private List<Object> registrationBookGroupExceptCalendars;

	@JsonProperty("m")
	private int m;

	@JsonProperty("userId")
	private int userId;

	@JsonProperty("parentId")
	private int parentId;

	@JsonProperty("bookId")
	private int bookId;

	@JsonProperty("tema")
	private Object tema;

	@JsonProperty("additionalType")
	private String additionalType;

	@JsonProperty("limitVisibility")
	private int limitVisibility;

	@JsonProperty("comment")
	private Object comment;

	@JsonProperty("student_tags")
	private List<Object> studentTags;

	@JsonProperty("additional_groups")
	private List<Object> additionalGroups;
}