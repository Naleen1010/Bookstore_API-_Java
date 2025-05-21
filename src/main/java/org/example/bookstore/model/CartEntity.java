package org.example.bookstore.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.bookstore.resource.BookResource;

public class CartEntity {
    private String customerId;
    private Map<String, CartItemEntity> items;
    private double total;

    // Default constructor
    public CartEntity(String customerId) {
        this.customerId = customerId;
        this.items = new HashMap<>();
        this.total = 0.0;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<CartItemEntity> getItems() {
        return new ArrayList<>(items.values());
    }

    public void addItem(CartItemEntity item) {
        String bookId = item.getBookId();

        if (items.containsKey(bookId)) {
            // Update existing item quantity
            CartItemEntity existingItem = items.get(bookId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            // Add new item
            items.put(bookId, item);
        }

        // Recalculate total
        calculateTotal();
    }

    public void updateItem(String bookId, int quantity) {
        if (items.containsKey(bookId)) {
            CartItemEntity item = items.get(bookId);
            item.setQuantity(quantity);

            // Remove item if quantity is 0
            if (quantity <= 0) {
                items.remove(bookId);
            }

            // Recalculate total
            calculateTotal();
        }
    }

    public void removeItem(String bookId) {
        items.remove(bookId);
        calculateTotal();
    }

    public CartItemEntity getItem(String bookId) {
        return items.get(bookId);
    }

    public void clear() {
        items.clear();
        total = 0.0;
    }

    public double getTotal() {
        return total;
    }

    private void calculateTotal() {
        this.total = items.values().stream()
                .mapToDouble(CartItemEntity::getSubtotal)
                .sum();
    }

    // This method was previously empty, now implemented properly
    public void addItem(String bookId, int quantity) {
        // Get book price from BookResource
        double price = BookResource.getBookPrice(bookId);
        
        // Create a new cart item
        CartItemEntity item = new CartItemEntity(bookId, quantity, price);
        
        // Use the existing addItem method to add it to the cart
        addItem(item);
    }

    // This method was previously empty, now implemented properly
    public void updateItemQuantity(String bookId, int quantity) {
        // Use the existing updateItem method
        updateItem(bookId, quantity);
    }
}