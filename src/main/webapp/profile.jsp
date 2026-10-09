
<%@ page import="com.recipehub.model.User" %>
<%@ page import="com.recipehub.model.Recipe" %>
<%@ page import="java.util.List" %>

<%
  // Retrieve the logged-in user's profile information from the servlet.
  User profileUser = (User) request.getAttribute("profileUser");

  // Retrieve the recipes created by the logged-in user.
  List<Recipe> userRecipes =
          (List<Recipe>) request.getAttribute("userRecipes");

  // Retrieve the total number of recipes shared by the user.
  Integer totalRecipes =
          (Integer) request.getAttribute("totalRecipes");

  // Prevent unauthorized users from viewing the profile page.
  if (profileUser == null) {
    response.sendRedirect("login.html");
    return;
  }

  // Display zero if the recipe count is unavailable.
  if (totalRecipes == null) {
    totalRecipes = 0;
  }

  // Read the password-change status returned in the URL.
  String passwordStatus = request.getParameter("password");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Profile | RecipeHub</title>

  <style>
    /* Apply consistent sizing to all page elements. */
    * {
      box-sizing: border-box;
    }

    /* Set the default page appearance. */
    body {
      margin: 0;
      font-family: Arial, sans-serif;
      background: #f6f7fb;
      color: #222;
    }

    /* Style the navigation bar. */
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

    /* Center the profile content and limit its width. */
    .container {
      width: 88%;
      max-width: 1000px;
      margin: 40px auto;
    }

    /* Create the main profile card. */
    .profile-card {
      background: white;
      padding: 35px;
      border-radius: 16px;
      box-shadow: 0 4px 18px rgba(0, 0, 0, 0.07);
    }

    /* Arrange the user's avatar and account information. */
    .profile-header {
      display: flex;
      align-items: center;
      gap: 22px;
      padding-bottom: 25px;
      border-bottom: 1px solid #eee;
    }

    /* Style the circular avatar. */
    .avatar {
      width: 85px;
      height: 85px;
      flex-shrink: 0;
      border-radius: 50%;
      background: #ff7043;
      color: white;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 34px;
      font-weight: bold;
    }

    .profile-header h1 {
      margin: 0 0 8px;
      overflow-wrap: anywhere;
    }

    .profile-header p {
      margin: 0 0 6px;
      color: #666;
      overflow-wrap: anywhere;
    }

    /* Arrange profile statistics in responsive columns. */
    .stats {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
      gap: 18px;
      margin-top: 25px;
    }

    .stat-card {
      background: #fff4ee;
      border-radius: 12px;
      padding: 22px;
    }

    .stat-card h2 {
      color: #e85d2a;
      margin: 0 0 8px;
      font-size: 30px;
    }

    .stat-card p {
      margin: 0;
      color: #555;
    }

    /* Display the user's recipes as individual cards. */
    .section-title {
      margin: 28px 0 18px;
    }

    .recipe-list {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 16px;
    }

    .recipe-item {
      border: 1px solid #eee;
      padding: 18px;
      border-radius: 10px;
      background: #fff;
    }

    .recipe-item h3 {
      margin: 0 0 10px;
      overflow-wrap: anywhere;
    }

    .recipe-item p {
      color: #666;
      margin: 0 0 12px;
    }

    /* Reusable style for navigation and action buttons. */
    .btn {
      display: inline-block;
      background: #1f2937;
      color: white;
      padding: 10px 15px;
      border: none;
      border-radius: 7px;
      text-decoration: none;
      font-size: 14px;
      margin-top: 15px;
      cursor: pointer;
    }

    .btn:hover {
      background: #374151;
    }

    /* Style the password-change section. */
    .password-section {
      margin-top: 32px;
      padding-top: 24px;
      border-top: 1px solid #eee;
    }

    .password-form {
      max-width: 480px;
    }

    .form-group {
      margin-bottom: 16px;
    }

    .form-group label {
      display: block;
      margin-bottom: 7px;
      font-weight: bold;
    }

    .form-group input {
      width: 100%;
      padding: 12px;
      border: 1px solid #d1d5db;
      border-radius: 7px;
      font-size: 15px;
    }

    .form-group input:focus {
      outline: 2px solid #fb923c;
      border-color: transparent;
    }

    /* Display feedback messages returned by the password servlet. */
    .message {
      padding: 12px;
      margin: 15px 0;
      border-radius: 7px;
      font-size: 14px;
    }

    .success {
      background: #dcfce7;
      color: #166534;
    }

    .error {
      background: #fee2e2;
      color: #991b1b;
    }

    /* Style the empty recipe message. */
    .empty {
      color: #666;
      background: #fafafa;
      padding: 22px;
      border-radius: 10px;
    }

    /* Adjust the layout for mobile screens. */
    @media (max-width: 600px) {
      nav {
        padding: 18px 20px;
      }

      nav a {
        margin-left: 0;
        margin-right: 12px;
      }

      .container {
        width: 92%;
        margin-top: 25px;
      }

      .profile-card {
        padding: 22px;
      }

      .profile-header {
        align-items: flex-start;
        flex-direction: column;
      }
    }
  </style>
</head>

<body>

<!-- Main navigation links for the RecipeHub application. -->
<nav>
  <h2>🍴 RecipeHub</h2>

  <div>
    <a href="recipes">Home</a>
    <a href="my-recipes">My Recipes</a>
    <a href="add-recipe.html">Add Recipe</a>
    <a href="favorite">Favorites</a>
    <a href="profile">My Profile</a>
    <a href="logout">Logout</a>
  </div>
</nav>

<main class="container">

  <section class="profile-card">

    <!-- Display the user's avatar, name and email address. -->
    <div class="profile-header">

      <div class="avatar">
        <%= profileUser.getName() != null
                && !profileUser.getName().isEmpty()
                ? profileUser.getName().substring(0, 1).toUpperCase()
                : "U" %>
      </div>

      <div>
        <h1>My Profile</h1>

        <p>
          <%= profileUser.getName() == null
                  ? "RecipeHub User"
                  : profileUser.getName() %>
        </p>

        <p>
          <%= profileUser.getEmail() == null
                  ? "Email not available"
                  : profileUser.getEmail() %>
        </p>
      </div>

    </div>

    <!-- Show basic statistics about the user's contributions. -->
    <div class="stats">

      <div class="stat-card">
        <h2><%= totalRecipes %></h2>
        <p>Recipes Shared</p>
      </div>

      <div class="stat-card">
        <h2>🍴</h2>
        <p>RecipeHub Member</p>
      </div>

    </div>

    <!-- Display recipes created by the logged-in user. -->
    <h2 class="section-title">My Contributions</h2>

    <% if (userRecipes == null || userRecipes.isEmpty()) { %>

    <div class="empty">
      You haven't shared any recipes yet.
      <br>
      <a href="add-recipe.html" class="btn">
        Add Your First Recipe
      </a>
    </div>

    <% } else { %>

    <div class="recipe-list">

      <% for (Recipe recipe : userRecipes) { %>

      <div class="recipe-item">

        <h3><%= recipe.getTitle() %></h3>

        <p>
          Category: <%= recipe.getCategory() %>
        </p>

        <p>
          Cooking Time: <%= recipe.getCookingTime() %> minutes
        </p>

        <a class="btn"
           href="recipe-details?id=<%= recipe.getId() %>">
          View Recipe
        </a>

      </div>

      <% } %>

    </div>

    <% } %>

    <!-- Password management section for the current user. -->
    <section class="password-section">

      <h2>Security Settings</h2>
      <p>Keep your RecipeHub account secure by updating your password.</p>

      <!-- Show success or error feedback when redirected by the servlet. -->
      <% if ("success".equals(passwordStatus)) { %>

      <div class="message success">
        Password changed successfully.
      </div>

      <% } else if ("incorrect".equals(passwordStatus)) { %>

      <div class="message error">
        Your current password is incorrect.
      </div>

      <% } else if ("mismatch".equals(passwordStatus)) { %>

      <div class="message error">
        The new password and confirmation do not match.
      </div>

      <% } else if ("invalid".equals(passwordStatus)) { %>

      <div class="message error">
        Please enter valid passwords. The new password must contain at least 8 characters.
      </div>

      <% } else if ("failed".equals(passwordStatus)) { %>

      <div class="message error">
        Password could not be changed. Please try again.
      </div>

      <% } %>

      <!-- Submit the password-change request to the ChangePasswordServlet. -->
      <form class="password-form"
            action="change-password"
            method="post">

        <!-- Ask the user to verify their existing password. -->
        <div class="form-group">
          <label for="oldPassword">Current Password</label>
          <input type="password"
                 id="oldPassword"
                 name="oldPassword"
                 autocomplete="current-password"
                 required>
        </div>

        <!-- Collect the new password to be saved in the database. -->
        <div class="form-group">
          <label for="newPassword">New Password</label>
          <input type="password"
                 id="newPassword"
                 name="newPassword"
                 minlength="8"
                 autocomplete="new-password"
                 required>
        </div>

        <!-- Ask the user to repeat the new password for confirmation. -->
        <div class="form-group">
          <label for="confirmPassword">Confirm New Password</label>
          <input type="password"
                 id="confirmPassword"
                 name="confirmPassword"
                 minlength="8"
                 autocomplete="new-password"
                 required>
        </div>

        <!-- Submit the form for server-side validation and updating. -->
        <button type="submit" class="btn">
          🔒 Change Password
        </button>

      </form>

    </section>

    <!-- Provide a shortcut back to the main recipes page. -->
    <a href="recipes" class="btn">← Back to Recipes</a>

  </section>

</main>

</body>
</html>