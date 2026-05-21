package dev.jefersonwvs.mspayment.messaging.outbox;

import dev.jefersonwvs.mspayment.messaging.MessagingConfig;
import dev.jefersonwvs.mspayment.messaging.OrderCreatedEvent;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class OutboxPublisher {

  private static final Logger logger = LoggerFactory.getLogger(OutboxPublisher.class);

  private final OutboxEventRepository outboxEventRepository;
  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper;

  public OutboxPublisher(
      OutboxEventRepository outboxEventRepository,
      RabbitTemplate rabbitTemplate,
      ObjectMapper objectMapper) {
    this.outboxEventRepository = outboxEventRepository;
    this.rabbitTemplate = rabbitTemplate;
    this.objectMapper = objectMapper;
  }

  @Scheduled(fixedDelay = 5000)
  public void publishPendingEvents() {
    var events = outboxEventRepository.findAllByPublishedAtNull();

    for (var event : events) {
      try {
        rabbitTemplate.convertAndSend(
            MessagingConfig.EXCHANGE,
            event.getEventType().toString(),
            objectMapper.readValue(event.getPayload(), OrderCreatedEvent.class));

        event.setPublishedAt(Instant.now());
        outboxEventRepository.save(event);

        logger.info("Outbox event published: {}", event);

      } catch (Exception e) {
        logger.error("Failed to publish outbox event: eventId={}", event.getEventId(), e);
      }
    }
  }
}
