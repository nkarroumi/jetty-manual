
package com.example;

import jakarta.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    static final FakeDB db = new FakeDB();

    public static void main(String[] args) throws Exception {
        //Convenience constructor Creates server and a ServerConnector at the passed port.
        Server server = new Server(8081);
        
        
        /*ServletContextHandler vs ServletHandler in Jetty
            Jetty provides two key classes for handling servlets: ServletContextHandler and ServletHandler. 
            While they may seem similar, they serve different purposes and are used in distinct scenarios. Here's a breakdown:
            1. ServletContextHandler
            Purpose: Manages a complete ServletContext for your application.
            Features:
            Creates and manages a shared ServletContext for all servlets, filters, sessions, and security configurations.
            Supports advanced features like session management, context attributes, and lifecycle listeners.
            Ideal for applications requiring a fully functional servlet environment.
            Use Case: When you need a complete web application setup with multiple servlets, filters, and session handling.
           
            Example:
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            context.addServlet(MyServlet.class, "/example");
            server.setHandler(context);
            
            2. ServletHandler
            Purpose: A lightweight handler for managing servlets without a full ServletContext.
            Features:
            Does not create or manage a ServletContext.
            Lacks support for filters, sessions, and other advanced features.
            Suitable for simple setups where you only need to serve basic servlets.
            Use Case: When you need a minimal setup for serving servlets without the overhead of a full servlet environment.
            
            Example:
            ServletHandler handler = new ServletHandler();
            handler.addServletWithMapping(MyServlet.class, "/example");
            server.setHandler(handler);
         */
        ServletContextHandler context = new ServletContextHandler();
        
        /*
         * In simple terms, setContextPath in Jetty is used to define the base URL path for your web application. It determines how users access your app through the browser.
         *  For example:
         *   If you set the context path to /myapp, users will access your app at http://localhost:8080/myapp.
         *  If you set it to /, your app will be accessible directly at http://localhost:8080.
         * It’s like giving your app a specific "address" within the server so it knows where to respond to requests.
         */
        context.setContextPath("/");
        server.setHandler(context);
        /* 
         * Adding the servlets to my contextHandler. This is what links the servlets to my server 
         */
        context.addServlet(new ServletHolder(new HomeServlet()), "/");
        context.addServlet(new ServletHolder(new SubmitServlet()), "/submit");
        context.addServlet(new ServletHolder(new QueryServlet()), "/query");
        context.addServlet(new ServletHolder(new FetchServlet()), "/fetch");

        server.start();
        server.join();
    }

    public static class HomeServlet extends HttpServlet {
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            InputStream in = getClass().getClassLoader().getResourceAsStream("index.html");
            if (in == null) {
                resp.setStatus(404);
                resp.getWriter().write("Form not found");
                return;
            }
            resp.setContentType("text/html");
            in.transferTo(resp.getOutputStream());
        }
    }

    public static class SubmitServlet extends HttpServlet {
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String name = req.getParameter("name");
            int[] nums = IntStream.rangeClosed(1, 5)
                .map(i -> Integer.parseInt(req.getParameter("n" + i)))
                .toArray();

            Record rec = new Record(name, nums);
            db.save(rec);

            resp.setContentType("text/html");
            resp.getWriter().write("<h2>Record Saved</h2>");
            resp.getWriter().write(rec.toHTML());
            resp.getWriter().write("<p><a href='/'>Back</a></p>");
        }
    }

    public static class QueryServlet extends HttpServlet {
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String name = req.getParameter("name");
            Record rec = db.findByName(name);

            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();

            if (rec != null) {
                out.write("<h2>Record Found</h2>");
                out.write(rec.toHTML());
            } else {
                out.write("<p>No record found for name: " + name + "</p>");
            }

            out.write("<p><a href='/'>Back</a></p>");
        }
    }



    public static class FetchServlet extends HttpServlet {
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            URL url = null;
            try {
                URI uri = new URI("http://localhost:8082/api/numbers");
                url = uri.toURL();
            }catch (Exception e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.lines().collect(Collectors.joining());
            reader.close();

            resp.setContentType("application/json");
            resp.getWriter().write(response);
        }
    }
}
