package com.example.gildedrose.controller;

import com.example.gildedrose.exceptions.InsufficientStockException;
import com.example.gildedrose.exceptions.ItemNotFoundException;
import com.example.gildedrose.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice
public class CommonControllerAdvice {
    @ResponseBody
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String itemNotFoundHandler(ItemNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String insufficientStockHandler(InsufficientStockException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String unauthorizedUserHandler(UserNotFoundException e) {
        return e.getMessage();
    }
}
