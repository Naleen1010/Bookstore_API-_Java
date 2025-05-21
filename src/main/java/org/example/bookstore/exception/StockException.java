package org.example.bookstore.exception;

public class StockException extends RuntimeException {
    public StockException(String message) {
        super(message);
    }
}