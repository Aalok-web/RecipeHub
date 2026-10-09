package com.recipehub.dao;

import com.recipehub.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    public List<com.recipehub.model.Recipe> getFavorites(int userId) {

        List<com.recipehub.model.Recipe> favorites = new ArrayList<>();

        String sql = "SELECT r.* FROM recipes r " +
                "JOIN favorites f ON r.id = f.recipe_id " +
                "WHERE f.user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                com.recipehub.model.Recipe recipe =
                        new com.recipehub.model.Recipe();

                recipe.setId(rs.getInt("id"));
                recipe.setTitle(rs.getString("title"));
                recipe.setDescription(rs.getString("description"));
                recipe.setIngredients(rs.getString("ingredients"));
                recipe.setInstructions(rs.getString("instructions"));
                recipe.setCategory(rs.getString("category"));
                recipe.setImage(rs.getString("image"));
                recipe.setCookingTime(rs.getInt("cooking_time"));
                recipe.setUserId(rs.getInt("user_id"));

                favorites.add(recipe);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return favorites;
    }

    public boolean addFavorite(int userId, int recipeId) {

        String sql = "INSERT IGNORE INTO favorites (user_id, recipe_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, recipeId);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}