package lms.kiu.notifier.lms.nemo.playwright.model;


import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoursePosts {

  String courseName;
  List<Post> posts;

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("course name: " + courseName + "\n");
    for (Post post : posts) {
      builder.append(post.toString()).append("\n");
    }
    return builder.toString();
  }
}
