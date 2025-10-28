package lms.kiu.notifier.lms.nemo.playwright.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lms.kiu.notifier.lms.nemo.mongo.model.Course;

public class requestUtils {


  public static Map<String, String> getQueryParams(String jsonString) {
    Map<String, String> params = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    try {
      JsonNode jsonNode = mapper.readTree(jsonString);

      if (jsonNode.has("groupId")) {
        params.put("groupId", jsonNode.get("groupId").asText());
      }
      if (jsonNode.has("id")) {
        params.put("id", jsonNode.get("id").asText());
      }

    } catch (Exception e) {
      throw new RuntimeException("Failed to parse JSON: " + jsonString, e);
    }
    return params;
  }

  public static Course parseCourseRes(String res, int courseId, int groupId) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root;
    try {
      root = mapper.readTree(res);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    JsonNode dataNode = root.get("data");
    String courseName = dataNode.get("name").asText().trim();

    return Course.builder()
        .groupId(groupId)
        .courseId(courseId)
        .courseName(courseName).build();
  }

}
