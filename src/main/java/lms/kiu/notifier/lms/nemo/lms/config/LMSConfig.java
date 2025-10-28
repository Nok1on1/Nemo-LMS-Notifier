package lms.kiu.notifier.lms.nemo.lms.config;

import lms.kiu.notifier.lms.nemo.lms.service.LMSService;
import lms.kiu.notifier.lms.nemo.mongo.service.CourseService;
import lms.kiu.notifier.lms.nemo.mongo.service.StudentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

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
  public LMSService lmsService(StudentService studentService, CourseService courseService,
      TextEncryptor textEncryptor) {
    return new LMSService(studentService, courseService, textEncryptor);
  }
}
