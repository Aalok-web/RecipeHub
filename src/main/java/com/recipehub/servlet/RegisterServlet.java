package com.recipehub.servlet;

import com.recipehub.dao.UserDAO;
import com.recipehub.dao.impl.JdbcUserDAO;
import com.recipehub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new JdbcUserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String tab = request.getParameter("tab");
        if ("login".equalsIgnoreCase(tab)) {
            request.setAttribute("mode", "login");
        } else if ("forgot".equalsIgnoreCase(tab)) {
            request.setAttribute("mode", "forgot");
        } else {
            request.setAttribute("mode", "register");
        }
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("mode", "register");

        // Validation
        if (name == null || name.trim().length() < 2) {
            request.setAttribute("error", "Please enter your full name (minimum 2 characters).");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            request.setAttribute("error", "Please provide a valid email address (e.g., alex@example.com).");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters in length.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "The entered passwords do not match. Please re-enter.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        try {
            if (userDAO.emailExists(email.trim())) {
                request.setAttribute("error", "An account with this email address already exists. Please sign in instead.");
                request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
                return;
            }

            User newUser = userDAO.create(name.trim(), email.trim(), password);
            if (newUser != null) {
                HttpSession session = request.getSession(true);
                request.changeSessionId();
                session.setAttribute("currentUser", newUser);
                session.setAttribute("flashMessage", "Welcome to RecipeHub, " + newUser.getName() + "! Your account has been created.");
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                request.setAttribute("error", "Could not complete account registration. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            getServletContext().log("Database error during registration", e);
            request.setAttribute("error", "Database error occurred during registration. Please verify database connection.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
        }
    }
}
