package com.recipehub.servlet;

import com.recipehub.dao.RecipeDAO;
import com.recipehub.dao.impl.JdbcRecipeDAO;
import com.recipehub.model.Recipe;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/** Handles recipe discovery requests and forwards data to the JSP view. */
@WebServlet(urlPatterns = {"/", "/recipes"})
public class HomeServlet extends HttpServlet {
    private RecipeDAO recipeDAO;

    @Override public void init() { recipeDAO = new JdbcRecipeDAO(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath != null && servletPath.startsWith("/assets/")) {
            getServletContext().getNamedDispatcher("default").forward(request, response);
            return;
        }

        if (servletPath != null && !servletPath.isEmpty() && !"/".equals(servletPath) && !"/recipes".equals(servletPath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String search = request.getParameter("q");
        String category = request.getParameter("category");
        try {
            List<Recipe> recipes = recipeDAO.findAll(search, category);
            request.setAttribute("recipes", recipes);
            request.setAttribute("search", search == null ? "" : search.trim());
            request.setAttribute("selectedCategory", category == null ? "" : category.trim());

            HttpSession session = request.getSession(false);
            com.recipehub.model.User currentUser = (session != null) ? (com.recipehub.model.User) session.getAttribute("currentUser") : null;
            if (currentUser != null) {
                try {
                    List<Recipe> favs = recipeDAO.findFavoritesByUserId(currentUser.getId());
                    java.util.Set<Long> favoriteIds = new java.util.HashSet<>();
                    for (Recipe r : favs) {
                        favoriteIds.add(r.getId());
                    }
                    request.setAttribute("favoriteIds", favoriteIds);
                    request.setAttribute("favoriteCount", favoriteIds.size());
                } catch (Exception ignored) {}
            }

            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
        } catch (SQLException exception) {
            getServletContext().log("Unable to load recipes", exception);
            request.setAttribute("recipes", Collections.emptyList());
            request.setAttribute("search", search == null ? "" : search.trim());
            request.setAttribute("selectedCategory", category == null ? "" : category.trim());
            request.setAttribute("dbError", exception.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
        }
    }
}

