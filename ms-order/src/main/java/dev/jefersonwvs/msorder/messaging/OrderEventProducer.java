package dev.jefersonwvs.msorder.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {

  private final RabbitTemplate rabbitTemplate;

  public OrderEventProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishOrderCreated(OrderCreatedEvent orderCreatedEvent) {
    rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ORDER_CREATED_EVENT, orderCreatedEvent);
  }

}
