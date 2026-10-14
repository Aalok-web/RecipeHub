package com.recipehub.dao;

import com.recipehub.model.Recipe;
import com.recipehub.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements database operations for recipes.
 * Supports fetching, filtering, editing, deleting,
 * and retrieving recipes created by a specific user.
 */
public class RecipeDAOImpl implements RecipeDAO {

    /**
     * Converts the current database row into a Recipe object.
     */
    private Recipe mapRecipe(ResultSet rs) throws Exception {

        Recipe recipe = new Recipe();

        recipe.setId(rs.getInt("id"));
        recipe.setTitle(rs.getString("title"));
        recipe.setDescription(rs.getString("description"));
        recipe.setIngredients(rs.getString("ingredients"));
        recipe.setInstructions(rs.getString("instructions"));
        recipe.setCategory(rs.getString("category"));
        recipe.setImage(rs.getString("image"));
        recipe.setCookingTime(rs.getInt("cooking_time"));
        recipe.setUserId(rs.getInt("user_id"));

        return recipe;
    }

    /**
     * Fetches all recipes from the database.
     */
    @Override
    public List<Recipe> getAllRecipes() {

        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                recipes.add(mapRecipe(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }

    /**
     * Fetches recipes matching a particular category.
     */
    @Override
    public List<Recipe> getRecipesByCategory(String category) {

        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes WHERE category = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapRecipe(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }

    /**
     * Fetches a single recipe using its unique ID.
     */
    @Override
    public Recipe getRecipeById(int id) {

        String sql = "SELECT * FROM recipes WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRecipe(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates an existing recipe's details.
     */
    @Override
    public boolean updateRecipe(Recipe recipe) {

        String sql = "UPDATE recipes SET title = ?, description = ?, "
                + "ingredients = ?, instructions = ?, category = ?, "
                + "image = ?, cooking_time = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, recipe.getTitle());
            ps.setString(2, recipe.getDescription());
            ps.setString(3, recipe.getIngredients());
            ps.setString(4, recipe.getInstructions());
            ps.setString(5, recipe.getCategory());
            ps.setString(6, recipe.getImage());
            ps.setInt(7, recipe.getCookingTime());
            ps.setInt(8, recipe.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a recipe using its unique ID.
     */
    @Override
    public boolean deleteRecipe(int id) {

        String sql = "DELETE FROM recipes WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches recipes created by a particular user.
     */
    @Override
    public List<Recipe> getRecipesByUserId(int userId) {

        List<Recipe> recipes = new ArrayList<>();

        String sql = "SELECT * FROM recipes WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Select recipes belonging to this user.
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                // Convert each matching row into a Recipe object.
                while (rs.next()) {
                    recipes.add(mapRecipe(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }
}