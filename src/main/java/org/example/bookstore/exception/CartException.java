package org.example.bookstore.exception;

public class CartException extends RuntimeException {
    public CartException(String message) {
        super(message);
    }
}