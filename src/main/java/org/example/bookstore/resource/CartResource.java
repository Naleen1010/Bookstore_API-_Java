package org.example.bookstore.resource;

import org.example.bookstore.exception.CartException;
import org.example.bookstore.exception.InputException;
import org.example.bookstore.exception.StockException;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.CartEntity;
import org.example.bookstore.model.CartItemEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
public class CartResource {
    // In-memory storage for shopping carts
    private static final Map<String, CartEntity> carts = new HashMap<>();

    // Helper method to get or create a cart
    private CartEntity getOrCreateCart(String customerId) {
        // Verify customer exists
        CustomerResource.checkCustomerExists(customerId);

        // Get or create cart
        CartEntity cart = carts.get(customerId);
        if (cart == null) {
            cart = new CartEntity(customerId);
            carts.put(customerId, cart);
        }
        return cart;
    }

    @POST
    @Path("/items")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItemToCart(@PathParam("customerId") String customerId, Map<String, Object> item) {
        // Validate input
        if (!item.containsKey("bookId") || !item.containsKey("quantity")) {
            throw new InputException("Both bookId and quantity are required");
        }

        String bookId = item.get("bookId").toString();
        int quantity;
        try {
            quantity = Integer.parseInt(item.get("quantity").toString());
        } catch (NumberFormatException e) {
            throw new InputException("Quantity must be a valid number");
        }

        if (quantity <= 0) {
            throw new InputException("Quantity must be greater than zero");
        }

        // Check book availability
        BookEntity book = BookResource.checkBookAvailability(bookId, quantity);

        // Add item to cart
        CartEntity cart = getOrCreateCart(customerId);
        cart.addItem(bookId, quantity);

        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Response getCart(@PathParam("customerId") String customerId) {
        // Verify customer exists
        CustomerResource.checkCustomerExists(customerId);

        // Get or create cart
        CartEntity cart = getOrCreateCart(customerId);
        
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCartItem(
            @PathParam("customerId") String customerId,
            @PathParam("bookId") String bookId,
            Map<String, Object> update) {

        // Validate input
        if (!update.containsKey("quantity")) {
            throw new InputException("Quantity is required");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(update.get("quantity").toString());
        } catch (NumberFormatException e) {
            throw new InputException("Quantity must be a valid number");
        }

        // Get cart
        CartEntity cart = getOrCreateCart(customerId);

        // Check if the item exists in the cart
        boolean itemExists = false;
        for (CartItemEntity item : cart.getItems()) {
            if (item.getBookId().equals(bookId)) {
                itemExists = true;
                break;
            }
        }

        if (!itemExists && quantity > 0) {
            throw new InputException("Book not in cart");
        }

        // Check stock availability if quantity is increasing
        if (quantity > 0) {
            int currentQuantity = 0;
            for (CartItemEntity item : cart.getItems()) {
                if (item.getBookId().equals(bookId)) {
                    currentQuantity = item.getQuantity();
                    break;
                }
            }

            if (quantity > currentQuantity) {
                BookResource.checkBookAvailability(bookId, quantity - currentQuantity);
            }
        }

        // Update cart
        cart.updateItemQuantity(bookId, quantity);

        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(
            @PathParam("customerId") String customerId,
            @PathParam("bookId") String bookId) {

        // Get cart
        CartEntity cart = getOrCreateCart(customerId);

        // Remove item from cart
        cart.removeItem(bookId);

        return Response.ok(cart).build();
    }

    // Method to check if a cart exists and get it
    public static CartEntity getCartById(String customerId) {
        CartEntity cart = carts.get(customerId);
        if (cart == null) {
            throw new CartException("Cart for customer with ID " + customerId + " not found");
        }
        return cart;
    }

    // Method to clear a cart after order is placed
    public static void clearCart(String customerId) {
        CartEntity cart = carts.get(customerId);
        if (cart != null) {
            cart.clear();
        }
    }
}