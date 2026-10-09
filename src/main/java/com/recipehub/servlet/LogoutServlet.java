
package com.recipehub.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles user logout by ending the current session.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Ends the user's session and redirects to the login page.
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Get the existing session without creating a new one.
        HttpSession session = request.getSession(false);

        // Invalidate the session if one exists.
        if (session != null) {
            session.invalidate();
        }

        // Prevent the browser from showing cached private pages.
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Redirect the user to the login page.
        response.sendRedirect("login.html?logout=success");
    }
}