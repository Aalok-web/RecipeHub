
<%@ page import="com.recipehub.model.Recipe" %>
<%
  // Retrieve the recipe sent by EditRecipeServlet.
  Recipe recipe = (Recipe) request.getAttribute("recipe");

  // Redirect if no recipe was provided.
  if (recipe == null) {
    response.sendRedirect("recipes");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>RecipeHub - Edit Recipe</title>

  <style>
    /* Set the overall page appearance. */
    body {
      margin: 0;
      font-family: Arial, sans-serif;
      background: #f6f7fb;
      color: #1f2937;
    }

    /* Navigation bar styling. */
    nav {
      background: #1f2937;
      padding: 18px 40px;
    }

    nav h2 {
      color: white;
      margin: 0;
    }

    /* Main form container. */
    .container {
      max-width: 650px;
      margin: 35px auto;
      padding: 30px;
      background: white;
      border-radius: 14px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
    }

    h1 {
      text-align: center;
      margin-bottom: 25px;
    }

    /* Form labels and input fields. */
    label {
      display: block;
      margin: 16px 0 7px;
      font-weight: bold;
    }

    input, textarea, select {
      box-sizing: border-box;
      width: 100%;
      padding: 12px;
      border: 1px solid #d1d5db;
      border-radius: 7px;
      font-size: 15px;
    }

    textarea {
      min-height: 100px;
      resize: vertical;
    }

    /* Save button styling. */
    button {
      width: 100%;
      margin-top: 24px;
      padding: 13px;
      background: #1f2937;
      color: white;
      border: none;
      border-radius: 7px;
      font-size: 16px;
      cursor: pointer;
    }

    button:hover {
      background: #374151;
    }

    /* Navigation link below the form. */
    .back-link {
      display: block;
      margin-top: 18px;
      text-align: center;
      color: #1f2937;
      text-decoration: none;
    }

    @media (max-width: 700px) {
      .container {
        margin: 20px;
        padding: 22px;
      }

      nav {
        padding: 18px 22px;
      }
    }
  </style>
</head>

<body>

<!-- Website navigation bar. -->
<nav>
  <h2>🍴 RecipeHub</h2>
</nav>

<div class="container">

  <h1>✏️ Edit Recipe</h1>

  <!-- Submit updated recipe details to EditRecipeServlet. -->
  <form action="edit-recipe" method="post">

    <!-- Hidden field identifies the recipe being edited. -->
    <input type="hidden" name="id" value="<%= recipe.getId() %>">

    <label for="title">Recipe Title</label>
    <input id="title" type="text" name="title"
           value="<%= recipe.getTitle() %>" required>

    <label for="description">Description</label>
    <textarea id="description" name="description" required><%= recipe.getDescription() %></textarea>

    <label for="ingredients">Ingredients</label>
    <textarea id="ingredients" name="ingredients" required><%= recipe.getIngredients() %></textarea>

    <label for="instructions">Instructions</label>
    <textarea id="instructions" name="instructions" required><%= recipe.getInstructions() %></textarea>

    <label for="category">Category</label>
    <select id="category" name="category" required>
      <option value="Indian" <%= "Indian".equals(recipe.getCategory()) ? "selected" : "" %>>Indian</option>
      <option value="Italian" <%= "Italian".equals(recipe.getCategory()) ? "selected" : "" %>>Italian</option>
      <option value="Chinese" <%= "Chinese".equals(recipe.getCategory()) ? "selected" : "" %>>Chinese</option>
      <option value="Dessert" <%= "Dessert".equals(recipe.getCategory()) ? "selected" : "" %>>Dessert</option>
      <option value="Healthy" <%= "Healthy".equals(recipe.getCategory()) ? "selected" : "" %>>Healthy</option>
      <option value="Rice" <%= "Rice".equals(recipe.getCategory()) ? "selected" : "" %>>Rice</option>
    </select>

    <label for="cooking_time">Cooking Time (minutes)</label>
    <input id="cooking_time" type="number" name="cooking_time"
           min="1" value="<%= recipe.getCookingTime() %>" required>

    <button type="submit">Save Changes</button>
  </form>

  <!-- Return to the recipes list without saving changes. -->
  <a class="back-link" href="recipes">← Back to Recipes</a>

</div>

</body>
</html>