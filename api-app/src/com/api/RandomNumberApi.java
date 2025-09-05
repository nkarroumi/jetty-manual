package com.api;
import jakarta.servlet.http.*;

import java.io.IOException;

import org.eclipse.jetty.ee10.servlet.*;
import org.eclipse.jetty.server.Server;

public class RandomNumberApi {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8082);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new RandomServlet()), "/api/numbers");
    }

    public static class RandomServlet extends HttpServlet {
        protected void doGet(HttpServletRequest req, HttpServletResponse resp ) throws IOException{
            

        }
    }
    
}
