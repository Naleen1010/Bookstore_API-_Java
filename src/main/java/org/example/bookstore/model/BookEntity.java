package org.example.bookstore.model;

import java.util.Objects;

public class BookEntity {
    private String id;
    private String title;
    private String isbn;
    private int publicationYear;
    private double price;
    private int stockQuantity;
    private String authorId;

    // Default constructor for JSON deserialization
    public BookEntity() {
    }

    public BookEntity(String id, String title, String isbn, int publicationYear,
                double price, int stockQuantity, String authorId) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.authorId = authorId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity book = (BookEntity) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
