package dev.jefersonwvs.mspayment.entity;

import dev.jefersonwvs.mspayment.model.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tbl_payment")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long orderId;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private PaymentStatus status;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant createdAt;

  @Column
  @UpdateTimestamp
  private Instant updatedAt;

  protected Payment() {
    // JPA
  }

  public Payment(Long orderId, BigDecimal amount) {
    this.orderId = orderId;
    this.amount = amount;
    this.status = PaymentStatus.PENDING;
    this.createdAt = Instant.now();
  }

  public void approve() {
    this.status = PaymentStatus.APPROVED;
  }

  public void reject() {
    this.status = PaymentStatus.REJECTED;
  }

  public Long getId() {
    return id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public PaymentStatus getStatus() {
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
    return "Payment{" +
      "id=" + id +
      ", orderId=" + orderId +
      ", totalAmount=" + amount +
      ", status=" + status +
      '}';
  }
}
