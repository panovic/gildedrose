package com.example.gildedrose.repository;

import com.example.gildedrose.dto.Item;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;


    @Test
    public void whenFindAll_thenReturnItem() {
        Item beer = new Item("Honey Brown", "Big Rock Honey Brown Amber Lager", 8, 100);
        entityManager.persist(beer);
        entityManager.flush();

        Item found = itemRepository.findAll().get(0);
        assert(found.equals(beer));
        assert(found.getName().equals(beer.getName()));
    }

    @Test
    public void whenFindAllEmpty_thenReturnEmptyList() {
        List<Item> found = itemRepository.findAll();
        assert(found.isEmpty());
    }
}
