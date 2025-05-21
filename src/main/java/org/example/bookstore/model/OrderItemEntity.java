package org.example.bookstore.model;

public class OrderItemEntity {
    private String bookId;
    private String title; // Store book title at time of purchase
    private int quantity;
    private double unitPrice;

    // Default constructor for JSON deserialization
    public OrderItemEntity() {
    }

    public OrderItemEntity(String bookId, String title, int quantity, double unitPrice) {
        this.bookId = bookId;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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