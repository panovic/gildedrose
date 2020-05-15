package com.example.gildedrose.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Item {
    private @Id @GeneratedValue Long id;
    private @NotNull String name;
    private @NotNull String description;
    private @NotNull Integer price;
    private @NotNull Integer quantity;

    public Item() { }

    public Item(String name, String description, Integer price, Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("ItemEntity { id=%d, name=%s, description=%s, price=%d, quantity=%d }",
                id, name, description, price, quantity);
    }
}
