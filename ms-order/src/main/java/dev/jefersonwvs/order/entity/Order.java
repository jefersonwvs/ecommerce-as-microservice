package dev.jefersonwvs.order.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tbl_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long customerId;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal totalAmount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private OrderStatus status;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant createdAt;

  @Column @UpdateTimestamp private Instant updatedAt;

  protected Order() {
    // JPA
  }

  public Order(Long customerId, BigDecimal totalAmount) {
    this.customerId = customerId;
    this.totalAmount = totalAmount;
    this.status = OrderStatus.CREATED;
    this.createdAt = Instant.now();
  }

  public void markAsPaid() {
    this.status = OrderStatus.PAID;
  }

  public void cancel() {
    this.status = OrderStatus.CANCELED;
  }

  public Long getId() {
    return id;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public String toString() {
    return "Order{"
        + "id="
        + id
        + ", customerId="
        + customerId
        + ", totalAmount="
        + totalAmount
        + ", status="
        + status
        + '}';
  }
}
