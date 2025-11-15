package lms.kiu.notifier.tests.lms.api.model.response.subresponses.getCoursesInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemsItem {

  @JsonProperty("listId")
  private int listId;

  @JsonProperty("profName")
  private String profName;

  @JsonProperty("groupName")
  private String groupName;

  @JsonProperty("curationFromDate")
  private String curationFromDate;

  @JsonProperty("curation")
  private int curation;

  @JsonProperty("name")
  private String name;

  @JsonProperty("courseGroupId")
  private int courseGroupId;

  @JsonProperty("altName")
  private String altName;

  @JsonProperty("type")
  private String type;

  @JsonProperty("notifications")
  private int notifications;
}