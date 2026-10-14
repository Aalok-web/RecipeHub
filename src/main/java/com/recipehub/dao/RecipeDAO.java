package com.recipehub.dao;

import com.recipehub.model.Recipe;
import java.util.List;

/**
 * Defines database operations for recipes.
 */
public interface RecipeDAO {

    // Fetch all recipes.
    List<Recipe> getAllRecipes();

    // Fetch recipes by category.
    List<Recipe> getRecipesByCategory(String category);

    // Fetch one recipe by its ID.
    Recipe getRecipeById(int id);

    // Update an existing recipe.
    boolean updateRecipe(Recipe recipe);

    // Delete a recipe by its ID.
    boolean deleteRecipe(int id);

    // Fetch recipes created by a particular user.
    List<Recipe> getRecipesByUserId(int userId);
}