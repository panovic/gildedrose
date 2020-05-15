package com.example.gildedrose.repository;

import com.example.gildedrose.dto.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
