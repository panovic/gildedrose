package com.example.gildedrose.repository;

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
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindAll_thenReturnUser() {
        LocalDateTime datetime = LocalDateTime.now();
        String firstName = "First";
        User user = new User(1L, firstName, "Last", "ABC123");
        user = userRepository.save(user);

        User found = userRepository.findAll().get(0);
        assert(found.getFirstName().equals(firstName));
    }

    @Test
    public void whenFindAllEmpty_thenReturnEmptyList() {
        List<User> found = userRepository.findAll();
        assert(found.isEmpty());
    }
}
