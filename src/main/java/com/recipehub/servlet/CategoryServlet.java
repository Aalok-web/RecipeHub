package com.recipehub.servlet;
import com.recipehub.dao.RecipeDAO;
import com.recipehub.dao.RecipeDAOImpl;
import com.recipehub.model.Recipe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Handles recipe category filtering.
 * Retrieves recipes matching the category selected by the user.
 */
@WebServlet("/category")
public class CategoryServlet extends HttpServlet {

    // DAO used to retrieve recipes from the database.
    private final RecipeDAO recipeDAO = new RecipeDAOImpl();

    /**
     * Handles GET requests for category-based recipe filtering.
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Read the selected category from the request URL.
        String category = request.getParameter("name");

        // Handle a missing or blank category parameter.
        if (category == null || category.trim().isEmpty()) {
            response.sendRedirect("categories.jsp");
            return;
        }

        // Retrieve recipes that belong to the selected category.
        List<Recipe> recipes = recipeDAO.getRecipesByCategory(category.trim());

        // Make the filtered recipe list available to recipes.jsp.
        request.setAttribute("recipes", recipes);
        System.out.println("Selected category: " + category + " | Recipes found: " + recipes.size());

        // Forward the request to the existing recipes display page.
        request.getRequestDispatcher("/recipes.jsp")
                .forward(request, response);
    }
}

