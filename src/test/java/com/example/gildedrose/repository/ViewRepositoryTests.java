package com.example.gildedrose.repository;

import com.example.gildedrose.dto.View;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ViewRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ViewRepository viewRepository;

    @Test
    public void whenFindAll_thenReturnItem() {
        LocalDateTime datetime = LocalDateTime.now();
        View view = new View(datetime);
        entityManager.persist(view);
        entityManager.flush();

        View found = viewRepository.findAll().get(0);
        assert(found.equals(view));
        assert(found.getDateTime().equals(datetime));
    }

    @Test
    public void whenFindAllEmpty_thenReturnEmptyItemList() {
        List<View> found = viewRepository.findAll();
        assert(found.isEmpty());
    }
}
