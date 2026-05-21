package dev.jefersonwvs.msorder.messaging.outbox;

public enum EventType {
  ORDER_CREATED("order.created"),
  PAYMENT_APPROVED("payment.approved");

  private final String event;

  EventType(String event) {
    this.event = event;
  }

  @Override
  public String toString() {
    return event;
  }
}
