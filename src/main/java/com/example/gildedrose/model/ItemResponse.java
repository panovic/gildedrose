package com.example.gildedrose.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ItemResponse {
    private @NotNull Long id;
    private @NotNull String name;
    private @NotNull String description;
    private @NotNull @Min(value=1) Integer price;
    private @NotNull @Min(value=1) Integer quantity;

    public ItemResponse() { }

    public ItemResponse(Long id, String name, String description, Integer price, Integer quantity) {
        this.id = id;
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
        return String.format("Item { id=%d, name=%s, description=%s, price=%d, quantity=%d }",
                id, name, description, price, quantity);
    }
}
