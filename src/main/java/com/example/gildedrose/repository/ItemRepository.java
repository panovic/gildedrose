package com.example.gildedrose.repository;

import com.example.gildedrose.dto.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
