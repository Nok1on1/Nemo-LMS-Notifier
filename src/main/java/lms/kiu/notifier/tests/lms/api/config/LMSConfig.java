package lms.kiu.notifier.tests.lms.api.config;

import lms.kiu.notifier.tests.data.Constants;
import lms.kiu.notifier.tests.lms.api.service.LMSService;
import lms.kiu.notifier.tests.mongo.service.CourseService;
import lms.kiu.notifier.tests.mongo.service.StudentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LMSConfig {

  @Value("${encryptor.password}")
  private String encryptorPassword;

  @Value("${encryptor.salt}")
  private String encryptorSalt;

  @Bean
  public TextEncryptor textEncryptor() {
    return Encryptors.text(encryptorPassword, encryptorSalt);
  }

  @Bean
  public WebClient lmsWebClient() {
    return WebClient.builder()
        .baseUrl(Constants.MAIN_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(10 * 1024 * 1024))
            .build())
        .build();
  }

  @Bean
  public LMSService lmsService(StudentService studentService, CourseService courseService,
      TextEncryptor textEncryptor, WebClient lmsWebClient) {
    return new LMSService(studentService, courseService, textEncryptor, lmsWebClient);
  }
}
