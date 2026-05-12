package dev.jefersonwvs.msorder.messaging;

import dev.jefersonwvs.msorder.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedEvent(
  String eventId,
  Instant occurredAt,
  Long orderId,
  Long customerId,
  BigDecimal amount,
  OrderStatus status
) {
}
