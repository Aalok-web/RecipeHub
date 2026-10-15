package com.recipehub.servlet;

import com.recipehub.dao.UserDAO;
import com.recipehub.dao.impl.JdbcUserDAO;
import com.recipehub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new JdbcUserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, redirect to home
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Check for remembered user cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("recipehub_remembered_email".equals(c.getName())) {
                    request.setAttribute("email", c.getValue());
                    request.setAttribute("rememberMe", true);
                    break;
                }
            }
        }

        String tab = request.getParameter("tab");
        if ("register".equalsIgnoreCase(tab)) {
            request.setAttribute("mode", "register");
        } else if ("forgot".equalsIgnoreCase(tab)) {
            request.setAttribute("mode", "forgot");
        } else {
            request.setAttribute("mode", "login");
        }
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Please provide both your email address and password.");
            request.setAttribute("email", email);
            request.setAttribute("mode", "login");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        try {
            User user = userDAO.findByEmail(email.trim());
            if (user == null || !userDAO.verifyPassword(password, user.getPasswordHash())) {
                request.setAttribute("error", "Invalid email address or password. Please try again.");
                request.setAttribute("email", email.trim());
                request.setAttribute("mode", "login");
                request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
                return;
            }

            // Authentication successful: establish session with fixation protection
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute("currentUser", user);

            // Handle Remember Me cookie
            Cookie rememberCookie = new Cookie("recipehub_remembered_email", user.getEmail());
            rememberCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
            rememberCookie.setHttpOnly(true);
            if ("on".equalsIgnoreCase(rememberMe) || "true".equalsIgnoreCase(rememberMe)) {
                rememberCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
            } else {
                rememberCookie.setMaxAge(0); // clear cookie
            }
            response.addCookie(rememberCookie);

            session.setAttribute("flashMessage", "Welcome back, " + user.getName() + "!");
            response.sendRedirect(request.getContextPath() + "/");

        } catch (SQLException e) {
            getServletContext().log("Database error during login", e);
            request.setAttribute("error", "Unable to log in due to a database error. Please check database connection.");
            request.setAttribute("email", email);
            request.setAttribute("mode", "login");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
        }
    }
}
