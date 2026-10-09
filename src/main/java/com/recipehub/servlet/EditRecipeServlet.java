
package com.recipehub.servlet;

import com.recipehub.dao.RecipeDAO;
import com.recipehub.dao.RecipeDAOImpl;
import com.recipehub.model.Recipe;
import com.recipehub.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles requests to edit an existing recipe.
 */
@WebServlet("/edit-recipe")
public class EditRecipeServlet extends HttpServlet {

    // DAO used to retrieve and update recipe records.
    private final RecipeDAO recipeDAO = new RecipeDAOImpl();

    /**
     * Displays the selected recipe in the edit form.
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Only logged-in users can access recipe editing.
        HttpSession session = request.getSession(false);
        User user = (session == null)
                ? null
                : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Read the recipe ID from the request URL.
        int id;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid recipe ID.");
            return;
        }

        // Retrieve the recipe from the database.
        Recipe recipe = recipeDAO.getRecipeById(id);

        if (recipe == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Recipe not found.");
            return;
        }

        // Ensure users can edit only their own recipes.
        if (recipe.getUserId() != user.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "You can edit only your own recipes.");
            return;
        }

        // Make the recipe available to the edit JSP page.
        request.setAttribute("recipe", recipe);

        // Display the edit form.
        request.getRequestDispatcher("/edit-recipe.jsp")
                .forward(request, response);
    }

    /**
     * Saves the edited recipe details.
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Verify that the user is logged in.
        HttpSession session = request.getSession(false);
        User user = (session == null)
                ? null
                : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            // Read the recipe ID and updated form values.
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String ingredients = request.getParameter("ingredients");
            String instructions = request.getParameter("instructions");
            String category = request.getParameter("category");
            String cookingTimeText = request.getParameter("cooking_time");

            // Validate required fields.
            if (isBlank(title) || isBlank(description)
                    || isBlank(ingredients) || isBlank(instructions)
                    || isBlank(category) || isBlank(cookingTimeText)) {

                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Please fill in all recipe fields.");
                return;
            }

            int cookingTime = Integer.parseInt(cookingTimeText);

            if (cookingTime < 1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Cooking time must be at least 1 minute.");
                return;
            }

            // Retrieve the original recipe before updating it.
            Recipe existingRecipe = recipeDAO.getRecipeById(id);

            if (existingRecipe == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Recipe not found.");
                return;
            }

            // Prevent users from modifying another user's recipe.
            if (existingRecipe.getUserId() != user.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "You can edit only your own recipes.");
                return;
            }

            // Update the recipe with the submitted values.
            existingRecipe.setTitle(title.trim());
            existingRecipe.setDescription(description.trim());
            existingRecipe.setIngredients(ingredients.trim());
            existingRecipe.setInstructions(instructions.trim());
            existingRecipe.setCategory(category.trim());
            existingRecipe.setCookingTime(cookingTime);

            // Save changes to the database.
            boolean updated = recipeDAO.updateRecipe(existingRecipe);

            if (updated) {
                response.sendRedirect("recipes");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Recipe could not be updated.");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid recipe ID or cooking time.");
        }
    }

    /**
     * Checks whether a string is null, empty, or whitespace only.
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}