<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>RecipeHub - Categories</title>

  <style>
    /* Apply consistent sizing to all elements */
    * {
      box-sizing: border-box;
    }

    /* Set the overall page layout and background */
    body {
      margin: 0;
      font-family: Arial, sans-serif;
      background: #f6f7fb;
      color: #222;
    }

    /* Style the navigation bar */
    nav {
      background: #1f2937;
      padding: 18px 60px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    /* Style the RecipeHub logo */
    nav h2 {
      color: white;
      margin: 0;
    }

    /* Style navigation links */
    nav a {
      color: white;
      text-decoration: none;
      margin-left: 25px;
      font-size: 15px;
    }

    /* Style the category page heading section */
    .hero {
      background: white;
      text-align: center;
      padding: 50px 20px;
    }

    /* Style the main heading */
    .hero h1 {
      font-size: 40px;
      margin-bottom: 10px;
    }

    /* Style the introductory description */
    .hero p {
      color: #666;
      font-size: 17px;
    }

    /* Set the width and spacing of the category container */
    .container {
      width: 88%;
      margin: 40px auto;
    }

    /* Display category cards in a responsive grid */
    .categories {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 25px;
    }

    /* Style each individual category card */
    .category {
      background: white;
      padding: 35px;
      text-align: center;
      border-radius: 15px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
      transition: 0.3s;
    }

    /* Add a hover effect to category cards */
    .category:hover {
      transform: translateY(-5px);
    }

    /* Set the size of category icons */
    .category .icon {
      font-size: 50px;
    }

    /* Add spacing around category names */
    .category h2 {
      margin: 15px 0 10px;
    }

    /* Style the View Recipes buttons */
    .category a {
      display: inline-block;
      margin-top: 10px;
      padding: 10px 18px;
      background: #1f2937;
      color: white;
      text-decoration: none;
      border-radius: 7px;
    }

    /* Change button color when the user hovers over it */
    .category a:hover {
      background: #374151;
    }
  </style>
</head>

<body>

<!-- Navigation bar for moving between website pages -->
<nav>
  <h2>🍴 RecipeHub</h2>

  <div>
    <a href="recipes">Home</a>
    <a href="categories.jsp">Categories</a>
    <a href="add-recipe.html">➕ Add Recipe</a>
    <a href="favorite">❤️ Favorites</a>
    <a href="login.html">Logout</a>
  </div>
</nav>

<!-- Introduction section for the categories page -->
<section class="hero">
  <h1>🍽️ Recipe Categories</h1>
  <p>Explore recipes based on your favorite cuisine.</p>
</section>

<!-- Main container holding all recipe categories -->
<div class="container">

  <!-- Responsive grid containing individual category cards -->
  <div class="categories">

    <!-- Indian recipes category -->
    <div class="category">
      <div class="icon">🇮🇳</div>
      <h2>Indian</h2>
      <p>Delicious Indian recipes.</p>
      <a href="category?name=Indian">View Recipes</a>
    </div>

    <!-- Italian recipes category -->
    <div class="category">
      <div class="icon">🍝</div>
      <h2>Italian</h2>
      <p>Classic Italian dishes.</p>
      <a href="category?name=Italian">View Recipes</a>
    </div>

    <!-- Chinese recipes category -->
    <div class="category">
      <div class="icon">🥡</div>
      <h2>Chinese</h2>
      <p>Tasty Chinese recipes.</p>
      <a href="category?name=Chinese">View Recipes</a>
    </div>

    <!-- Dessert recipes category -->
    <div class="category">
      <div class="icon">🍰</div>
      <h2>Dessert</h2>
      <p>Sweet and delicious desserts.</p>
      <a href="category?name=Dessert">View Recipes</a>
    </div>

    <!-- Healthy recipes category -->
    <div class="category">
      <div class="icon">🥗</div>
      <h2>Healthy</h2>
      <p>Healthy and nutritious meals.</p>
      <a href="category?name=Healthy">View Recipes</a>
    </div>

  </div>
</div>

</body>
</html>
```
