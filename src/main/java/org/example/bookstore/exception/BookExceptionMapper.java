package org.example.bookstore.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class BookExceptionMapper implements ExceptionMapper<BookException> {
    @Override
    public Response toResponse(BookException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Book not found");
        error.put("message", exception.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}