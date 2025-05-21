package org.example.bookstore.model;

import org.example.bookstore.resource.BookResource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderEntity {
    private String id;
    private String customerId;
    private List<OrderItemEntity> items;
    private double total;
    private LocalDateTime orderDate;

    // Fixed constructor for JSON deserialization
    public OrderEntity(String orderId, String customerId, Map<String, Integer> orderItems, double totalPrice) {
        this.id = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>();
        this.total = totalPrice;
        this.orderDate = LocalDateTime.now();
        
        // Process the order items if provided
        if (orderItems != null) {
            for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
                String bookId = entry.getKey();
                int quantity = entry.getValue();
                
                // Get book title and price for order item
                BookEntity book = BookResource.checkBookAvailability(bookId, quantity);
                String title = book != null ? book.getTitle() : "Unknown Book";
                double price = book != null ? book.getPrice() : 0.0;
                
                // Create and add order item
                OrderItemEntity item = new OrderItemEntity(bookId, title, quantity, price);
                this.items.add(item);
            }
        }
    }

    public OrderEntity(String id, String customerId) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.orderDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
        calculateTotal();
    }

    public void addItem(OrderItemEntity item) {
        this.items.add(item);
        calculateTotal();
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    private void calculateTotal() {
        this.total = items.stream()
                .mapToDouble(OrderItemEntity::getSubtotal)
                .sum();
    }
}