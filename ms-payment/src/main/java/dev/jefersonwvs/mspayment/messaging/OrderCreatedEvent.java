package dev.jefersonwvs.mspayment.messaging;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedEvent(
    String eventId, Long orderId, BigDecimal totalAmount, Instant occurredAt) {}
