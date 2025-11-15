package lms.kiu.notifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class NemoLmsNotifierApplication {

  public static void main(String[] args) {
    SpringApplication.run(NemoLmsNotifierApplication.class, args);
  }
}
