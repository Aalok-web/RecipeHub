package com.recipehub.dao;

import com.recipehub.model.Recipe;
import com.recipehub.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Handles database operations for adding recipes.
 */
public class RecipeAddDAO {

    /**
     * Inserts a new recipe into the database.
     *
     * @param recipe recipe details provided by the user
     * @return true if the recipe is inserted successfully
     */
    public boolean addRecipe(Recipe recipe) {

        // SQL query to insert recipe details into the recipes table.
        String sql = "INSERT INTO recipes " +
                "(title, description, ingredients, instructions, category, image, cooking_time, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Open the database connection and prepare the insert statement.
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Set recipe details in the same order as the SQL placeholders.
            ps.setString(1, recipe.getTitle());
            ps.setString(2, recipe.getDescription());
            ps.setString(3, recipe.getIngredients());
            ps.setString(4, recipe.getInstructions());
            ps.setString(5, recipe.getCategory());
            ps.setString(6, recipe.getImage());
            ps.setInt(7, recipe.getCookingTime());
            ps.setInt(8, recipe.getUserId());

            // Execute the query and return whether a row was inserted.
            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            // Print database errors to the IntelliJ console.
            e.printStackTrace();
            return false;
        }
    }
}
