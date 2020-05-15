package com.example.gildedrose.controller;

import com.example.gildedrose.model.ItemResponse;
import com.example.gildedrose.service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value="/items", produces= MediaType.APPLICATION_JSON_VALUE)
    public List<ItemResponse> getAllItems() {
        return itemService.getItems();
    }
}
