package dev.jefersonwvs.order.controller;

import dev.jefersonwvs.order.dto.CreateOrderRequest;
import dev.jefersonwvs.order.dto.OrderResponse;
import dev.jefersonwvs.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
    return orderService.createOrder(createOrderRequest);
  }
}
