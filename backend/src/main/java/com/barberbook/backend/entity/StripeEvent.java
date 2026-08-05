package com.barberbook.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stripe_events")
public class StripeEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "event_type", nullable = false, length = 120)
    private String eventType;

    @Column(name = "checkout_session_id")
    private String checkoutSessionId;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

    protected StripeEvent() {
    }

    public StripeEvent(
        String eventId,
        String eventType,
        String checkoutSessionId,
        LocalDateTime processedAt
    ) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.checkoutSessionId = checkoutSessionId;
        this.processedAt = processedAt;
    }

    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getCheckoutSessionId() {
        return checkoutSessionId;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}
