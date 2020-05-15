package com.example.gildedrose.model;

import javax.validation.constraints.NotNull;

public class PurchaseRequest {
    private @NotNull Long itemId;
    private @NotNull Integer itemQuantity;

    public PurchaseRequest() { }

    public PurchaseRequest(Long itemId, Integer itemQuantity) {
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

}
