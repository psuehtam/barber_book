package com.barberbook.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_items")
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private boolean active;

    protected ServiceItem() {
    }

    public ServiceItem(
        String name,
        BigDecimal price,
        int durationMinutes
    ) {
        this.name = name;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public boolean isActive() {
        return active;
    }

    public void update(
        String name,
        BigDecimal price,
        int durationMinutes,
        boolean active
    ) {
        this.name = name;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.active = active;
    }
}
