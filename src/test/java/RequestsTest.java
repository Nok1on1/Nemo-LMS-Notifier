import java.net.URI;
import lms.kiu.notifier.data.Constants;
import lms.kiu.notifier.lms.api.model.response.StudentTablesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

public class RequestsTest {

  static String bearerToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzE1MjEsInVzZXJuYW1lIjpudWxsLCJuYW1lIjoiXHUxMGQyXHUxMGQ4XHUxMGRkXHUxMGUwXHUxMGQyXHUxMGQ4IFx1MTBkYlx1MTBkOFx1MTBlNVx1MTBkMFx1MTBkMVx1MTBkNFx1MTBlMFx1MTBkOFx1MTBlYlx1MTBkNCIsIm1haWwiOiJNaWthYmVyaWR6ZS5HaW9yZ2lAa2l1LmVkdS5nZSIsImd1YXJkIjoic3R1ZGVudCIsInBob3RvX2NvZGUiOiJcL2ZpbGVzXC9pbWFnZXNcL2MzNTM1MTc5NzFkMjkwYTE3YmI2Y2ZhZmQ2ZTJjZjgwLmpwZyIsImZpcnN0TmFtZSI6Ilx1MTBkMlx1MTBkOFx1MTBkZFx1MTBlMFx1MTBkMlx1MTBkOCIsImxhc3ROYW1lIjoiXHUxMGRiXHUxMGQ4XHUxMGU1XHUxMGQwXHUxMGQxXHUxMGQ0XHUxMGUwXHUxMGQ4XHUxMGViXHUxMGQ0IiwiZmlyc3ROYW1lRW5nIjoiR0lPUkdJIiwibGFzdE5hbWVFbmciOiJNSUtBQkVSSURaRSIsInBlcnNvbmFsTnVtYmVyIjoiMTg3MDEwNzU3NDciLCJiaXJ0aGRheSI6IjIwMDUtMTEtMjMiLCJnZW5kZXIiOiJtYWxlIiwicmVnaW9uSWQiOm51bGwsImFkZHJlc3MiOiJcdTEwZDZcdTEwZDRcdTEwZTFcdTEwZTJcdTEwZDBcdTEwZTRcdTEwZGRcdTEwZGNcdTEwZDggXHUxMGQ5XHUxMGQ0XHUxMGQ5XHUxMGQ0XHUxMGRhXHUxMGQ4XHUxMGViXHUxMGQ4XHUxMGUxIFx1MTBlNVx1MTBlM1x1MTBlOVx1MTBkMCBOMTMgXHUxMGQxXHUxMGQ4XHUxMGRjXHUxMGQwIDI2IiwicGhvbmUiOiI1NTg1NzU3NzYiLCJjb21tZW50IjpudWxsLCJjb3VudHJ5IjoiXHUxMGUxXHUxMGQwXHUxMGU1XHUxMGQwXHUxMGUwXHUxMGQ3XHUxMGQ1XHUxMGQ0XHUxMGRhXHUxMGRkIiwiaXNTcGVjaWFsTmVlZHMiOm51bGwsImlzUGVyc29uV2l0aExlYXJuaW5nRGlzYWJpbGl0aWVzIjpudWxsLCJ3dG9TdGVwQXV0aCI6MCwidmlldyI6eyJpZCI6MzE1MjEsIm5hbWUiOiJcdTEwZGJcdTEwZDhcdTEwZTVcdTEwZDBcdTEwZDFcdTEwZDRcdTEwZTBcdTEwZDhcdTEwZWJcdTEwZDQgXHUxMGQyXHUxMGQ4XHUxMGRkXHUxMGUwXHUxMGQyXHUxMGQ4IiwicGVyc29uYWxOdW1iZXIiOiIxODcwMTA3NTc0NyIsImVkdUlkIjoyMTk3NjI3LCJzY2hvb2xJZCI6MjUsInNjaG9vbE5hbWUiOiJcdTEwZDlcdTEwZGRcdTEwZGJcdTEwZGVcdTEwZDhcdTEwZTNcdTEwZTJcdTEwZDRcdTEwZTBcdTEwZTNcdTEwZGFcdTEwZDggXHUxMGRiXHUxMGQ0XHUxMGVhXHUxMGRjXHUxMGQ4XHUxMGQ0XHUxMGUwXHUxMGQ0XHUxMGQxXHUxMGQ4XHUxMGUxIFx1MTBlMVx1MTBkOVx1MTBkZFx1MTBkYVx1MTBkMCIsInNwZWNpYWxJZCI6NzEsInNwZWNpYWxOYW1lIjoiXHUxMGQ5XHUxMGRkXHUxMGRiXHUxMGRlXHUxMGQ4XHUxMGUzXHUxMGUyXHUxMGQ0XHUxMGUwXHUxMGUzXHUxMGRhXHUxMGQ4IFx1MTBkYlx1MTBkNFx1MTBlYVx1MTBkY1x1MTBkOFx1MTBkNFx1MTBlMFx1MTBkNFx1MTBkMVx1MTBkMCAyMDI1IiwicHJvZ3JhbUlkIjo0Nywic2VtZXN0ZXIiOjUsInN0YXR1cyI6ImFjdGl2ZSIsImdwYSI6MS45NCwiY3JlZGl0IjoxMjAsInByb2dyYW1DcmVkaXQiOjEyMCwiYXZlcmFnZVJlc3VsdCI6IkMiLCJhdmVyYWdlU2NvcmUiOjczLCJmaW5hbmNlR3JhcGhJZCI6bnVsbCwic3RhdGVtZW50SWQiOm51bGwsImJhbmtUcmFuc2FjdFBlcm0iOjAsImZpbmFuY2VHcmFwaFR5cGVBdXR1bW4iOiJhdXRvIiwiZmluYW5jZUdyYXBoUGVybWFuZW50bHlBdXR1bW4iOiJubyIsImZpbmFuY2VHcmFwaEtleUF1dHVtbiI6bnVsbCwiZmluYW5jZUdyYXBoVHlwZVNwcmluZyI6ImF1dG8iLCJmaW5hbmNlR3JhcGhQZXJtYW5lbnRseVNwcmluZyI6Im5vIiwiZmluYW5jZUdyYXBoS2V5U3ByaW5nIjpudWxsLCJmaW5hbmNlR3JhcGhUeXBlUmVTcHJpbmciOiJhdXRvIiwiZmluYW5jZUdyYXBoUGVybWFuZW50bHlSZVNwcmluZyI6Im5vIiwiZmluYW5jZUdyYXBoS2V5UmVTcHJpbmciOm51bGwsImNyZWF0ZWRfYXQiOiIyMDI0LTA0LTExIDIxOjA2OjUxIiwidXBkYXRlZF9hdCI6IjIwMjUtMDktMTAgMTc6MDc6NTkiLCJhbGxvd0J1eUNyZWRpdCI6MCwiZGVmYXVsdEN1cnJlbmN5IjoxLCJmaW5hbmNlVmVyaWZpZWQiOjAsInNob3dJbmZvRGlhbG9nIjowLCJtaW5vclByb2dyYW1JZCI6NDYsImFsdFNwZWNpYWxOYW1lIjoiQ29tcGl1dGVyIFNjaWVuY2UgLTIwMjUiLCJhbHRTY2hvb2xOYW1lIjoiXHUxMGQ5XHUxMGRkXHUxMGRiXHUxMGRlXHUxMGQ4XHUxMGUzXHUxMGUyXHUxMGQ0XHUxMGUwXHUxMGUzXHUxMGRhXHUxMGQ4IFx1MTBkYlx1MTBkNFx1MTBlYVx1MTBkY1x1MTBkOFx1MTBkNFx1MTBlMFx1MTBkNFx1MTBkMVx1MTBkOFx1MTBlMSBcdTEwZTFcdTEwZDlcdTEwZGRcdTEwZGFcdTEwZDAiLCJwcm9ncmFtTmFtZSI6Ilx1MTBkOVx1MTBkZFx1MTBkYlx1MTBkZVx1MTBkOFx1MTBlM1x1MTBlMlx1MTBkNFx1MTBlMFx1MTBlM1x1MTBkYVx1MTBkOCBcdTEwZGJcdTEwZDRcdTEwZWFcdTEwZGNcdTEwZDhcdTEwZDRcdTEwZTBcdTEwZDRcdTEwZDFcdTEwZDAifSwicHJvZ3JhbXMiOlt7ImlkIjo0NywibmFtZSI6Ilx1MTBkOVx1MTBkZFx1MTBkYlx1MTBkZVx1MTBkOFx1MTBlM1x1MTBlMlx1MTBkNFx1MTBlMFx1MTBlM1x1MTBkYVx1MTBkOCBcdTEwZGJcdTEwZDRcdTEwZWFcdTEwZGNcdTEwZDhcdTEwZDRcdTEwZTBcdTEwZDRcdTEwZDFcdTEwZDAiLCJuYW1lRW5nIjoiQ29tcHV0ZXIgU2NpZW5jZSIsInllYXIiOiIyMDI1Iiwic2Nob29sSWQiOjI1LCJzcGVjaWFsSWQiOjcxLCJ0eXBlIjoxLCJsZXZlbCI6ImJhayIsInN0YXR1cyI6ImFjdGl2ZSIsImRlc2NyaXB0aW9uIjpudWxsLCJjcmVhdGVkX2F0IjoiMjAyNS0wMy0xMSAxMDoyMToxOCIsInVwZGF0ZWRfYXQiOiIyMDI1LTAzLTExIDEwOjIxOjE4IiwidmlldyI6MSwiZWR1SWQiOjIxOTc2Mjd9XSwiY3VycmVudFByb2dyYW0iOnsiaWQiOjQ3LCJuYW1lIjoiXHUxMGQ5XHUxMGRkXHUxMGRiXHUxMGRlXHUxMGQ4XHUxMGUzXHUxMGUyXHUxMGQ0XHUxMGUwXHUxMGUzXHUxMGRhXHUxMGQ4IFx1MTBkYlx1MTBkNFx1MTBlYVx1MTBkY1x1MTBkOFx1MTBkNFx1MTBlMFx1MTBkNFx1MTBkMVx1MTBkMCIsIm5hbWVFbmciOiJDb21wdXRlciBTY2llbmNlIiwieWVhciI6IjIwMjUiLCJzY2hvb2xJZCI6MjUsInNwZWNpYWxJZCI6NzEsInR5cGUiOjEsImxldmVsIjoiYmFrIiwic3RhdHVzIjoiYWN0aXZlIiwiZGVzY3JpcHRpb24iOm51bGwsImNyZWF0ZWRfYXQiOiIyMDI1LTAzLTExIDEwOjIxOjE4IiwidXBkYXRlZF9hdCI6IjIwMjUtMDMtMTEgMTA6MjE6MTgiLCJ2aWV3IjoxLCJlZHVJZCI6MjE5NzYyN30sInBhc3N3b3JkQ2hhbmdlUmVxdWVzdCI6ZmFsc2UsInZlcmlmaWVkIjowLCJ1bmJsb2NrR3JvdXBzVmFsaWRhdGlvbiI6MCwibmVlZFRlc3RpbmdBY2Nlc3MiOjAsImlzQWRtaW5BdXRoIjowLCJhZG1pbkF1dGhVc2VySWQiOjUwMiwibWlub3JQcm9ncmFtSWQiOjQ2fQ.B38dau1fkZDjt15b4f3YyI9fKkXQKLGDjLB2gtS38-w";
  static String body = "{\n"
      + "  \"studentId\": 31521,\n"
      + "  \"eduId\": 2197627,\n"
      + "  \"groupId\": 17,\n"
      + "  \"dates\": [\n"
      + "    {\n"
      + "      \"d\": 1,\n"
      + "      \"day\": \"\\u10dd\\u10e0\\u10e8\\u10d0\\u10d1\\u10d0\\u10d7\\u10d8\",\n"
      + "      \"number\": \"10\",\n"
      + "      \"date\": \"2025-11-10\",\n"
      + "      \"month\": \"November\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"d\": 2,\n"
      + "      \"day\": \"\\u10e1\\u10d0\\u10db\\u10e8\\u10d0\\u10d1\\u10d0\\u10d7\\u10d8\",\n"
      + "      \"number\": \"11\",\n"
      + "      \"date\": \"2025-11-11\",\n"
      + "      \"month\": \"November\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"d\": 3,\n"
      + "      \"day\": \"\\u10dd\\u10d7\\u10ee\\u10e8\\u10d0\\u10d1\\u10d0\\u10d7\\u10d8\",\n"
      + "      \"number\": \"12\",\n"
      + "      \"date\": \"2025-11-12\",\n"
      + "      \"month\": \"November\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"d\": 4,\n"
      + "      \"day\": \"\\u10ee\\u10e3\\u10d7\\u10e8\\u10d0\\u10d1\\u10d0\\u10d7\\u10d8\",\n"
      + "      \"number\": \"13\",\n"
      + "      \"date\": \"2025-11-13\",\n"
      + "      \"month\": \"November\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"d\": 5,\n"
      + "      \"day\": \"\\u10de\\u10d0\\u10e0\\u10d0\\u10e1\\u10d9\\u10d4\\u10d5\\u10d8\",\n"
      + "      \"number\": \"14\",\n"
      + "      \"date\": \"2025-11-14\",\n"
      + "      \"month\": \"November\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"d\": 6,\n"
      + "      \"day\": \"\\u10e8\\u10d0\\u10d1\\u10d0\\u10d7\\u10d8\",\n"
      + "      \"number\": \"15\",\n"
      + "      \"date\": \"2025-11-15\",\n"
      + "      \"month\": \"November\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"d\": 0,\n"
      + "      \"day\": \"\\u10d9\\u10d5\\u10d8\\u10e0\\u10d0\",\n"
      + "      \"number\": \"16\",\n"
      + "      \"date\": \"2025-11-16\",\n"
      + "      \"month\": \"November\"\n"
      + "    }\n"
      + "  ]\n"
      + "}";


  private final WebClient webClient = WebClient.builder()
      .baseUrl(Constants.MAIN_URL)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .exchangeStrategies(ExchangeStrategies.builder()
          .codecs(configurer -> configurer
              .defaultCodecs()
              .maxInMemorySize(10 * 1024 * 1024)) // 10MB buffer size
          .build())
      .build();

  @Test
  void getStudentTableTest() {
    URI uri = URI.create(Constants.MAIN_URL + "/student/registration/getStudentTables");
    StudentTablesResponse stRes = webClient.post().uri(uri).header("Authorization", bearerToken)
        .bodyValue(body).retrieve()
        .bodyToMono(StudentTablesResponse.class).block();

    Assertions.assertNotNull(stRes);
    Assertions.assertNotNull(stRes.getData());
  }
}
