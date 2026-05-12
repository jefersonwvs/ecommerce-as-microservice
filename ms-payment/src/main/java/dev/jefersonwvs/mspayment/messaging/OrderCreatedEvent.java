package dev.jefersonwvs.mspayment.messaging;

import dev.jefersonwvs.mspayment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedEvent(
  String eventId,
  Instant occurredAt,
  Long orderId,
  Long customerId,
  BigDecimal amount,
  PaymentStatus status
) {
}
