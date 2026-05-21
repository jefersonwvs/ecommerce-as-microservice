package dev.jefersonwvs.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendApprovedPaymentEmail(String to, Long orderId) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Approved Payment");

    message.setText(
        """
        Your payment has been approved.

        Order ID: %s
        """
            .formatted(orderId));

    mailSender.send(message);

    logger.info("Approved payment (orderId={}) email has been sent to {}", orderId, to);
  }
}
