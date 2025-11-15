package lms.kiu.notifier.lms.api.model.response.subresponses.studentTables;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item{

	@JsonProperty("adminAuthUserId")
	private Object adminAuthUserId;

	@JsonProperty("orderId")
	private int orderId;

	@JsonProperty("seminar")
	private Seminar seminar;

	@JsonProperty("groupId")
	private int groupId;

	@JsonProperty("book")
	private Book book;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("type")
	private String type;

	@JsonProperty("eduId")
	private int eduId;

	@JsonProperty("isAdminAuth")
	private int isAdminAuth;

	@JsonProperty("studentId")
	private int studentId;

	@JsonProperty("listId")
	private int listId;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("lecture")
	private Lecture lecture;

	@JsonProperty("id")
	private int id;

	@JsonProperty("startedTime")
	private Object startedTime;

	@JsonProperty("minorGroupId")
	private int minorGroupId;

	@JsonProperty("seminarId")
	private int seminarId;

	@JsonProperty("ipAddress")
	private long ipAddress;

	@JsonProperty("listGroupId")
	private int listGroupId;

	@JsonProperty("bookId")
	private int bookId;

	@JsonProperty("lectureId")
	private int lectureId;

	@JsonProperty("canceled")
	private int canceled;

	@JsonProperty("registrationGroupId")
	private int registrationGroupId;

	@JsonProperty("buyStatus")
	private int buyStatus;

	@JsonProperty("endTime")
	private Object endTime;
}