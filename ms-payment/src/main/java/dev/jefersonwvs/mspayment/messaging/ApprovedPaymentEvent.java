package dev.jefersonwvs.mspayment.messaging;

import java.math.BigDecimal;
import java.time.Instant;

public record ApprovedPaymentEvent(
  String eventId,
  Long paymentId,
  BigDecimal amount,
  Instant occurredAt
) {
}
