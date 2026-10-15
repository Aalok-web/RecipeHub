package com.recipehub.servlet;

import com.recipehub.dao.RecipeDAO;
import com.recipehub.dao.impl.JdbcRecipeDAO;
import com.recipehub.model.Recipe;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles single recipe detail view requests and forwards to the recipe-detail JSP view.
 * Mapped to /recipe (e.g. /recipe?id=1).
 */
@WebServlet(urlPatterns = {"/recipe"})
public class RecipeDetailServlet extends HttpServlet {
    private RecipeDAO recipeDAO;

    @Override
    public void init() {
        recipeDAO = new JdbcRecipeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/recipes");
            return;
        }

        long id;
        try {
            id = Long.parseLong(idParam.trim());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("errorMessage", "The requested recipe ID is invalid.");
            request.setAttribute("notFound", true);
            request.getRequestDispatcher("/WEB-INF/views/recipe-detail.jsp").forward(request, response);
            return;
        }

        try {
            Recipe recipe = recipeDAO.findById(id);
            if (recipe == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("errorMessage", "We couldn't find a recipe with ID #" + id + ".");
                request.setAttribute("notFound", true);
                request.getRequestDispatcher("/WEB-INF/views/recipe-detail.jsp").forward(request, response);
                return;
            }

            request.setAttribute("recipe", recipe);

            // Count views using an application-scoped, thread-safe counter.
            Object tracker = getServletContext().getAttribute("recipeViewTracker");
            if (tracker instanceof com.recipehub.service.RecipeViewTracker) {
                com.recipehub.service.RecipeViewTracker viewTracker =
                        (com.recipehub.service.RecipeViewTracker) tracker;
                viewTracker.recordView(recipe.getId());
                request.setAttribute("recipeViewCount", viewTracker.getViewCount(recipe.getId()));
            }

            jakarta.servlet.http.HttpSession session = request.getSession(false);
            com.recipehub.model.User currentUser = (session != null) ? (com.recipehub.model.User) session.getAttribute("currentUser") : null;
            if (currentUser != null) {
                try {
                    boolean isFav = recipeDAO.isFavorite(currentUser.getId(), id);
                    request.setAttribute("isFavorite", isFav);
                } catch (Exception ignored) {}
            }

            // Fetch a few related recipes to enrich the detail page
            try {
                List<Recipe> all = recipeDAO.findAll("", "");
                List<Recipe> related = new ArrayList<>();
                for (Recipe r : all) {
                    if (r.getId() != id) {
                        related.add(r);
                        if (related.size() >= 3) {
                            break;
                        }
                    }
                }
                request.setAttribute("relatedRecipes", related);
            } catch (Exception ignored) {
                // Secondary related recipes failure should not block the main recipe view
            }

            request.getRequestDispatcher("/WEB-INF/views/recipe-detail.jsp").forward(request, response);
        } catch (SQLException exception) {
            getServletContext().log("Unable to load recipe detail for id=" + id, exception);
            request.setAttribute("notFound", true);
            request.setAttribute("errorMessage", "Unable to load recipe: database connection error. Please verify database configuration.");
            request.getRequestDispatcher("/WEB-INF/views/recipe-detail.jsp").forward(request, response);
        }
    }
}
