package org.example.bookstore;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
    public static void main(String[] args) throws Exception {
        // Set default port or use environment variable
        int port = System.getenv("PORT") != null ?
                Integer.parseInt(System.getenv("PORT")) : 8083;

        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder servletHolder = new ServletHolder(new ServletContainer(new BookstoreApplication()));
        context.addServlet(servletHolder, "/*");

        System.out.println("Starting server on port " + port);
        server.start();
        server.join();
    }
}