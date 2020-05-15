package com.example.gildedrose.exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(long id) {
        super("Insufficient stock for item: " + id);
    }
}
