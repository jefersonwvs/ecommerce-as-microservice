package dev.jefersonwvs.mspayment.service;

import dev.jefersonwvs.mspayment.dto.PaymentWebhookRequest;
import dev.jefersonwvs.mspayment.entity.Payment;
import dev.jefersonwvs.mspayment.messaging.OrderCreatedEvent;
import dev.jefersonwvs.mspayment.messaging.ProcessedPaymentEvent;
import dev.jefersonwvs.mspayment.messaging.ProcessedPaymentProducer;
import dev.jefersonwvs.mspayment.model.PaymentStatus;
import dev.jefersonwvs.mspayment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final ProcessedPaymentProducer processedPaymentProducer;

  public PaymentService(PaymentRepository paymentRepository, ProcessedPaymentProducer processedPaymentProducer) {
    this.paymentRepository = paymentRepository;
    this.processedPaymentProducer = processedPaymentProducer;
  }

  @Transactional
  public void createPendingPayment(OrderCreatedEvent event) {
    paymentRepository.save(new Payment(event.customerId(), event.orderId(), event.amount(), PaymentStatus.PENDING));
  }

  @Transactional
  public void approvePayment(PaymentWebhookRequest request) {
    var entity = paymentRepository.findById(request.paymentId()).orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));
    entity.setStatus(PaymentStatus.APPROVED);
    paymentRepository.save(entity);

    processedPaymentProducer.publishPendingPayment(new ProcessedPaymentEvent(UUID.randomUUID().toString(), entity.getId(), entity.getAmount(), entity.getStatus(), request.processedAt()));

  }
}
