package dev.jefersonwvs.payment.controller;

import dev.jefersonwvs.payment.dto.PaymentWebhookRequest;
import dev.jefersonwvs.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks/payment")
public class PaymentWebhookController {

  private final PaymentService paymentService;

  public PaymentWebhookController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void handlePayment(@RequestBody PaymentWebhookRequest request) {
    paymentService.approvePayment(request);
  }
}
