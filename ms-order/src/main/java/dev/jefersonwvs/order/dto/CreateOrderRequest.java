package dev.jefersonwvs.order.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(Long customerId, BigDecimal totalAmount) {}
