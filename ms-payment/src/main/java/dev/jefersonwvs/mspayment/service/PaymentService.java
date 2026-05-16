package dev.jefersonwvs.mspayment.service;

import dev.jefersonwvs.mspayment.dto.PaymentWebhookRequest;
import dev.jefersonwvs.mspayment.entity.Payment;
import dev.jefersonwvs.mspayment.messaging.ApprovedPaymentEvent;
import dev.jefersonwvs.mspayment.messaging.ApprovedPaymentProducer;
import dev.jefersonwvs.mspayment.messaging.OrderCreatedEvent;
import dev.jefersonwvs.mspayment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

  private final PaymentRepository paymentRepository;
  private final ApprovedPaymentProducer approvedPaymentProducer;

  public PaymentService(PaymentRepository paymentRepository, ApprovedPaymentProducer approvedPaymentProducer) {
    this.paymentRepository = paymentRepository;
    this.approvedPaymentProducer = approvedPaymentProducer;
  }

  @Transactional
  public Payment createPendingPayment(OrderCreatedEvent event) {
    var entity = new Payment(event.orderId(), event.totalAmount());
    paymentRepository.save(entity);
    return entity;
  }

  @Transactional
  public void approvePayment(PaymentWebhookRequest request) {
    var entity = paymentRepository.findById(request.paymentId()).orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));
    entity.approve();
    paymentRepository.save(entity);
    logger.info("Payment approved: paymentId={}, orderId={}", entity.getId(), entity.getOrderId());

    approvedPaymentProducer.publishApprovedPayment(new ApprovedPaymentEvent(UUID.randomUUID().toString(), entity.getOrderId(), entity.getId(), entity.getAmount(), request.processedAt()));
    logger.info("Published approved payment: paymentId={}, orderId={}", entity.getId(), entity.getOrderId());
  }
}
