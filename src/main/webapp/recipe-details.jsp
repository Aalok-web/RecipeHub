<%@ page import="com.recipehub.model.Recipe" %>

<!DOCTYPE html>
<html>
<head>
    <title>RecipeHub - Recipe Details</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f6f7fb;
            color: #222;
        }

        nav {
            background: #1f2937;
            padding: 18px 60px;
        }

        nav h2 {
            color: white;
            margin: 0;
        }

        .container {
            width: 75%;
            margin: 50px auto;
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }

        h1 {
            font-size: 36px;
            margin-bottom: 10px;
        }

        .category {
            color: #666;
            margin-bottom: 25px;
        }

        h2 {
            margin-top: 30px;
        }

        p {
            line-height: 1.7;
            color: #555;
        }

        .back {
            display: inline-block;
            margin-top: 30px;
            padding: 10px 18px;
            background: #1f2937;
            color: white;
            text-decoration: none;
            border-radius: 7px;
        }
    </style>
</head>

<body>

<nav>
    <h2>🍴 RecipeHub</h2>
</nav>

<%
    Recipe recipe = (Recipe) request.getAttribute("recipe");

    if (recipe != null) {
%>

<div class="container">

    <h1><%= recipe.getTitle() %></h1>

    <div class="category">
        📂 <%= recipe.getCategory() %>
        &nbsp;&nbsp; | &nbsp;&nbsp;
        ⏱️ <%= recipe.getCookingTime() %> minutes
    </div>

    <h2>Description</h2>
    <p><%= recipe.getDescription() %></p>

    <h2>Ingredients</h2>
    <p><%= recipe.getIngredients() %></p>

    <h2>Instructions</h2>
    <p><%= recipe.getInstructions() %></p>

    <a href="recipes" class="back">← Back to Recipes</a>

</div>

<%
} else {
%>

<div class="container">
    <h1>Recipe Not Found</h1>
    <a href="recipes" class="back">← Back to Recipes</a>
</div>

<%
    }
%>

</body>
</html>