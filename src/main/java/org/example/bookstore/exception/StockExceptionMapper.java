package org.example.bookstore.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class StockExceptionMapper implements ExceptionMapper<StockException> {
    @Override
    public Response toResponse(StockException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Item out of stock");
        error.put("message", exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}