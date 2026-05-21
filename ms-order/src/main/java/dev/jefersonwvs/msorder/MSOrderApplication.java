package dev.jefersonwvs.msorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MSOrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(MSOrderApplication.class, args);
  }
}
