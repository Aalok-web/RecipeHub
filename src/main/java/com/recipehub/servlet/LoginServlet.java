package com.recipehub.servlet;

import com.recipehub.dao.UserDAO;
import com.recipehub.dao.UserDAOImpl;
import com.recipehub.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userDAO.login(email, password);

        if (user != null) {

            // Create a session after successful login.
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            // Redirect to the recipes page.
            response.sendRedirect("recipes");

        } else {
            response.sendRedirect("login.html?error=invalid");
        }
    }
}