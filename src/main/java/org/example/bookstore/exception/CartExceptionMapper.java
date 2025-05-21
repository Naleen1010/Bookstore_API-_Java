package org.example.bookstore.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class CartExceptionMapper implements ExceptionMapper<CartException> {
    @Override
    public Response toResponse(CartException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Cart not found");
        error.put("message", exception.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}