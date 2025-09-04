
package com.example;

import jakarta.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.*;
import java.io.*;
import java.util.stream.IntStream;

public class Main {
    static final FakeDB db = new FakeDB();

    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new HomeServlet()), "/");
        context.addServlet(new ServletHolder(new SubmitServlet()), "/submit");
        context.addServlet(new ServletHolder(new QueryServlet()), "/query");

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
}
