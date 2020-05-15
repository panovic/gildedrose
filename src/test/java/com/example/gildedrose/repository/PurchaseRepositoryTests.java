package com.example.gildedrose.repository;

import com.example.gildedrose.dto.Item;
import com.example.gildedrose.dto.Purchase;
import com.example.gildedrose.dto.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PurchaseRepositoryTests {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void whenFindAll_thenReturnPurchase() {
        LocalDateTime datetime = LocalDateTime.now();
        User user = new User(1L, "First", "Last", "ABC123");
        user = userRepository.save(user);
        Item item = new Item("Honey Brown", "Big Rock Honey Brown Amber Lager", 8, 100);
        item = itemRepository.save(item);
        Integer price = 10;
        Purchase purchase = new Purchase(user, item, price, datetime);
        purchaseRepository.save(purchase);

        Purchase found = purchaseRepository.findAll().get(0);
        assert(found.getPrice().equals(price));
        assert(found.getCreatedDateTime().equals(datetime));
    }

    @Test
    public void whenFindAllEmpty_thenReturnEmptyList() {
        List<Purchase> found = purchaseRepository.findAll();
        assert(found.isEmpty());
    }
}
