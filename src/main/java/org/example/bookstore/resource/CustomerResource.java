package org.example.bookstore.resource;

import org.example.bookstore.exception.CustomerException;
import org.example.bookstore.exception.InputException;
import org.example.bookstore.model.CustomerEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    // In-memory storage for customers
    private static final Map<String, CustomerEntity> customers = new HashMap<>();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(CustomerEntity customer) {
        // Validate input
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new InputException("Customer name cannot be empty");
        }
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new InputException("Email cannot be empty");
        }
        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            throw new InputException("Password cannot be empty");
        }

        // Generate ID if not present
        if (customer.getId() == null || customer.getId().isEmpty()) {
            customer.setId(UUID.randomUUID().toString());
        }

        customers.put(customer.getId(), customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public Response getAllCustomers() {
        List<CustomerEntity> customerList = new ArrayList<>(customers.values());
        return Response.ok(customerList).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") String id) {
        CustomerEntity customer = customers.get(id);
        if (customer == null) {
            throw new CustomerException("Customer with ID " + id + " not found");
        }
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("id") String id, CustomerEntity customer) {
        if (!customers.containsKey(id)) {
            throw new CustomerException("Customer with ID " + id + " not found");
        }

        // Validate input
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new InputException("Customer name cannot be empty");
        }
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new InputException("Email cannot be empty");
        }

        customer.setId(id);
        customers.put(id, customer);
        return Response.ok(customer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") String id) {
        if (!customers.containsKey(id)) {
            throw new CustomerException("Customer with ID " + id + " not found");
        }
        customers.remove(id);
        return Response.noContent().build();
    }

    // Method to check if a customer exists
    public static void checkCustomerExists(String customerId) {
        if (!customers.containsKey(customerId)) {
            throw new CustomerException("Customer with ID " + customerId + " not found");
        }
    }
}