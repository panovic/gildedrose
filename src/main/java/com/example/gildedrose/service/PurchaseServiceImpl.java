package com.example.gildedrose.service;

import com.example.gildedrose.dto.Purchase;
import com.example.gildedrose.dto.User;
import com.example.gildedrose.exceptions.InsufficientStockException;
import com.example.gildedrose.exceptions.ItemNotFoundException;
import com.example.gildedrose.exceptions.UserNotFoundException;
import com.example.gildedrose.model.PurchaseRequest;
import com.example.gildedrose.repository.ItemRepository;
import com.example.gildedrose.repository.PurchaseRepository;
import com.example.gildedrose.repository.UserRepository;
import com.example.gildedrose.repository.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private ViewRepository viewRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PurchaseRepository purchaseRepo;

    @Value("${price.surge.time.minutes}")
    private int priceSurgePeriodMinutes;

    @Value("${price.surge.view.count}")
    private int priceSurgeViewCount;

    @Value("${price.surge.percentage}")
    private int priceSurgePercentage;

    public PurchaseServiceImpl(ItemRepository itemRepo, ViewRepository viewRepo, UserRepository userRepo, PurchaseRepository purchaseRepo) {
        this.itemRepo = itemRepo;
        this.viewRepo = viewRepo;
        this.userRepo = userRepo;
        this.purchaseRepo = purchaseRepo;
    }

    @Override
    @Transactional
    public void purchaseItem(PurchaseRequest purchaseRequest, String apiKey) {
        // get user based on the given api key
        User user = userRepo.findDistinctByApiKey(apiKey).orElseThrow(() -> new UserNotFoundException());

        final double priceSurgeFactor = calculatePriceSurgeFactor();

        itemRepo.findById(purchaseRequest.getItemId())
                .map(item -> {
                    // check if enough stock
                    if (purchaseRequest.getItemQuantity() > item.getQuantity()) {
                        throw new InsufficientStockException(purchaseRequest.getItemId());
                    }

                    // update stock quantity
                    item.setQuantity(item.getQuantity() - purchaseRequest.getItemQuantity());

                    // calculate item price
                    int price = (int) Math.round(item.getPrice() * priceSurgeFactor);

                    // submit the order
                    Purchase purchase = new Purchase(user, item, price, LocalDateTime.now());
                    purchaseRepo.save(purchase);

                    return itemRepo.save(item);
                })
                .orElseThrow(() -> new ItemNotFoundException(purchaseRequest.getItemId()));
    }

    @Override
    public double calculatePriceSurgeFactor() {
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(priceSurgePeriodMinutes);
        long viewCount = viewRepo.findAll().stream().filter(view -> view.getDateTime().isAfter(timeThreshold)).count();
        return (viewCount > priceSurgeViewCount) ? (1+(priceSurgePercentage/100.0)) : 1;
    }
}
