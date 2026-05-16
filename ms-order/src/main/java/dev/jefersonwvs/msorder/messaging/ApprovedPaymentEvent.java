package dev.jefersonwvs.msorder.messaging;

import java.math.BigDecimal;
import java.time.Instant;

public record ApprovedPaymentEvent(
  String eventId,
  Long orderId,
  Long paymentId,
  BigDecimal amount,
  Instant occurredAt
) {
}
