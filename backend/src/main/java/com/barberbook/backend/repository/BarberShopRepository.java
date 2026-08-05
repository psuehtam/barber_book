package com.barberbook.backend.repository;

import com.barberbook.backend.entity.BarberShop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberShopRepository
    extends JpaRepository<BarberShop, Long> {
}
