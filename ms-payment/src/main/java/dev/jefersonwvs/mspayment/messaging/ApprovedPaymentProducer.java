package dev.jefersonwvs.mspayment.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApprovedPaymentProducer {

  private final RabbitTemplate rabbitTemplate;

  public ApprovedPaymentProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishApprovedPayment(ApprovedPaymentEvent event) {
    rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.APPROVED_PAYMENT_ROUTING_KEY, event);
  }

}
