package com.barberbook.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_item_id", nullable = false)
    private ServiceItem serviceItem;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private AppointmentStatus status;

    @Column(name = "checkout_session_id", unique = true)
    private String checkoutSessionId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Appointment() {
    }

    public Appointment(
        User client,
        Barber barber,
        ServiceItem serviceItem,
        LocalDateTime startAt,
        LocalDateTime endAt,
        BigDecimal amount,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
    ) {
        this.client = client;
        this.barber = barber;
        this.serviceItem = serviceItem;
        this.startAt = startAt;
        this.endAt = endAt;
        this.amount = amount;
        this.status = AppointmentStatus.PENDING_PAYMENT;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public Barber getBarber() {
        return barber;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getCheckoutSessionId() {
        return checkoutSessionId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void attachCheckoutSession(String sessionId) {
        this.checkoutSessionId = sessionId;
    }

    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
    }

    public void expire() {
        this.status = AppointmentStatus.EXPIRED;
    }

    public void markPaymentReview() {
        this.status = AppointmentStatus.PAYMENT_REVIEW;
    }
}
