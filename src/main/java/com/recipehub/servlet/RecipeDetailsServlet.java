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

@WebServlet("/recipe-details")
public class RecipeDetailsServlet extends HttpServlet {

    private final RecipeDAO recipeDAO = new RecipeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        List<Recipe> recipes = recipeDAO.getAllRecipes();
        Recipe selectedRecipe = null;

        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                selectedRecipe = recipe;
                break;
            }
        }

        request.setAttribute("recipe", selectedRecipe);
        request.getRequestDispatcher("recipe-details.jsp").forward(request, response);
    }
}