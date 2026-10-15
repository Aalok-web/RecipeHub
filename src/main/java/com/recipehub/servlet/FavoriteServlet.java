package com.recipehub.servlet;

import com.recipehub.dao.RecipeDAO;
import com.recipehub.dao.impl.JdbcRecipeDAO;
import com.recipehub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles adding and removing recipes from user favorites/saved list.
 * Mapped to /favorite/toggle.
 */
@WebServlet(urlPatterns = {"/favorite/toggle"})
public class FavoriteServlet extends HttpServlet {
    private RecipeDAO recipeDAO;

    @Override
    public void init() {
        recipeDAO = new JdbcRecipeDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        String acceptHeader = request.getHeader("Accept");
        boolean isJsonRequest = acceptHeader != null && acceptHeader.contains("application/json");

        if (sessionUser == null) {
            if (isJsonRequest) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"requiresLogin\":true,\"redirect\":\"" + request.getContextPath() + "/login\"}");
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }

        String recipeIdParam = request.getParameter("recipeId");
        if (recipeIdParam == null || recipeIdParam.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        long recipeId;
        try {
            recipeId = Long.parseLong(recipeIdParam.trim());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            boolean currentlyFavorite = recipeDAO.isFavorite(sessionUser.getId(), recipeId);
            boolean newFavoriteState;

            if (currentlyFavorite) {
                recipeDAO.removeFavorite(sessionUser.getId(), recipeId);
                newFavoriteState = false;
            } else {
                recipeDAO.addFavorite(sessionUser.getId(), recipeId);
                newFavoriteState = true;
            }

            int updatedFavoritesCount = recipeDAO.countFavoritesByUserId(sessionUser.getId());

            if (isJsonRequest) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":true,\"isFavorite\":" + newFavoriteState + ",\"favoritesCount\":" + updatedFavoritesCount + "}");
            } else {
                String referer = request.getHeader("Referer");
                if (referer != null && !referer.isBlank()) {
                    response.sendRedirect(referer);
                } else {
                    response.sendRedirect(request.getContextPath() + "/profile");
                }
            }

        } catch (SQLException e) {
            getServletContext().log("Database error toggling favorite for user " + sessionUser.getId(), e);
            if (isJsonRequest) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\":false,\"error\":\"Database error\"}");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        }
    }
}
