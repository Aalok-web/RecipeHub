```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>RecipeHub - Password Recovery</title>

  <style>
    /* Keep the recovery result page simple and centered. */
    body {
      margin: 0;
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #fff7ed;
      font-family: Arial, sans-serif;
    }

    .card {
      width: 85%;
      max-width: 420px;
      padding: 30px;
      background: white;
      border-radius: 14px;
      text-align: center;
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
    }

    h1 {
      color: #f97316;
    }

    p {
      line-height: 1.6;
      color: #444;
    }

    a {
      display: inline-block;
      margin-top: 15px;
      padding: 12px 20px;
      border-radius: 8px;
      background: #f97316;
      color: white;
      text-decoration: none;
    }
  </style>
</head>
<body>
<div class="card">
  <h1>RecipeHub</h1>
  <h2>Password Recovery</h2>

  <!-- Display the generic recovery message set by the servlet. -->
  <p>
    <%= request.getAttribute("message") != null
            ? request.getAttribute("message")
            : "Please submit your registered email to continue." %>
  </p>

  <!-- Allow the user to return to the login page. -->
  <a href="login.html">Back to Login</a>
</div>
</body>
</html>
```
