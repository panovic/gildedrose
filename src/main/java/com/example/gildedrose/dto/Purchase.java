package com.example.gildedrose.dto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Purchase {
    private @Id @GeneratedValue Long id;
    private @ManyToOne(fetch=FetchType.LAZY) User user;
    private @ManyToOne(fetch=FetchType.LAZY) Item item;
    private @NotNull Integer price;
    private @NotNull LocalDateTime createdDateTime;

    public Purchase() { }

    public Purchase(User user, Item item, Integer price, LocalDateTime createdDateTime) {
        this.user = user;
        this.item = item;
        this.price = price;
        this.createdDateTime = createdDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        return String.format("Purchase { id=%d, user=%s, item=%s, price=%d, createdDateTime=%s }",
                id, user, item, price, createdDateTime);
    }
}
