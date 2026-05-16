package dev.jefersonwvs.msorder.service;

import dev.jefersonwvs.msorder.dto.CreateOrderRequest;
import dev.jefersonwvs.msorder.dto.OrderResponse;
import dev.jefersonwvs.msorder.entity.Order;
import dev.jefersonwvs.msorder.messaging.OrderCreatedEvent;
import dev.jefersonwvs.msorder.messaging.OrderEventProducer;
import dev.jefersonwvs.msorder.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private final OrderRepository orderRepository;
  private final OrderEventProducer orderEventProducer;

  public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
    this.orderRepository = orderRepository;
    this.orderEventProducer = orderEventProducer;
  }

  @Transactional
  public OrderResponse createOrder(CreateOrderRequest orderRequest) {
    var entity = new Order(
      orderRequest.customerId(),
      orderRequest.totalAmount()
    );

    entity = orderRepository.save(entity);
    logger.info("Order created: orderId={}, totalAmount={}", entity.getId(), entity.getTotalAmount());

    orderEventProducer.publishOrderCreated(
      new OrderCreatedEvent(
        UUID.randomUUID().toString(),
        entity.getId(),
        entity.getTotalAmount(),
        Instant.now()
      )
    );
    logger.info("Published order-created event: orderId={}", entity.getId());

    return new OrderResponse(
      entity.getId(),
      entity.getCustomerId(),
      entity.getTotalAmount(),
      entity.getStatus(),
      entity.getCreatedAt()
    );
  }
}
