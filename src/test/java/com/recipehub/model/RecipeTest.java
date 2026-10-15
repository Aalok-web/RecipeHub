package com.recipehub.model;

import org.junit.Test;

import java.io.*;
import java.util.List;
import static org.junit.Assert.*;

public class RecipeTest {

    @Test
    public void testRecipeFullConstructorAndGetters() {
        Recipe recipe = new Recipe(
                1L,
                "Creamy Tuscan Pasta",
                "Silky garlic cream sauce",
                "Pasta; garlic; cream; spinach",
                "Boil pasta. Sauté garlic. Add cream.",
                "https://example.com/pasta.jpg",
                "Dinner",
                10,
                20,
                4
        );

        assertEquals(1L, recipe.getId());
        assertEquals("Creamy Tuscan Pasta", recipe.getTitle());
        assertEquals("Silky garlic cream sauce", recipe.getDescription());
        assertEquals("Pasta; garlic; cream; spinach", recipe.getIngredients());
        assertEquals("Boil pasta. Sauté garlic. Add cream.", recipe.getInstructions());
        assertEquals("https://example.com/pasta.jpg", recipe.getImageUrl());
        assertEquals("Dinner", recipe.getCategory());
        assertEquals(10, recipe.getPrepMinutes());
        assertEquals(20, recipe.getCookMinutes());
        assertEquals(30, recipe.getTotalMinutes());
        assertEquals(4, recipe.getServings());
    }

    @Test
    public void testIngredientListParsing() {
        Recipe recipe = new Recipe();
        recipe.setIngredients("Pasta; garlic; cream; sun-dried tomatoes; spinach");
        List<String> list = recipe.getIngredientList();

        assertEquals(5, list.size());
        assertEquals("Pasta", list.get(0));
        assertEquals("garlic", list.get(1));
        assertEquals("cream", list.get(2));
        assertEquals("sun-dried tomatoes", list.get(3));
        assertEquals("spinach", list.get(4));
    }

    @Test
    public void testIngredientListWithNewlinesAndEmpty() {
        Recipe recipe = new Recipe();
        recipe.setIngredients("2 cups flour\n1 tsp baking soda\r\n\n1/2 cup butter\n");
        List<String> list = recipe.getIngredientList();

        assertEquals(3, list.size());
        assertEquals("2 cups flour", list.get(0));
        assertEquals("1 tsp baking soda", list.get(1));
        assertEquals("1/2 cup butter", list.get(2));

        recipe.setIngredients(null);
        assertTrue(recipe.getIngredientList().isEmpty());

        recipe.setIngredients("   ");
        assertTrue(recipe.getIngredientList().isEmpty());
    }

    @Test
    public void testInstructionListSentenceParsing() {
        Recipe recipe = new Recipe();
        recipe.setInstructions("Boil pasta. Sauté garlic and tomatoes. Add cream and spinach. Toss with pasta.");
        List<String> steps = recipe.getInstructionList();

        assertEquals(4, steps.size());
        assertEquals("Boil pasta.", steps.get(0));
        assertEquals("Sauté garlic and tomatoes.", steps.get(1));
        assertEquals("Add cream and spinach.", steps.get(2));
        assertEquals("Toss with pasta.", steps.get(3));
    }

    @Test
    public void testInstructionListNewlineParsing() {
        Recipe recipe = new Recipe();
        recipe.setInstructions("Step 1: Prep ingredients.\nStep 2: Cook pasta.\nStep 3: Serve warm.");
        List<String> steps = recipe.getInstructionList();

        assertEquals(3, steps.size());
        assertEquals("Step 1: Prep ingredients.", steps.get(0));
        assertEquals("Step 2: Cook pasta.", steps.get(1));
        assertEquals("Step 3: Serve warm.", steps.get(2));

        recipe.setInstructions(null);
        assertTrue(recipe.getInstructionList().isEmpty());
    }

    @Test
    public void testBoundaryValuesAndValidation() {
        Recipe recipe = new Recipe(2L, "Quick Snack", "Crispy snack", "https://img.com/s.jpg", "Snack", -5, -10);
        assertEquals(0, recipe.getPrepMinutes());
        assertEquals(0, recipe.getCookMinutes());
        assertEquals(2, recipe.getServings()); // default servings for legacy constructor is 2

        recipe.setPrepMinutes(-15);
        assertEquals(0, recipe.getPrepMinutes());

        recipe.setServings(-4);
        assertEquals(1, recipe.getServings());
    }

    @Test
    public void testEqualsHashCodeAndToString() {
        Recipe r1 = new Recipe(10L, "Salad", "Fresh", "img.jpg", "Lunch", 5, 0);
        Recipe r2 = new Recipe(10L, "Salad New", "Fresh New", "img2.jpg", "Lunch", 10, 0);
        Recipe r3 = new Recipe(20L, "Salad", "Fresh", "img.jpg", "Lunch", 5, 0);

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertTrue(r1.toString().contains("Salad"));
    }

    @Test
    public void testSerializationSupport() throws IOException, ClassNotFoundException {
        Recipe recipe = new Recipe(5L, "Pie", "Apple Pie", "flour; apples", "Bake 40m", "pie.jpg", "Dessert", 20, 40, 8);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(recipe);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            Recipe deserialized = (Recipe) ois.readObject();
            assertEquals(recipe.getId(), deserialized.getId());
            assertEquals(recipe.getTitle(), deserialized.getTitle());
            assertEquals(recipe.getTotalMinutes(), deserialized.getTotalMinutes());
        }
    }
}
