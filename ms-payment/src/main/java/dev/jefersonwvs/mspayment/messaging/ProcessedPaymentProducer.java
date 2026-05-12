package dev.jefersonwvs.mspayment.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProcessedPaymentProducer {

  private final RabbitTemplate rabbitTemplate;

  public ProcessedPaymentProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishPendingPayment(ProcessedPaymentEvent event) {
    rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.PROCESSED_PAYMENT_ROUTING_KEY, event);
  }

}
