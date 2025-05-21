package org.example.bookstore.resource;

import org.example.bookstore.exception.AuthorException;
import org.example.bookstore.exception.InputException;
import org.example.bookstore.model.AuthorEntity;
import org.example.bookstore.model.BookEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
public class AuthorResource {
    // In-memory storage for authors
    private static final Map<String, AuthorEntity> authors = new HashMap<>();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAuthor(AuthorEntity author) {
        // Validate input
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InputException("Author name cannot be empty");
        }

        // Generate ID if not present
        if (author.getId() == null || author.getId().isEmpty()) {
            author.setId(UUID.randomUUID().toString());
        }

        authors.put(author.getId(), author);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public Response getAllAuthors() {
        List<AuthorEntity> authorList = new ArrayList<>(authors.values());
        return Response.ok(authorList).build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthor(@PathParam("id") String id) {
        AuthorEntity author = authors.get(id);
        if (author == null) {
            throw new AuthorException("Author with ID " + id + " not found");
        }
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAuthor(@PathParam("id") String id, AuthorEntity author) {
        if (!authors.containsKey(id)) {
            throw new AuthorException("Author with ID " + id + " not found");
        }

        // Validate input
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InputException("Author name cannot be empty");
        }

        author.setId(id);
        authors.put(id, author);
        return Response.ok(author).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") String id) {
        if (!authors.containsKey(id)) {
            throw new AuthorException("Author with ID " + id + " not found");
        }
        authors.remove(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getAuthorBooks(@PathParam("id") String id) {
        if (!authors.containsKey(id)) {
            throw new AuthorException("Author with ID " + id + " not found");
        }

        List<BookEntity> authorBooks = BookResource.getBooksByAuthor(id);
        return Response.ok(authorBooks).build();
    }

    // Method to check if an author exists
    public static void checkAuthorExists(String authorId) {
        if (authorId != null && !authorId.isEmpty() && !authors.containsKey(authorId)) {
            throw new AuthorException("Author with ID " + authorId + " not found");
        }
    }
}