package org.example.bookstore.model;

public class CartItemEntity {
    private String bookId;
    private int quantity;
    private double unitPrice;

    // Default constructor for JSON deserialization
    public CartItemEntity() {
    }

    public CartItemEntity(String bookId, int quantity, double unitPrice) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return quantity * unitPrice;
    }
}