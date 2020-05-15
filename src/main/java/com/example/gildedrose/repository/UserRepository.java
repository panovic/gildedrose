package com.example.gildedrose.repository;

import com.example.gildedrose.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findDistinctByApiKey(String apiKey);
}
