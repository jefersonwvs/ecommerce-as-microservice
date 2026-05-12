package dev.jefersonwvs.mspayment.messaging;

import dev.jefersonwvs.mspayment.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

  private final PaymentService paymentService;

  public OrderCreatedConsumer(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @RabbitListener(queues = "payment.order-created")
  public void consume(OrderCreatedEvent event) {
    System.out.println("Order Created Event: " + event);
    System.out.println("Order ID: " + event.orderId());

    paymentService.createPendingPayment(event);
  }

}
