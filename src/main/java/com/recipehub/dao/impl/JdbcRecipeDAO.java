package com.recipehub.dao.impl;

import com.recipehub.dao.RecipeDAO;
import com.recipehub.model.Recipe;
import com.recipehub.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of RecipeDAO.
 * Employs parameterized SQL, deterministic resource management, and full CRUD capabilities.
 */
public class JdbcRecipeDAO implements RecipeDAO {

    @Override
    public List<Recipe> findAll(String search, String category) throws SQLException {
        String sql = "SELECT r.id, r.title, r.description, r.ingredients, r.instructions, " +
                "r.image_url, c.name AS category, r.prep_minutes, r.cook_minutes, r.servings " +
                "FROM recipes r JOIN categories c ON r.category_id = c.id " +
                "WHERE (? = '' OR r.title LIKE ? OR r.description LIKE ?) " +
                "AND (? = '' OR c.slug = ?) ORDER BY r.created_at DESC";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String term = search == null ? "" : search.trim();
            String categorySlug = category == null ? "" : category.trim();
            statement.setString(1, term);
            statement.setString(2, "%" + term + "%");
            statement.setString(3, "%" + term + "%");
            statement.setString(4, categorySlug);
            statement.setString(5, categorySlug);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapRow(rs));
                }
            }
        }
        return recipes;
    }

    @Override
    public Recipe findById(long id) throws SQLException {
        String sql = "SELECT r.id, r.title, r.description, r.ingredients, r.instructions, " +
                "r.image_url, c.name AS category, r.prep_minutes, r.cook_minutes, r.servings " +
                "FROM recipes r JOIN categories c ON r.category_id = c.id " +
                "WHERE r.id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public long create(Recipe recipe) throws SQLException {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        // Resolve or default category_id
        long categoryId = resolveCategoryId(recipe.getCategory());

        String sql = "INSERT INTO recipes (category_id, title, description, ingredients, instructions, image_url, prep_minutes, cook_minutes, servings) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, categoryId);
            statement.setString(2, recipe.getTitle());
            statement.setString(3, recipe.getDescription() != null ? recipe.getDescription() : "");
            statement.setString(4, recipe.getIngredients() != null ? recipe.getIngredients() : "");
            statement.setString(5, recipe.getInstructions() != null ? recipe.getInstructions() : "");
            statement.setString(6, recipe.getImageUrl() != null ? recipe.getImageUrl() : "");
            statement.setInt(7, recipe.getPrepMinutes());
            statement.setInt(8, recipe.getCookMinutes());
            statement.setInt(9, recipe.getServings());
            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    recipe.setId(generatedId);
                    return generatedId;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean update(Recipe recipe) throws SQLException {
        if (recipe == null || recipe.getId() == 0) {
            return false;
        }

        long categoryId = resolveCategoryId(recipe.getCategory());

        String sql = "UPDATE recipes SET category_id = ?, title = ?, description = ?, ingredients = ?, " +
                "instructions = ?, image_url = ?, prep_minutes = ?, cook_minutes = ?, servings = ? " +
                "WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, categoryId);
            statement.setString(2, recipe.getTitle());
            statement.setString(3, recipe.getDescription() != null ? recipe.getDescription() : "");
            statement.setString(4, recipe.getIngredients() != null ? recipe.getIngredients() : "");
            statement.setString(5, recipe.getInstructions() != null ? recipe.getInstructions() : "");
            statement.setString(6, recipe.getImageUrl() != null ? recipe.getImageUrl() : "");
            statement.setInt(7, recipe.getPrepMinutes());
            statement.setInt(8, recipe.getCookMinutes());
            statement.setInt(9, recipe.getServings());
            statement.setLong(10, recipe.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM recipes WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public List<String> findCategories() throws SQLException {
        String sql = "SELECT name FROM categories ORDER BY name ASC";
        List<String> categories = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        }
        return categories;
    }

    @Override
    public boolean addFavorite(long userId, long recipeId) throws SQLException {
        String sql = "INSERT IGNORE INTO favorites (user_id, recipe_id) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, recipeId);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removeFavorite(long userId, long recipeId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND recipe_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, recipeId);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean isFavorite(long userId, long recipeId) throws SQLException {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND recipe_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, recipeId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Recipe> findFavoritesByUserId(long userId) throws SQLException {
        String sql = "SELECT r.id, r.title, r.description, r.ingredients, r.instructions, " +
                "r.image_url, c.name AS category, r.prep_minutes, r.cook_minutes, r.servings " +
                "FROM recipes r " +
                "JOIN favorites f ON r.id = f.recipe_id " +
                "JOIN categories c ON r.category_id = c.id " +
                "WHERE f.user_id = ? ORDER BY f.created_at DESC";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapRow(rs));
                }
            }
        }
        return recipes;
    }

    @Override
    public List<Recipe> findByUserId(long userId) throws SQLException {
        String sql = "SELECT r.id, r.title, r.description, r.ingredients, r.instructions, " +
                "r.image_url, c.name AS category, r.prep_minutes, r.cook_minutes, r.servings " +
                "FROM recipes r " +
                "JOIN categories c ON r.category_id = c.id " +
                "WHERE r.user_id = ? ORDER BY r.created_at DESC";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapRow(rs));
                }
            }
        }
        return recipes;
    }

    @Override
    public int countFavoritesByUserId(long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public int countRecipesByUserId(long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM recipes WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private long resolveCategoryId(String categoryName) throws SQLException {
        if (categoryName != null && !categoryName.isBlank()) {
            String sql = "SELECT id FROM categories WHERE LOWER(name) = LOWER(?) OR LOWER(slug) = LOWER(?)";
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, categoryName.trim());
                stmt.setString(2, categoryName.trim());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                }
            }
        }
        // Fallback to first available category or id 1
        String fallbackSql = "SELECT id FROM categories ORDER BY id ASC LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fallbackSql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("id");
            }
        }
        return 1L;
    }

    private Recipe mapRow(ResultSet rs) throws SQLException {
        return new Recipe(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("ingredients"),
                rs.getString("instructions"),
                rs.getString("image_url"),
                rs.getString("category"),
                rs.getInt("prep_minutes"),
                rs.getInt("cook_minutes"),
                rs.getInt("servings")
        );
    }
}
