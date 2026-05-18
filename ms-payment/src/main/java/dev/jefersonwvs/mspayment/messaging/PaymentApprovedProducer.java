package dev.jefersonwvs.mspayment.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentApprovedProducer {

  private final RabbitTemplate rabbitTemplate;

  public PaymentApprovedProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishPaymentApproved(PaymentApprovedEvent event) {
    rabbitTemplate.convertAndSend(
        MessagingConfig.EXCHANGE, MessagingConfig.PAYMENT_APPROVED_EVENT, event);
  }
}
