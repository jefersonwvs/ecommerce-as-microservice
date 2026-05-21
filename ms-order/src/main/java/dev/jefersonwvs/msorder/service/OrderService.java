package dev.jefersonwvs.msorder.service;

import dev.jefersonwvs.msorder.dto.CreateOrderRequest;
import dev.jefersonwvs.msorder.dto.OrderResponse;
import dev.jefersonwvs.msorder.entity.Order;
import dev.jefersonwvs.msorder.messaging.OrderCreatedEvent;
import dev.jefersonwvs.msorder.messaging.PaymentApprovedEvent;
import dev.jefersonwvs.msorder.messaging.outbox.EventType;
import dev.jefersonwvs.msorder.messaging.outbox.OutboxEvent;
import dev.jefersonwvs.msorder.messaging.outbox.OutboxEventRepository;
import dev.jefersonwvs.msorder.repository.OrderRepository;
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
        new OutboxEvent(eventId, EventType.ORDER_CREATED, objectMapper.writeValueAsString(event));
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
