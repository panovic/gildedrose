package com.example.gildedrose.service;

import com.example.gildedrose.dto.Item;
import com.example.gildedrose.dto.View;
import com.example.gildedrose.model.ItemResponse;
import com.example.gildedrose.repository.ItemRepository;
import com.example.gildedrose.repository.ViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemServiceTests {
    @MockBean
    private ViewRepository viewRepo;

    @MockBean
    private ItemRepository itemRepo;

    @MockBean
    private PurchaseService purchaseService;

    private ItemService itemService;
    private List<Item> items;
    private Item beer;

    @BeforeEach
    public void setup() {
        itemService = new ItemServiceImpl(itemRepo, viewRepo, purchaseService);
        items = new ArrayList<Item>();
        beer = new Item("Honey Brown", "Big Rock Honey Brown Amber Lager", 8, 100);
        items.add(beer);
    }

    @Test
    public void whenGetItems_thenReturnItems() {
        double priceSurgeFactor = 1.0d;
        when(purchaseService.calculatePriceSurgeFactor()).thenReturn(priceSurgeFactor);
        when(itemRepo.findAll()).thenReturn(items);

        List<ItemResponse> found = itemService.getItems();

        assert(!found.isEmpty());
        assert(found.get(0).getName().equals(beer.getName()));
        assert(found.get(0).getPrice().equals(beer.getPrice()));
        verify(viewRepo, times(1)).save(any(View.class));
        verify(purchaseService, times(1)).calculatePriceSurgeFactor();
    }

    @Test
    public void whenGetItemsEdgeThreshold_thenReturnOriginalPrices() {
        double priceSurgeFactor = 1.0;
        when(purchaseService.calculatePriceSurgeFactor()).thenReturn(priceSurgeFactor);
        when(itemRepo.findAll()).thenReturn(items);

        // view items up to the edge case - prices should stay the same
        List<ItemResponse> found = null;
        int viewCount = 10;
        for (int i=0; i<viewCount; i++) {
            found = itemService.getItems();
        }

        assert(!found.isEmpty());
        assert(found.get(0).getPrice() == ((int) Math.round(beer.getPrice() * priceSurgeFactor)));
        verify(viewRepo, times(viewCount)).save(any(View.class));
        verify(purchaseService, times(viewCount)).calculatePriceSurgeFactor();
    }

    @Test
    public void whenGetItemsExceedThreshold_thenReturnIncreasedPrices() {
        double priceSurgeFactor = 1.1;
        when(purchaseService.calculatePriceSurgeFactor()).thenReturn(priceSurgeFactor);
        when(itemRepo.findAll()).thenReturn(items);

        // view items above the edge case - should trigger increased prices
        List<ItemResponse> found = null;
        int viewCount = 11;
        for (int i=0; i<viewCount; i++) {
            found = itemService.getItems();
        }

        assert(!found.isEmpty());
        assert(found.get(0).getPrice() == ((int) Math.round(beer.getPrice() * priceSurgeFactor)));
        verify(viewRepo, times(viewCount)).save(any(View.class));
        verify(purchaseService, times(viewCount)).calculatePriceSurgeFactor();
    }

    @Test
    public void whenGetItemsEmpty_thenReturnEmptyList() {
        when(itemRepo.findAll()).thenReturn(Collections.emptyList());

        List<ItemResponse> found = itemService.getItems();

        assert(found.isEmpty());
    }
}
