package com.api;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Random;
import org.eclipse.jetty.ee10.servlet.*;
import org.eclipse.jetty.server.Server;

public class RandomNumberApi {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8082);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new RandomServlet()), "/api/numbers");
        server.start();
        System.out.println("RandomNumberApi running on http://localhost:8082/api/numbers");
        server.join();

    }

    public static class RandomServlet extends HttpServlet {
        protected void doGet(HttpServletRequest req, HttpServletResponse resp ) throws IOException{
            Random rand = new Random();
            int[] numbers = rand.ints(5,1,100).toArray();
            String json = "{ \"numbers\": [" +
                String.join(", ", java.util.Arrays.stream(numbers)
                .mapToObj(String::valueOf).toArray(String[]::new)) + "] }";
                //Curious what this is:
                System.out.println(String.join(", ", java.util.Arrays.stream(numbers)
                .mapToObj(String::valueOf).toArray(String[]::new)));
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        }
    }
    
}
