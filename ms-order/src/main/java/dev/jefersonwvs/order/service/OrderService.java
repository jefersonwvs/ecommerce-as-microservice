package dev.jefersonwvs.order.service;

import dev.jefersonwvs.order.dto.CreateOrderRequest;
import dev.jefersonwvs.order.dto.OrderResponse;
import dev.jefersonwvs.order.entity.Order;
import dev.jefersonwvs.order.messaging.OrderCreatedEvent;
import dev.jefersonwvs.order.messaging.PaymentApprovedEvent;
import dev.jefersonwvs.order.messaging.outbox.OutboxEvent;
import dev.jefersonwvs.order.messaging.outbox.OutboxEventRepository;
import dev.jefersonwvs.order.repository.OrderRepository;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private final OrderRepository orderRepository;
  private final OutboxEventRepository outboxEventRepository;
  private final ObjectMapper objectMapper;

  public OrderService(
      OrderRepository orderRepository,
      OutboxEventRepository outboxEventRepository,
      ObjectMapper objectMapper) {
    this.orderRepository = orderRepository;
    this.outboxEventRepository = outboxEventRepository;
    this.objectMapper = objectMapper;
  }

  @Transactional
  public OrderResponse createOrder(CreateOrderRequest orderRequest) {
    var order = new Order(orderRequest.customerId(), orderRequest.totalAmount());

    order = orderRepository.save(order);
    logger.info("Order created: orderId={}, totalAmount={}", order.getId(), order.getTotalAmount());

    var eventId = UUID.randomUUID().toString();
    var eventCreatedAt = Instant.now();
    var event =
        new OrderCreatedEvent(eventId, order.getId(), order.getTotalAmount(), eventCreatedAt);

    var outboxEvent =
        new OutboxEvent(eventId, "order.created", objectMapper.writeValueAsString(event));
    outboxEventRepository.save(outboxEvent);

    logger.info("Outbox event created: {}", objectMapper.writeValueAsString(outboxEvent));

    return new OrderResponse(
        order.getId(),
        order.getCustomerId(),
        order.getTotalAmount(),
        order.getStatus(),
        order.getCreatedAt());
  }

  @Transactional
  public OrderResponse approveOrderPayment(PaymentApprovedEvent event) {
    var order =
        orderRepository
            .findById(event.orderId())
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
    order.markAsPaid();
    orderRepository.save(order);

    return new OrderResponse(
        order.getId(),
        order.getCustomerId(),
        order.getTotalAmount(),
        order.getStatus(),
        order.getCreatedAt());
  }
}
