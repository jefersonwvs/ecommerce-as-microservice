package dev.jefersonwvs.msorder.service;

import dev.jefersonwvs.msorder.dto.CreateOrderRequest;
import dev.jefersonwvs.msorder.dto.OrderResponse;
import dev.jefersonwvs.msorder.entity.Order;
import dev.jefersonwvs.msorder.entity.OrderStatus;
import dev.jefersonwvs.msorder.messaging.OrderCreatedEvent;
import dev.jefersonwvs.msorder.messaging.OrderEventProducer;
import dev.jefersonwvs.msorder.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

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
      orderRequest.amount(),
      OrderStatus.PENDING
    );

    entity = orderRepository.save(entity);

    orderEventProducer.publishOrderCreated(
      new OrderCreatedEvent(
        UUID.randomUUID().toString(),
        Instant.now(),
        entity.getId(),
        entity.getCustomerId(),
        entity.getAmount(),
        entity.getStatus()
      )
    );

    return new OrderResponse(
      entity.getId(),
      entity.getCustomerId(),
      entity.getAmount(),
      entity.getStatus(),
      entity.getCreatedAt()
    );
  }
}
