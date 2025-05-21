package org.example.bookstore.model;

import java.util.Objects;

public class AuthorEntity {
    private String id;
    private String name;
    private String biography;

    // Default constructor for JSON deserialization
    public AuthorEntity() {
    }

    public AuthorEntity(String id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorEntity author = (AuthorEntity) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}