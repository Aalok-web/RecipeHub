<%@ page import="java.util.List" %>
<%@ page import="com.recipehub.model.Recipe" %>

<!DOCTYPE html>
<html>
<head>
    <title>RecipeHub - Favorites</title>

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

        /* Navigation bar */
        nav {
            background: #1f2937;
            padding: 18px 60px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        nav h2 {
            color: white;
            margin: 0;
        }

        nav a {
            color: white;
            text-decoration: none;
            margin-left: 25px;
            font-size: 15px;
        }

        /* Page heading */
        .hero {
            background: white;
            text-align: center;
            padding: 45px 20px;
        }

        .hero h1 {
            font-size: 38px;
            margin: 0 0 10px;
        }

        .hero p {
            color: #666;
            font-size: 17px;
        }

        /* Recipe container */
        .container {
            width: 88%;
            margin: 40px auto;
        }

        .recipes {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(270px, 1fr));
            gap: 25px;
        }

        /* Recipe card */
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
            padding: 10px 18px;
            border-radius: 7px;
        }

        /* Message shown when no favorites exist */
        .empty {
            text-align: center;
            background: white;
            padding: 40px;
            border-radius: 15px;
            color: #666;
        }
    </style>
</head>

<body>

<!-- Navigation bar -->
<nav>
    <h2>🍴 RecipeHub</h2>

    <div>
        <a href="recipes">Home</a>
        <a href="#">Categories</a>
        <a href="add-recipe.html">➕ Add Recipe</a>
        <a href="favorite">❤️ Favorites</a>
        <a href="login.html">Logout</a>
    </div>
</nav>

<!-- Page introduction -->
<section class="hero">
    <h1>❤️ My Favorite Recipes</h1>
    <p>Your saved recipes in one place.</p>
</section>

<div class="container">

    <div class="recipes">

        <%
            // Retrieve the user's favorite recipes from the servlet.
            List<Recipe> favorites =
                    (List<Recipe>) request.getAttribute("favorites");

            // Display favorite recipes if available.
            if (favorites != null && !favorites.isEmpty()) {

                for (Recipe recipe : favorites) {
        %>

        <div class="card">

            <div class="image">
                🍽️
            </div>

            <div class="content">

                <h3><%= recipe.getTitle() %></h3>

                <p>
                    <%= recipe.getDescription() %>
                </p>

                <div class="info">
                    📂 <%= recipe.getCategory() %>
                    &nbsp;&nbsp; | &nbsp;&nbsp;
                    ⏱️ <%= recipe.getCookingTime() %> min
                </div>

                <a href="recipe-details?id=<%= recipe.getId() %>"
                   class="btn">
                    View Recipe
                </a>

            </div>

        </div>

        <%
            }

        } else {
        %>

        <!-- Displayed when the user has no favorite recipes -->
        <div class="empty">
            <h2>No Favorite Recipes Yet ❤️</h2>
            <p>Go to the recipes page and add some recipes to your favorites.</p>
            <br>
            <a href="recipes" class="btn">Browse Recipes</a>
        </div>

        <%
            }
        %>

    </div>

</div>

</body>
</html>