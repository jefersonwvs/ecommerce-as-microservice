package dev.jefersonwvs.mspayment.messaging.outbox;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tbl_outbox_event")
public class OutboxEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String eventId;

  @Column(nullable = false, updatable = false)
  @Enumerated(EnumType.STRING)
  private EventType eventType;

  @Column(columnDefinition = "TEXT", nullable = false, updatable = false)
  private String payload;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column private Instant publishedAt;

  protected OutboxEvent() {}

  public OutboxEvent(String eventId, EventType eventType, String payload) {
    this.eventId = eventId;
    this.eventType = eventType;
    this.payload = payload;
    this.createdAt = Instant.now();
  }

  public Long getId() {
    return id;
  }

  public String getEventId() {
    return eventId;
  }

  public EventType getEventType() {
    return eventType;
  }

  public String getPayload() {
    return payload;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(Instant publishedAt) {
    this.publishedAt = publishedAt;
  }

  @Transient
  public boolean isPublished() {
    return publishedAt != null;
  }

  @Override
  public String toString() {
    return "OutboxEvent{"
        + "id="
        + id
        + ", eventId='"
        + eventId
        + '\''
        + ", eventType="
        + eventType
        + ", createdAt="
        + createdAt
        + ", publishedAt="
        + publishedAt
        + '}';
  }
}
