package org.example.bookstore.exception;

public class CustomerException extends RuntimeException {
    public CustomerException(String message) {
        super(message);
    }
}