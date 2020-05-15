package com.example.gildedrose.controller;

import com.example.gildedrose.model.PurchaseRequest;
import com.example.gildedrose.service.PurchaseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping(value="/purchases", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public void purchaseItem(@RequestBody @NotNull PurchaseRequest purchaseRequest, @RequestHeader("x-apikey") String apiKey) {
        purchaseService.purchaseItem(purchaseRequest, apiKey);
    }
}
