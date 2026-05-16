package dev.jefersonwvs.mspayment.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentWebhookRequest(
  Long paymentId,
  BigDecimal amount,
  Instant processedAt
) {
}
