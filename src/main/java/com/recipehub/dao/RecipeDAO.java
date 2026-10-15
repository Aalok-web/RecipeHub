package com.recipehub.dao;

import com.recipehub.model.Recipe;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object contract for Recipe persistence, favorites, and user recipe management.
 */
public interface RecipeDAO {
    List<Recipe> findAll(String search, String category) throws SQLException;
    Recipe findById(long id) throws SQLException;
    long create(Recipe recipe) throws SQLException;
    boolean update(Recipe recipe) throws SQLException;
    boolean delete(long id) throws SQLException;
    List<String> findCategories() throws SQLException;

    // Favorites & User Recipes
    boolean addFavorite(long userId, long recipeId) throws SQLException;
    boolean removeFavorite(long userId, long recipeId) throws SQLException;
    boolean isFavorite(long userId, long recipeId) throws SQLException;
    List<Recipe> findFavoritesByUserId(long userId) throws SQLException;
    List<Recipe> findByUserId(long userId) throws SQLException;
    int countFavoritesByUserId(long userId) throws SQLException;
    int countRecipesByUserId(long userId) throws SQLException;
}
