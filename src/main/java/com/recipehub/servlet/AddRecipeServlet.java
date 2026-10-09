
package com.recipehub.servlet;

// Import DAO and model classes.
import com.recipehub.dao.RecipeAddDAO;
import com.recipehub.model.Recipe;
import com.recipehub.model.User;

// Import Jakarta Servlet classes.
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles requests to add a new recipe.
 * Checks the user's login session, validates recipe details,
 * and saves the recipe to the database.
 */
@WebServlet("/add-recipe")
public class AddRecipeServlet extends HttpServlet {

    // DAO object used to insert recipes into the database.
    private final RecipeAddDAO recipeAddDAO = new RecipeAddDAO();

    /**
     * Handles form submission when a user adds a recipe.
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Check whether a login session already exists.
        HttpSession session = request.getSession(false);

        // Retrieve the logged-in user from the session.
        User user = (session == null)
                ? null
                : (User) session.getAttribute("user");

        // Redirect users who are not logged in.
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            // Read recipe details submitted through the form.
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String ingredients = request.getParameter("ingredients");
            String instructions = request.getParameter("instructions");
            String category = request.getParameter("category");
            String cookingTimeText = request.getParameter("cooking_time");

            // Validate required fields before saving.
            if (isBlank(title) || isBlank(description)
                    || isBlank(ingredients) || isBlank(instructions)
                    || isBlank(category) || isBlank(cookingTimeText)) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Please fill in all recipe fields.");
                return;
            }

            // Convert the cooking time to an integer.
            int cookingTime = Integer.parseInt(cookingTimeText.trim());

            // Cooking time must be a positive number.
            if (cookingTime < 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Cooking time must be at least 1 minute.");
                return;
            }

            // Create a Recipe object and populate its fields.
            Recipe recipe = new Recipe();
            recipe.setTitle(title.trim());
            recipe.setDescription(description.trim());
            recipe.setIngredients(ingredients.trim());
            recipe.setInstructions(instructions.trim());
            recipe.setCategory(category.trim());
            recipe.setImage("");
            recipe.setCookingTime(cookingTime);

            // Associate the recipe with the logged-in user.
            recipe.setUserId(user.getId());

            // Save the recipe using the DAO.
            boolean success = recipeAddDAO.addRecipe(recipe);

            // Redirect to the recipes page if saving succeeds.
            if (success) {
                response.sendRedirect("recipes");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println(
                        "Recipe could not be added. Check the server console."
                );
            }

        } catch (NumberFormatException e) {
            // Handle invalid cooking-time input.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Please enter a valid cooking time.");
        }
    }

    /**
     * Checks whether a string is null, empty, or only whitespace.
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}