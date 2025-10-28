package lms.kiu.notifier.lms.nemo.lms.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer that handles fields that can be either: - A JSON array of strings: ["url1",
 * "url2"] - A single string: "url" - null
 * <p>
 * Always converts to List<String>
 */
public class FlexibleListDeserializer extends JsonDeserializer<List<String>> {

  @Override
  public List<String> deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {

    JsonNode node = parser.getCodec().readTree(parser);
    List<String> result = new ArrayList<>();

    if (node == null || node.isNull()) {
      return result; // Return empty list for null
    }

    if (node.isArray()) {
      // Handle array: ["url1", "url2", "url3"]
      for (JsonNode element : node) {
        if (!element.isNull()) {
          result.add(element.asText());
        }
      }
    } else if (node.isTextual()) {
      // Handle single string: "url"
      String text = node.asText();
      if (text != null && !text.isEmpty()) {
        result.add(text);
      }
    }

    return result;
  }
}