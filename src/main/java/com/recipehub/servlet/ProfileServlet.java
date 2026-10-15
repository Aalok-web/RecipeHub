package com.recipehub.servlet;

import com.recipehub.dao.RecipeDAO;
import com.recipehub.dao.UserDAO;
import com.recipehub.dao.impl.JdbcRecipeDAO;
import com.recipehub.dao.impl.JdbcUserDAO;
import com.recipehub.model.Recipe;
import com.recipehub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Handles User Profile viewing and editing, along with Saved / My Recipes.
 * Protected: Unauthenticated visitors are redirected to /login.
 */
@WebServlet(urlPatterns = {"/profile", "/profile/edit"})
public class ProfileServlet extends HttpServlet {
    private UserDAO userDAO;
    private RecipeDAO recipeDAO;

    @Override
    public void init() {
        userDAO = new JdbcUserDAO();
        recipeDAO = new JdbcRecipeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Load latest profile data directly from database
            User user = userDAO.findById(sessionUser.getId());
            if (user == null) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Sync session
            session.setAttribute("currentUser", user);

            // Fetch user's saved/favorite recipes and contributed recipes
            List<Recipe> savedRecipes = recipeDAO.findFavoritesByUserId(user.getId());
            List<Recipe> myRecipes = recipeDAO.findByUserId(user.getId());

            request.setAttribute("profileUser", user);
            request.setAttribute("savedRecipes", savedRecipes != null ? savedRecipes : Collections.emptyList());
            request.setAttribute("myRecipes", myRecipes != null ? myRecipes : Collections.emptyList());
            request.setAttribute("savedCount", savedRecipes != null ? savedRecipes.size() : 0);
            request.setAttribute("contributedCount", myRecipes != null ? myRecipes.size() : 0);

            // Check if edit mode requested via query param e.g. /profile?edit=true
            String editParam = request.getParameter("edit");
            request.setAttribute("isEditMode", "true".equalsIgnoreCase(editParam) || request.getServletPath().endsWith("/edit"));

            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Database error loading user profile", e);
            request.setAttribute("error", "Unable to load profile data due to database error.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String bio = request.getParameter("bio");

        if (name == null || name.trim().length() < 2) {
            request.setAttribute("error", "Name must be at least 2 characters long.");
            doGet(request, response);
            return;
        }

        if (bio != null && bio.trim().length() > 300) {
            request.setAttribute("error", "Bio cannot exceed 300 characters.");
            doGet(request, response);
            return;
        }

        try {
            boolean updated = userDAO.updateProfile(sessionUser.getId(), name.trim(), bio != null ? bio.trim() : "");
            if (updated) {
                User refreshed = userDAO.findById(sessionUser.getId());
                session.setAttribute("currentUser", refreshed);
                session.setAttribute("flashMessage", "Your profile details have been updated!");
                response.sendRedirect(request.getContextPath() + "/profile");
            } else {
                request.setAttribute("error", "Could not update profile. Please try again.");
                doGet(request, response);
            }
        } catch (SQLException e) {
            getServletContext().log("Database error updating profile", e);
            request.setAttribute("error", "Database error occurred while updating profile.");
            doGet(request, response);
        }
    }
}
