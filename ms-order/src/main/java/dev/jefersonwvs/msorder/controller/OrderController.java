package dev.jefersonwvs.msorder.controller;

import dev.jefersonwvs.msorder.dto.CreateOrderRequest;
import dev.jefersonwvs.msorder.dto.OrderResponse;
import dev.jefersonwvs.msorder.service.OrderService;
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
