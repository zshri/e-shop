package com.example.eshop.model.exception;

public class CartIsEmptyException extends RuntimeException{

    public CartIsEmptyException(String message) {
        super(message);
    }
}
