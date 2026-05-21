package dev.jefersonwvs.mspayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MSPaymentApplication {

  public static void main(String[] args) {
    SpringApplication.run(MSPaymentApplication.class, args);
  }
}
