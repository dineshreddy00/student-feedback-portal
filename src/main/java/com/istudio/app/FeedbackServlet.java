package com.istudio.app;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Feedback Submitted</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Feedback Submitted Successfully!</h1>");
        out.println("<p>Thank you, " + name + ".</p>");
        out.println("<p>Your feedback has been received.</p>");
        out.println("<a href='index.html'>Submit another feedback</a>");
        out.println("</body>");
        out.println("</html>");
    }
}
