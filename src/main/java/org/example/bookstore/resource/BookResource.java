package org.example.bookstore.resource;

import org.example.bookstore.exception.BookException;
import org.example.bookstore.exception.InputException;
import org.example.bookstore.exception.StockException;
import org.example.bookstore.model.BookEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {
    // In-memory storage for books
    private static final Map<String, BookEntity> books = new HashMap<>();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBook(BookEntity book) {
        // Validate input
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InputException("Book title cannot be empty");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new InputException("ISBN cannot be empty");
        }
        if (book.getPrice() <= 0) {
            throw new InputException("Price must be greater than zero");
        }
        if (book.getStockQuantity() < 0) {
            throw new InputException("Stock quantity cannot be negative");
        }

        // Generate ID if not present
        if (book.getId() == null || book.getId().isEmpty()) {
            book.setId(UUID.randomUUID().toString());
        }

        books.put(book.getId(), book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public Response getAllBooks() {
        List<BookEntity> bookList = new ArrayList<>(books.values());
        return Response.ok(bookList).build();
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") String id) {
        BookEntity book = books.get(id);
        if (book == null) {
            throw new BookException("Book with ID " + id + " not found");
        }
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") String id, BookEntity book) {
        if (!books.containsKey(id)) {
            throw new BookException("Book with ID " + id + " not found");
        }

        // Validate input
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InputException("Book title cannot be empty");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new InputException("ISBN cannot be empty");
        }
        if (book.getPrice() <= 0) {
            throw new InputException("Price must be greater than zero");
        }
        if (book.getStockQuantity() < 0) {
            throw new InputException("Stock quantity cannot be negative");
        }

        book.setId(id);
        books.put(id, book);
        return Response.ok(book).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") String id) {
        if (!books.containsKey(id)) {
            throw new BookException("Book with ID " + id + " not found");
        }
        books.remove(id);
        return Response.noContent().build();
    }

    // Method to check if a book exists and has sufficient stock
    public static BookEntity checkBookAvailability(String bookId, int requestedQuantity) {
        BookEntity book = books.get(bookId);
        if (book == null) {
            throw new BookException("Book with ID " + bookId + " not found");
        }
        if (book.getStockQuantity() < requestedQuantity) {
            throw new StockException("Book with ID " + bookId + " has insufficient stock");
        }
        return book;
    }

    // Method to update stock quantity
    public static void updateBookStock(String bookId, int quantityChange) {
        BookEntity book = books.get(bookId);
        if (book != null) {
            book.setStockQuantity(book.getStockQuantity() - quantityChange);
        }
    }

    // Method to get book price for order calculations
    public static double getBookPrice(String bookId) {
        BookEntity book = books.get(bookId);
        return book != null ? book.getPrice() : 0;
    }

    // Method to get books by author
    public static List<BookEntity> getBooksByAuthor(String authorId) {
        List<BookEntity> authorBooks = new ArrayList<>();
        for (BookEntity book : books.values()) {
            if (book.getAuthorId() != null && book.getAuthorId().equals(authorId)) {
                authorBooks.add(book);
            }
        }
        return authorBooks;
    }
}