package dev.jefersonwvs.msorder.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(Long customerId, BigDecimal amount) {
}
