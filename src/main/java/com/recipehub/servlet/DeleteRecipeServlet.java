
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
 * Handles deletion of recipes.
 * Only the logged-in owner of a recipe can delete it.
 */
@WebServlet("/delete-recipe")
public class DeleteRecipeServlet extends HttpServlet {

    // DAO used to access recipe records in the database.
    private final RecipeDAO recipeDAO = new RecipeDAOImpl();

    /**
     * Processes the delete request submitted by the user.
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the existing login session.
        HttpSession session = request.getSession(false);

        // Get the currently logged-in user.
        User user = (session == null)
                ? null
                : (User) session.getAttribute("user");

        // Only logged-in users may delete recipes.
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Read and validate the recipe ID.
        int id;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid recipe ID.");
            return;
        }

        // Fetch the recipe to verify its owner.
        Recipe recipe = recipeDAO.getRecipeById(id);

        if (recipe == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Recipe not found.");
            return;
        }

        // Prevent deletion of another user's recipe.
        if (recipe.getUserId() != user.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "You can delete only your own recipes.");
            return;
        }

        // Delete the recipe from the database.
        boolean deleted = recipeDAO.deleteRecipe(id);

        if (deleted) {
            // Return to the recipes page after successful deletion.
            response.sendRedirect("recipes");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Recipe could not be deleted.");
        }
    }
}