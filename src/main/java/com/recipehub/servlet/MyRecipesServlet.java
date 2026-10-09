
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
 * Handles the My Recipes page.
 * Displays only the recipes created by the logged-in user.
 */
@WebServlet("/my-recipes")
public class MyRecipesServlet extends HttpServlet {

    // DAO object used to perform recipe-related database operations.
    private final RecipeDAO recipeDAO = new RecipeDAOImpl();

    /**
     * Handles GET requests for the My Recipes page.
     *
     * @param request  contains the incoming request data
     * @param response sends the response to the client
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException      if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the existing session without creating a new one.
        HttpSession session = request.getSession(false);

        // Redirect users to the login page if they are not logged in.
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Retrieve the currently logged-in user from the session.
        User user = (User) session.getAttribute("user");

        // Fetch only the recipes created by this user's ID.
        List<Recipe> recipes =
                recipeDAO.getRecipesByUserId(user.getId());

        // Make the recipe list available to the JSP page.
        request.setAttribute("recipes", recipes);

        // Forward the request to the My Recipes page for display.
        request.getRequestDispatcher("/my-recipes.jsp")
                .forward(request, response);
    }
}