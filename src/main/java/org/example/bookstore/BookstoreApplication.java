package org.example.bookstore;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class BookstoreApplication extends ResourceConfig {
    public BookstoreApplication() {
        // Register resource packages
        packages("org.example.bookstore.resource");

        // Register exception mappers
        packages("org.example.bookstore.exception");

        // Register Jackson for JSON handling
        register(JacksonFeature.class);
    }
}
