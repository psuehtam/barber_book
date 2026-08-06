package com.barberbook.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "barber_shops")
public class BarberShop {

    public static final long SINGLE_ID = 1L;

    @Id
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 8)
    private String cep;

    @Column(length = 160)
    private String street;

    @Column(length = 20)
    private String number;

    @Column(length = 120)
    private String neighborhood;

    @Column(length = 120)
    private String city;

    @Column(length = 2)
    private String state;

    protected BarberShop() {
    }

    public BarberShop(String name) {
        this.id = SINGLE_ID;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCep() {
        return cep;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public void updateAddress(
        String name,
        String cep,
        String street,
        String number,
        String neighborhood,
        String city,
        String state
    ) {
        this.name = name;
        this.cep = cep;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
    }
}
