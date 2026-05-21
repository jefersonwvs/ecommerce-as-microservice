package dev.jefersonwvs.notification.messaging;

import dev.jefersonwvs.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentApprovedConsumer {

  private static final Logger logger = LoggerFactory.getLogger(PaymentApprovedConsumer.class);

  private final EmailService emailService;

  public PaymentApprovedConsumer(EmailService emailService) {
    this.emailService = emailService;
  }

  @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_PAYMENT_APPROVED_QUEUE)
  public void consume(PaymentApprovedEvent event) {
    logger.info("Received payment-approved event. orderId={}", event.orderId());
    emailService.sendApprovedPaymentEmail("jeferson@mail.com", event.orderId());
  }
}
