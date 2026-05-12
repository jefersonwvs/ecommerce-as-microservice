package dev.jefersonwvs.mspayment.messaging;

import dev.jefersonwvs.mspayment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record ProcessedPaymentEvent(
  String eventId,
  Long paymentId,
  BigDecimal amount,
  PaymentStatus status,
  Instant occurredAt
) {
}
