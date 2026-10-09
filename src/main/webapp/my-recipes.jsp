
<%@ page import="java.util.List" %>
<%@ page import="com.recipehub.model.Recipe" %>
<%@ page import="com.recipehub.model.User" %>

<%
  // Retrieve the logged-in user from the session.
  User user = (User) session.getAttribute("user");

  // Redirect to login if no user session exists.
  if (user == null) {
    response.sendRedirect("login.html");
    return;
  }

  // Get the recipes supplied by MyRecipesServlet.
  List<Recipe> recipes =
          (List<Recipe>) request.getAttribute("recipes");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Recipes | RecipeHub</title>

  <style>
    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
    }

    body {
      font-family: Arial, sans-serif;
      background: #f7f8fc;
      color: #222;
    }

    nav {
      background: #202b3c;
      color: white;
      padding: 18px 7%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: 15px;
    }

    nav h2 {
      color: #ff7043;
    }

    nav a {
      color: white;
      text-decoration: none;
      margin-left: 18px;
    }

    nav a:hover {
      color: #ff7043;
    }

    .container {
      width: 86%;
      max-width: 1200px;
      margin: 40px auto;
    }

    .heading {
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: 15px;
      margin-bottom: 25px;
    }

    .heading h1 {
      margin-bottom: 8px;
    }

    .heading p {
      color: #666;
    }

    .add-btn, .action-btn {
      display: inline-block;
      border: none;
      border-radius: 6px;
      padding: 10px 15px;
      text-decoration: none;
      cursor: pointer;
      font-size: 14px;
    }

    .add-btn {
      background: #ff7043;
      color: white;
    }

    .recipe-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
      gap: 24px;
    }

    .recipe-card {
      background: white;
      border-radius: 12px;
      padding: 22px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.07);
    }

    .category {
      display: inline-block;
      background: #fff0e9;
      color: #e85d2a;
      padding: 6px 10px;
      border-radius: 20px;
      font-size: 12px;
      margin-bottom: 12px;
    }

    .recipe-card h3 {
      margin-bottom: 10px;
    }

    .description {
      color: #666;
      line-height: 1.5;
      margin-bottom: 14px;
    }

    .time {
      color: #555;
      margin-bottom: 18px;
      font-size: 14px;
    }

    .actions {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }

    .view-btn {
      background: #202b3c;
      color: white;
    }

    .edit-btn {
      background: #2878d0;
      color: white;
    }

    .delete-btn {
      background: #dc3545;
      color: white;
    }

    .empty {
      background: white;
      text-align: center;
      padding: 45px 20px;
      border-radius: 12px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
    }

    .empty h3 {
      margin-bottom: 12px;
    }

    .empty p {
      color: #666;
      margin-bottom: 20px;
    }

    @media (max-width: 600px) {
      nav {
        padding: 18px 5%;
      }

      nav a {
        margin-left: 0;
        margin-right: 12px;
      }

      .container {
        width: 90%;
        margin-top: 25px;
      }
    }
  </style>

  <script>
    // Ask for confirmation before deleting a recipe.
    function confirmDelete() {
      return confirm("Are you sure you want to delete this recipe?");
    }
  </script>
</head>

<body>

<nav>
  <h2>RecipeHub</h2>

  <div>
    <a href="index.html">Home</a>
    <a href="recipes">All Recipes</a>
    <a href="my-recipes">My Recipes</a>
    <a href="add-recipe">Add Recipe</a>
    <a href="favorites.jsp">Favorites</a>
    <a href="logout">Logout</a>
  </div>
</nav>

<main class="container">

  <div class="heading">
    <div>
      <h1>My Recipes</h1>
      <p>Manage the recipes you have shared on RecipeHub.</p>
    </div>

    <a href="add-recipe" class="add-btn">+ Add New Recipe</a>
  </div>

  <% if (recipes == null || recipes.isEmpty()) { %>

  <!-- Display this section when the user has not added any recipes. -->
  <div class="empty">
    <h3>No recipes yet!</h3>
    <p>Start sharing your favorite dishes with the community.</p>
    <a href="add-recipe" class="add-btn">Create Your First Recipe</a>
  </div>

  <% } else { %>

  <!-- Display each recipe belonging to the logged-in user. -->
  <div class="recipe-grid">

    <% for (Recipe recipe : recipes) { %>

    <article class="recipe-card">

                    <span class="category">
                        <%= recipe.getCategory() %>
                    </span>

      <h3><%= recipe.getTitle() %></h3>

      <p class="description">
        <%= recipe.getDescription() == null
                ? "No description available."
                : recipe.getDescription() %>
      </p>

      <p class="time">
        Cooking time: <%= recipe.getCookingTime() %> minutes
      </p>

      <div class="actions">

        <a class="action-btn view-btn"
           href="recipe-details?id=<%= recipe.getId() %>">
          View
        </a>

        <a class="action-btn edit-btn"
           href="edit-recipe?id=<%= recipe.getId() %>">
          Edit
        </a>

        <form action="delete-recipe" method="post"
              onsubmit="return confirmDelete();">

          <input type="hidden" name="id"
                 value="<%= recipe.getId() %>">

          <button type="submit"
                  class="action-btn delete-btn">
            Delete
          </button>
        </form>

      </div>
    </article>

    <% } %>

  </div>

  <% } %>

</main>

</body>
</html>