package com.example.gildedrose.service;

import com.example.gildedrose.model.PurchaseRequest;

public interface PurchaseService {
    void purchaseItem(PurchaseRequest order, String apiKey);
    double calculatePriceSurgeFactor();
}
