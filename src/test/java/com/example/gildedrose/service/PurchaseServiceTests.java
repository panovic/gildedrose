package com.example.gildedrose.service;

import com.example.gildedrose.dto.Item;
import com.example.gildedrose.dto.Purchase;
import com.example.gildedrose.dto.User;
import com.example.gildedrose.dto.View;
import com.example.gildedrose.exceptions.InsufficientStockException;
import com.example.gildedrose.exceptions.UserNotFoundException;
import com.example.gildedrose.model.PurchaseRequest;
import com.example.gildedrose.repository.ItemRepository;
import com.example.gildedrose.repository.PurchaseRepository;
import com.example.gildedrose.repository.UserRepository;
import com.example.gildedrose.repository.ViewRepository;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseServiceTests {
    @MockBean
    private ItemRepository itemRepo;

    @MockBean
    private ViewRepository viewRepo;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private PurchaseRepository purchaseRepo;

    private PurchaseService purchaseService;
    private List<Item> items;
    private Item beer;
    private Optional<User> user;
    private List<View> views;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @BeforeEach
    public void setup() {
        purchaseService = new PurchaseServiceImpl(itemRepo, viewRepo, userRepo, purchaseRepo);
        items = new ArrayList<Item>();
        beer = new Item("Honey Brown", "Big Rock Honey Brown Amber Lager", 8, 100);
        beer.setId(1L);
        items.add(beer);
        user = Optional.ofNullable(new User(1L, "Mobile", "Integration", "ABC123"));
        views = new ArrayList<View>();
        views.add(new View(LocalDateTime.now()));
    }

    @Test
    public void whenPurchaseItem_thenReduceStockAndSubmitOrder() {
        PurchaseRequest purchaseReq = new PurchaseRequest(beer.getId(), 50);
        when(userRepo.findDistinctByApiKey(anyString())).thenReturn(user);
        when(viewRepo.findAll()).thenReturn(views);
        when(itemRepo.findById(anyLong())).thenReturn(Optional.ofNullable(beer));
        when(itemRepo.save(any())).thenReturn(beer);

        purchaseService.purchaseItem(purchaseReq, user.get().getApiKey());

        verify(userRepo, times(1)).findDistinctByApiKey(anyString());
        verify(itemRepo, times(1)).findById(anyLong());
        verify(purchaseRepo, times(1)).save(any(Purchase.class));
        verify(itemRepo, times(1)).save(any(Item.class));
    }

    @Test
    public void whenPurchaseItemWithIncorrectApiKey_thenExceptionThrown() {
        PurchaseRequest purchaseReq = new PurchaseRequest(beer.getId(), 50);
        when(userRepo.findDistinctByApiKey(anyString())).thenReturn(Optional.ofNullable(null));
        when(viewRepo.findAll()).thenReturn(views);

        try {
            purchaseService.purchaseItem(purchaseReq, user.get().getApiKey());
            assert(false);
        } catch(UserNotFoundException e) {}
    }

    @Test
    public void whenPurchaseItemWithInsufficientStock_thenExceptionThrown() {
        PurchaseRequest purchaseReq = new PurchaseRequest(beer.getId(), 150);
        Optional<Item> item = Optional.ofNullable(beer);
        when(userRepo.findDistinctByApiKey(anyString())).thenReturn(user);
        when(viewRepo.findAll()).thenReturn(views);
        when(itemRepo.findById(anyLong())).thenReturn(Optional.ofNullable(beer));
        when(itemRepo.save(any())).thenReturn(beer);

        try {
            purchaseService.purchaseItem(purchaseReq, user.get().getApiKey());
            assert(false);
        } catch(InsufficientStockException e) {}
    }
}
