package com.recipehub.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a recipe in RecipeHub.
 * Implements Serializable for HTTP session and caching support.
 */
public class Recipe implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private String imageUrl;
    private String category;
    private int prepMinutes;
    private int cookMinutes;
    private int servings = 2;

    public Recipe() {}

    public Recipe(long id, String title, String description, String imageUrl,
                  String category, int prepMinutes, int cookMinutes) {
        this(id, title, description, "", "", imageUrl, category, prepMinutes, cookMinutes, 2);
    }

    public Recipe(long id, String title, String description, String ingredients,
                  String instructions, String imageUrl, String category,
                  int prepMinutes, int cookMinutes, int servings) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.category = category;
        this.prepMinutes = Math.max(0, prepMinutes);
        this.cookMinutes = Math.max(0, cookMinutes);
        this.servings = servings > 0 ? servings : 1;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getPrepMinutes() { return prepMinutes; }
    public void setPrepMinutes(int prepMinutes) { this.prepMinutes = Math.max(0, prepMinutes); }

    public int getCookMinutes() { return cookMinutes; }
    public void setCookMinutes(int cookMinutes) { this.cookMinutes = Math.max(0, cookMinutes); }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = Math.max(1, servings); }

    public int getTotalMinutes() { return prepMinutes + cookMinutes; }

    /** Returns ingredients as a clean list of individual items. */
    public List<String> getIngredientList() {
        if (ingredients == null || ingredients.isBlank()) {
            return Collections.emptyList();
        }
        String[] parts = ingredients.split("[;\\r\\n]+");
        List<String> list = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                list.add(trimmed);
            }
        }
        return list;
    }

    /** Returns instructions broken down into distinct steps. */
    public List<String> getInstructionList() {
        if (instructions == null || instructions.isBlank()) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        String[] lines = instructions.split("\\r?\\n");
        if (lines.length > 1) {
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    list.add(trimmed);
                }
            }
        } else {
            // Split sentences if formatted in a single paragraph
            String[] sentences = instructions.split("(?<=[.!?])\\s+");
            for (String sentence : sentences) {
                String trimmed = sentence.trim();
                if (!trimmed.isEmpty()) {
                    list.add(trimmed);
                }
            }
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        return id != 0 && id == recipe.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", prepMinutes=" + prepMinutes +
                ", cookMinutes=" + cookMinutes +
                ", servings=" + servings +
                '}';
    }
}
