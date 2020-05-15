package com.example.gildedrose.exceptions;

public class ItemNotFoundException  extends RuntimeException {
    public ItemNotFoundException(long id) {
        super("Could not find item: " + id);
    }
}
