package org.example.bookstore.resource;

import org.example.bookstore.exception.CustomerException;
import org.example.bookstore.exception.InputException;
import org.example.bookstore.exception.StockException;
import org.example.bookstore.model.BookEntity;
import org.example.bookstore.model.CartEntity;
import org.example.bookstore.model.CartItemEntity;
import org.example.bookstore.model.OrderEntity;
import org.example.bookstore.model.OrderItemEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    // In-memory storage for orders, with customer ID as the key
    private static final Map<String, List<OrderEntity>> customerOrders = new HashMap<>();
    // In-memory storage for all orders with order ID as the key (for fast lookup)
    private static final Map<String, OrderEntity> allOrders = new HashMap<>();

    @POST
    public Response createOrder(@PathParam("customerId") String customerId) {
        try {
            // Verify customer exists
            CustomerResource.checkCustomerExists(customerId);

            // Get the customer's cart
            CartEntity cart = CartResource.getCartById(customerId);

            // Validate cart is not empty
            if (cart.getItems().isEmpty()) {
                throw new InputException("Cannot create order with empty cart");
            }

            // Check stock availability for all items and calculate total price
            double totalPrice = 0;
            Map<String, Integer> orderItems = new HashMap<>();

            // Convert List<CartItemEntity> to Map<String, Integer> for order creation
            for (CartItemEntity item : cart.getItems()) {
                String bookId = item.getBookId();
                int quantity = item.getQuantity();
                orderItems.put(bookId, quantity);
                
                // Check book availability
                BookEntity book = BookResource.checkBookAvailability(bookId, quantity);

                // Update stock
                BookResource.updateBookStock(bookId, quantity);

                // Add to total price
                totalPrice += book.getPrice() * quantity;
            }

            // Create order
            String orderId = UUID.randomUUID().toString();
            OrderEntity order = new OrderEntity(orderId, customerId, orderItems, totalPrice);

            // Store order
            if (!customerOrders.containsKey(customerId)) {
                customerOrders.put(customerId, new ArrayList<>());
            }
            customerOrders.get(customerId).add(order);
            allOrders.put(orderId, order);

            // Clear cart
            CartResource.clearCart(customerId);

            return Response.status(Response.Status.CREATED).entity(order).build();
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            
            // Re-throw the original exception if it's one of our custom exceptions
            if (e instanceof CustomerException || e instanceof InputException || e instanceof StockException) {
                throw e;
            }
            
            // Otherwise, wrap it in a generic exception
            throw new WebApplicationException("Error creating order: " + e.getMessage(), 
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    public Response getCustomerOrders(@PathParam("customerId") String customerId) {
        // Verify customer exists
        CustomerResource.checkCustomerExists(customerId);

        List<OrderEntity> orders = customerOrders.getOrDefault(customerId, new ArrayList<>());
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrder(
            @PathParam("customerId") String customerId,
            @PathParam("orderId") String orderId) {

        // Verify customer exists
        CustomerResource.checkCustomerExists(customerId);

        // Get order
        OrderEntity order = allOrders.get(orderId);
        if (order == null || !order.getCustomerId().equals(customerId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(order).build();
    }
}