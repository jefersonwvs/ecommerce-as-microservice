package dev.jefersonwvs.order.messaging;

import dev.jefersonwvs.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentApprovedConsumer {

  private static final Logger logger = LoggerFactory.getLogger(PaymentApprovedConsumer.class);

  private final OrderService orderService;

  public PaymentApprovedConsumer(OrderService orderService) {
    this.orderService = orderService;
  }

  @RabbitListener(queues = RabbitMQConfig.ORDER_PAYMENT_APPROVED_QUEUE)
  public void consume(PaymentApprovedEvent event) {
    logger.info(
        "Received payment-approved event: orderId={}, paymentId={}",
        event.orderId(),
        event.paymentId());
    var payment = orderService.approveOrderPayment(event);
    logger.info("Order payment approved: orderId={} paymentId={}", event.orderId(), payment.id());
  }
}
