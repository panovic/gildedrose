package com.example.gildedrose.service;

import com.example.gildedrose.dto.View;
import com.example.gildedrose.model.ItemResponse;
import com.example.gildedrose.repository.ItemRepository;
import com.example.gildedrose.repository.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private ViewRepository viewRepo;

    @Autowired
    private PurchaseService purchaseService;

    public ItemServiceImpl(ItemRepository itemRepo, ViewRepository viewRepo, PurchaseService purchaseService) {
        this.itemRepo = itemRepo;
        this.viewRepo = viewRepo;
        this .purchaseService = purchaseService;
    }

    @Override
    @Transactional
    public List<ItemResponse> getItems() {
        // keep track of item views
        View view = new View(LocalDateTime.now());
        viewRepo.save(view);

        // update the price before returning items
        final double priceSurgeFactor = purchaseService.calculatePriceSurgeFactor();
        return itemRepo.findAll().stream()
                .map(item -> {
                    int price = (int) Math.round(item.getPrice() * priceSurgeFactor);
                    return new ItemResponse(item.getId(), item.getName(), item.getDescription(), price, item.getQuantity());
                })
                .collect(Collectors.toList());
    }
}
