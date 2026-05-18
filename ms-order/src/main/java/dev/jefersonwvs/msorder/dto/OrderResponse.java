package dev.jefersonwvs.msorder.dto;

import dev.jefersonwvs.msorder.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
    Long id, Long customerId, BigDecimal amount, OrderStatus status, Instant createdAt) {}
