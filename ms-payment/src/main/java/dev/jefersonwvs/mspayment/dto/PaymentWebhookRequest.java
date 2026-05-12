package dev.jefersonwvs.mspayment.dto;

import dev.jefersonwvs.mspayment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentWebhookRequest(
  Long paymentId,
  BigDecimal amount,
  PaymentStatus status,
  Instant processedAt
) {
}
