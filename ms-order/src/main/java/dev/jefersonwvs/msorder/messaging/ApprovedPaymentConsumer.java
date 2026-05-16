package dev.jefersonwvs.msorder.messaging;

import dev.jefersonwvs.msorder.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ApprovedPaymentConsumer {

  private static final Logger logger = LoggerFactory.getLogger(ApprovedPaymentConsumer.class);

  private final OrderService orderService;

  public ApprovedPaymentConsumer(OrderService orderService) {
    this.orderService = orderService;
  }

  @RabbitListener(queues = MessagingConfig.APPROVED_PAYMENT_QUEUE)
  public void consume(ApprovedPaymentEvent event) {
    logger.info("Received approved-payment event: orderId={}, paymentId={}", event.orderId(), event.paymentId());
    var payment = orderService.approveOrderPayment(event);
    logger.info("Order payment approved: orderId={} paymentId={}", event.orderId(), payment.id());
  }

}