package dev.jefersonwvs.mspayment.service;

import dev.jefersonwvs.mspayment.dto.PaymentWebhookRequest;
import dev.jefersonwvs.mspayment.entity.Payment;
import dev.jefersonwvs.mspayment.messaging.OrderCreatedEvent;
import dev.jefersonwvs.mspayment.messaging.PaymentApprovedEvent;
import dev.jefersonwvs.mspayment.messaging.outbox.EventType;
import dev.jefersonwvs.mspayment.messaging.outbox.OutboxEvent;
import dev.jefersonwvs.mspayment.messaging.outbox.OutboxEventRepository;
import dev.jefersonwvs.mspayment.repository.PaymentRepository;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
public class PaymentService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

  private final PaymentRepository paymentRepository;
  private final OutboxEventRepository outboxEventRepository;
  private final ObjectMapper objectMapper;

  public PaymentService(
      PaymentRepository paymentRepository,
      OutboxEventRepository outboxEventRepository,
      ObjectMapper objectMapper) {
    this.paymentRepository = paymentRepository;
    this.outboxEventRepository = outboxEventRepository;
    this.objectMapper = objectMapper;
  }

  @Transactional
  public void createPendingPayment(OrderCreatedEvent event) {
    var existingPayment = paymentRepository.findByOrderId(event.orderId()).orElse(null);
    if (existingPayment != null) {
      logger.info(
          "Pending payment already exists: paymentId={}, orderId={}",
          existingPayment.getId(),
          event.orderId());
      return;
    }

    var newPayment = new Payment(event.orderId(), event.totalAmount());
    paymentRepository.save(newPayment);
    logger.info(
        "Pending payment created: paymentId={}, orderId={}", newPayment.getId(), event.orderId());
  }

  @Transactional
  public void approvePayment(PaymentWebhookRequest request) {
    var payment =
        paymentRepository
            .findById(request.paymentId())
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));
    payment.approve();
    paymentRepository.save(payment);
    logger.info(
        "Payment approved: paymentId={}, orderId={}", payment.getId(), payment.getOrderId());

    var eventId = UUID.randomUUID().toString();
    var eventCreatedAt = Instant.now();
    var event =
        new PaymentApprovedEvent(
            eventId, payment.getOrderId(), payment.getId(), payment.getAmount(), eventCreatedAt);

    var outboxEvent =
        new OutboxEvent(
            eventId, EventType.PAYMENT_APPROVED, objectMapper.writeValueAsString(event));
    outboxEventRepository.save(outboxEvent);

    logger.info("Outbox event created: {}", objectMapper.writeValueAsString(outboxEvent));
  }
}
