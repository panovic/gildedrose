package com.example.gildedrose.configuration;

import com.example.gildedrose.dto.Item;
import com.example.gildedrose.dto.User;
import com.example.gildedrose.repository.ItemRepository;
import com.example.gildedrose.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class LoadDatabase {
    private static final Logger log = Logger.getLogger(LoadDatabase.class.getSimpleName());

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo, ItemRepository itemRepo) {
        return args -> {
            log.info("Preloading " + userRepo.save(new User(1L, "Mobile", "Integration", "5af08d534f904e84803c1942473b453d")));
            log.info("Preloading " + itemRepo.save(new Item("Belgian Moon", "Belgian White Wheat Ale", 10, 100)));
            log.info("Preloading " + itemRepo.save(new Item("Stella Artois", "Belgian Lager", 11, 100)));
            log.info("Preloading " + itemRepo.save(new Item("Big Rock Honey Brown", "Honey Brown Amber Lager", 8, 100)));
        };
    }
}
