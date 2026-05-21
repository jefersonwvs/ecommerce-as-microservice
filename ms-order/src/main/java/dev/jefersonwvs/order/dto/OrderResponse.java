package dev.jefersonwvs.order.dto;

import dev.jefersonwvs.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
    Long id, Long customerId, BigDecimal amount, OrderStatus status, Instant createdAt) {}
