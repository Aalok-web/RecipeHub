
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
import java.util.List;

/**
 * Handles the user profile page.
 * Displays the logged-in user's information
 * and the recipes created by that user.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    // DAO object for accessing recipe data from the database.
    private final RecipeDAO recipeDAO = new RecipeDAOImpl();

    /**
     * Processes requests to display the user profile.
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the current session without creating a new one.
        HttpSession session = request.getSession(false);

        // Redirect to login if the user is not authenticated.
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Get the currently logged-in user's information.
        User user = (User) session.getAttribute("user");

        // Fetch all recipes created by this user.
        List<Recipe> recipes =
                recipeDAO.getRecipesByUserId(user.getId());

        // Pass user details and recipes to the profile JSP.
        request.setAttribute("profileUser", user);
        request.setAttribute("userRecipes", recipes);
        request.setAttribute("totalRecipes", recipes.size());

        // Forward the request to the profile page.
        request.getRequestDispatcher("/profile.jsp")
                .forward(request, response);
    }
}