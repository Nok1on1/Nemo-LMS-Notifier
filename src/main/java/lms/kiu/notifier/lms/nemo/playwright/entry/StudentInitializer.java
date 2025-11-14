package lms.kiu.notifier.lms.nemo.playwright.entry;

import static lms.kiu.notifier.lms.nemo.playwright.util.requestUtils.getQueryParams;
import static lms.kiu.notifier.lms.nemo.playwright.util.requestUtils.parseCourseRes;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Request;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lms.kiu.notifier.lms.nemo.mongo.model.Course;
import lms.kiu.notifier.lms.nemo.mongo.model.Student;
import lms.kiu.notifier.lms.nemo.mongo.service.CourseService;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import lms.kiu.notifier.lms.nemo.data.Constants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Handles student initialization by traversing LMS course structure via Playwright automation.
 * <p>
 * This class extends {@link InitPlaywright} to leverage browser automation capabilities for
 * extracting and persisting student course enrollments from the LMS system.
 * <p>
 * The initialization process performs a depth-first traversal of the LMS hierarchy, intercepting
 * API requests to capture course metadata and storing it in the database.
 *
 * @see InitPlaywright
 */
@Slf4j
@Getter
public class StudentInitializer extends InitPlaywright {

  Set<String> courseNames = new HashSet<>();

  public StudentInitializer(Student student,
      StudentService studentService,
      CourseService courseService) {
    super(student, studentService, courseService);
  }

  /**
   * Initializes student by traversing LMS course structure and extracting course data.
   * <p>
   * Performs depth-first traversal through the LMS hierarchy: LMS → Course → Subsection →
   * Interaction.
   * <p>
   * Navigation flow:
   * <ol>
   *   <li>Navigate to main LMS page</li>
   *   <li>Iterate through all available courses</li>
   *   <li>For each course, iterate through all subsections</li>
   *   <li>Intercept getCourse API requests to extract course metadata</li>
   *   <li>Store extracted course data in database</li>
   * </ol>
   * <p>
   * The method ensures proper cleanup of Playwright resources via the finally block,
   * even if errors occur during traversal.
   *
   * @return this StudentInitializer instance for method chaining
   * @throws RuntimeException if navigation fails or course extraction encounters errors, wrapping
   *                          the original exception with student context
   */
  public StudentInitializer initializeStudent() {
    // LMS -> Course -> Subsection -> Interaction
    // Depth First Traversal
    try {
      page.navigate(Constants.MAIN_URL);
      mainSteps.goToLms();

      lmsSteps.buildCourseIterator();

      Locator course;

      while (true) {
        course = lmsSteps.getNextCourse();

        if (course == null) {
          break;
        }

        course.click();

        courseSteps.buildSubSectionIterator();

        while (courseSteps.goToNextSubSection()) {
          page.onRequest(this::handleGetCourseRequest);

          page.goBack();

          if (courseSteps.hasNextSubSection()) {
            course.click();
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "Error fetching messages for Telegram ID: " + student.getTelegramId(), e);
    } finally {
      cleanup();
    }

    return this;
  }

  /**
   * Intercepts and processes getCourse API requests to extract and persist course data.
   * <p>
   * This method is registered as a request handler via
   * {@link com.microsoft.playwright.Page#onRequest}. When a getCourse request is detected:
   * <ol>
   *   <li>Extracts courseId and groupId from request parameters</li>
   *   <li>Parses course details from response body</li>
   *   <li>Checks if course already exists in database</li>
   *   <li>Saves new course if not present</li>
   *   <li>Associates course with the current student</li>
   *   <li>Adds course name to {@link #courseNames} set upon success</li>
   * </ol>
   * <p>
   * Note: Uses blocking {@code .block()} call as Playwright operates synchronously.
   *
   * @param req the intercepted Playwright request object
   */
  private void handleGetCourseRequest(Request req) {
    if (req.url().contains("getCourse")) {
      log.info("Playwright - Retrieving getCourse request Headers");

      Map<String, String> queryParams = getQueryParams(req.postData());

      int groupId = Integer.parseInt(queryParams.get("groupId"));
      int courseId = Integer.parseInt(queryParams.get("id"));

      Course parsedCourse = parseCourseRes(req.response().text(), courseId, groupId);

      try {
        Student updatedStudent = courseService.findCourseByCourseIdAndGroupId(courseId, groupId)
            .switchIfEmpty(
                req.response().ok() ? courseService.save(parsedCourse) : Mono.empty()
            )
            .flatMap(c -> studentService.addCourse(student.getTelegramId(), c))
            .block();

        if (updatedStudent != null) {
          courseNames.add(parsedCourse.getCourseName());
          log.info("Successfully added course: {}. Total: {}",
              parsedCourse.getCourseName(), courseNames.size());
        }

      } catch (Exception e) {
        log.error("Error processing course request for courseId: {}, groupId: {}",
            courseId, groupId, e);
      }
    }
  }
}
