
package com.recipehub.servlet;

// Import DAO classes to retrieve recipes from the database.
import com.recipehub.dao.RecipeDAO;
import com.recipehub.dao.RecipeDAOImpl;

// Import the Recipe model class.
import com.recipehub.model.Recipe;

// Import Jakarta Servlet classes.
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * RecipeServlet handles requests to display all recipes.
 * The servlet retrieves recipes from the database
 * and forwards them to recipes.jsp for display.
 */
@WebServlet("/recipes")
public class RecipeServlet extends HttpServlet {

    // DAO object responsible for database operations.
    private final RecipeDAO recipeDAO = new RecipeDAOImpl();

    /**
     * Handles HTTP GET requests for the recipes page.
     *
     * @param request  contains the incoming request
     * @param response contains the outgoing response
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Fetch all available recipes from the database.
        List<Recipe> recipes = recipeDAO.getAllRecipes();

        // Make the recipe list accessible to recipes.jsp.
        request.setAttribute("recipes", recipes);

        // Forward the request to the JSP page for display.
        request.getRequestDispatcher("/recipes.jsp")
                .forward(request, response);
    }
}