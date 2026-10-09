
<%@ page import="java.util.List" %>
<%@ page import="com.recipehub.model.Recipe" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RecipeHub - Recipes</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f6f7fb;
            color: #222;
        }

        nav {
            background: #1f2937;
            padding: 18px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
        }

        nav h2 {
            color: white;
            margin: 0;
        }

        nav a {
            color: white;
            text-decoration: none;
            margin-left: 18px;
            font-size: 15px;
        }

        nav a:hover {
            color: #fb923c;
        }

        .hero {
            background: white;
            text-align: center;
            padding: 55px 20px 40px;
        }

        .hero h1 {
            font-size: 42px;
            margin: 0 0 12px;
        }

        .hero p {
            color: #666;
            font-size: 18px;
        }

        .search {
            text-align: center;
            margin: 30px 0;
        }

        .search input {
            width: 55%;
            padding: 15px 20px;
            border: 1px solid #ddd;
            border-radius: 30px;
            font-size: 16px;
            outline: none;
        }

        .container {
            width: 88%;
            margin: auto;
        }

        .container h2 {
            margin-bottom: 25px;
        }

        .recipes {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(270px, 1fr));
            gap: 25px;
            padding-bottom: 50px;
        }

        .card {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            transition: 0.3s;
        }

        .card:hover {
            transform: translateY(-5px);
        }

        .image {
            height: 180px;
            background: #e5e7eb;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 60px;
        }

        .content {
            padding: 20px;
        }

        .content h3 {
            margin-top: 0;
            font-size: 22px;
        }

        .content p {
            color: #666;
            line-height: 1.5;
            overflow-wrap: anywhere;
        }

        .info {
            margin: 15px 0;
            color: #555;
            font-size: 14px;
        }

        .btn {
            display: inline-block;
            background: #1f2937;
            color: white;
            text-decoration: none;
            padding: 10px 14px;
            border: none;
            border-radius: 7px;
            cursor: pointer;
            font-size: 14px;
            margin: 4px 2px;
        }

        .edit-btn {
            background: #2563eb;
        }

        .delete-btn {
            background: #dc2626;
        }

        .action-form {
            display: inline;
        }

        @media (max-width: 700px) {
            nav {
                padding: 18px 20px;
            }

            nav a {
                margin-left: 8px;
            }

            .hero h1 {
                font-size: 32px;
            }

            .search input {
                width: 85%;
            }

            .container {
                width: 92%;
            }
        }
    </style>

    <script>
        // Search recipes by title.
        function searchRecipes() {
            const input = document.getElementById("searchInput")
                .value.toLowerCase();

            const cards = document.getElementsByClassName("card");

            for (let i = 0; i < cards.length; i++) {
                const title = cards[i]
                    .getElementsByTagName("h3")[0]
                    .innerText.toLowerCase();

                cards[i].style.display =
                    title.includes(input) ? "" : "none";
            }
        }

        // Confirm before deleting a recipe.
        function confirmDelete() {
            return confirm(
                "Are you sure you want to delete this recipe?"
            );
        }
    </script>
</head>

<body>

<nav>
    <h2>🍴 RecipeHub</h2>

    <div>
        <a href="recipes">Home</a>
        <a href="categories.jsp">Categories</a>
        <a href="add-recipe.html">➕ Add Recipe</a>
        <a href="my-recipes">📖 My Recipes</a>
        <a href="profile">👤 My Profile</a>
        <a href="favorite">❤️ Favorites</a>
        <a href="logout">Logout</a>
    </div>
</nav>

<section class="hero">
    <h1>Discover Delicious Recipes</h1>
    <p>Explore, share and enjoy amazing recipes.</p>
</section>

<div class="search">
    <input type="text"
           id="searchInput"
           onkeyup="searchRecipes()"
           placeholder="🔍 Search recipes...">
</div>

<div class="container">

    <h2>Popular Recipes</h2>

    <div class="recipes">

        <%
            // Retrieve recipes loaded by RecipeServlet.
            List<Recipe> recipes =
                    (List<Recipe>) request.getAttribute("recipes");

            if (recipes != null && !recipes.isEmpty()) {
                for (Recipe recipe : recipes) {
        %>

        <div class="card">

            <div class="image">🍽️</div>

            <div class="content">

                <h3><%= recipe.getTitle() %></h3>

                <p>
                    <%= recipe.getDescription() == null
                            ? "No description available."
                            : recipe.getDescription() %>
                </p>

                <div class="info">
                    📂 <%= recipe.getCategory() %>
                    &nbsp; | &nbsp;
                    ⏱️ <%= recipe.getCookingTime() %> min
                </div>

                <!-- View recipe details. -->
                <a href="recipe-details?id=<%= recipe.getId() %>"
                   class="btn">
                    View Recipe
                </a>

                <!-- Add recipe to favorites. -->
                <form action="favorite"
                      method="post"
                      class="action-form">

                    <input type="hidden"
                           name="recipeId"
                           value="<%= recipe.getId() %>">

                    <button type="submit" class="btn">
                        ❤️ Favorite
                    </button>
                </form>

                <!-- Edit recipe. -->
                <a href="edit-recipe?id=<%= recipe.getId() %>"
                   class="btn edit-btn">
                    ✏️ Edit
                </a>

                <!-- Delete recipe using POST. -->
                <form action="delete-recipe"
                      method="post"
                      class="action-form"
                      onsubmit="return confirmDelete();">

                    <input type="hidden"
                           name="id"
                           value="<%= recipe.getId() %>">

                    <button type="submit" class="btn delete-btn">
                        🗑️ Delete
                    </button>
                </form>

            </div>
        </div>

        <%
            }
        } else {
        %>

        <p>No recipes available.</p>

        <%
            }
        %>

    </div>
</div>

</body>
</html>