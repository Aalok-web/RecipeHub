package com.recipehub.servlet;

import com.recipehub.dao.UserDAO;
import com.recipehub.dao.UserDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles password recovery requests for RecipeHub users.
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    /**
     * Displays a generic message after receiving a recovery request.
     * Actual email verification and secure password reset will be added next.
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Read and normalize the email entered by the user.
        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect("forgot-password.html?error=invalid");
            return;
        }

        email = email.trim();

        // Do not reveal whether an email address exists in the database.
        request.setAttribute(
                "message",
                "If this email is registered, password recovery instructions will be provided."
        );

        // Show the result on the recovery page.
        request.getRequestDispatcher("/forgot-password-result.jsp")
                .forward(request, response);
    }
}

