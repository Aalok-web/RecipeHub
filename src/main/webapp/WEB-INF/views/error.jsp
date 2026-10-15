<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>
    <c:choose>
      <c:when test="${pageContext.errorData.statusCode == 404}">Page Not Found (404) — RecipeHub</c:when>
      <c:otherwise>Temporary Kitchen Hitch — RecipeHub</c:otherwise>
    </c:choose>
  </title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,400..700;1,9..40,400..600&family=Playfair+Display:ital,wght@0,500..800;1,400..700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-body">

  <!-- Topbar -->
  <header class="topbar">
    <div class="brand-wrapper">
      <a class="brand" href="${pageContext.request.contextPath}/">
        <span class="brand-mark">R</span> recipe<span>hub</span>
      </a>
      <span class="brand-edition-tag">EDITORIAL KITCHEN</span>
    </div>
    <a href="${pageContext.request.contextPath}/" class="back-link">
      ← Back to Home
    </a>
  </header>

  <!-- Error Card Container -->
  <main class="auth-main-wrapper" style="min-height: calc(100vh - 160px);">
    <div class="auth-card" style="text-align: center; max-width: 540px; padding: 48px 36px;">

      <div style="font-size: 3.5rem; line-height: 1; margin-bottom: 16px;">
        <c:choose>
          <c:when test="${pageContext.errorData.statusCode == 404}">🍳</c:when>
          <c:otherwise>🥣</c:otherwise>
        </c:choose>
      </div>

      <div class="eyebrow" style="justify-content: center; margin-bottom: 8px;">
        <span class="spark">✦</span>
        <c:choose>
          <c:when test="${pageContext.errorData.statusCode == 404}">ERROR 404 · MISSING INGREDIENT</c:when>
          <c:otherwise>ERROR <c:out value="${pageContext.errorData.statusCode != null ? pageContext.errorData.statusCode : 500}"/> · KITCHEN NOTICE</c:otherwise>
        </c:choose>
      </div>

      <h1 style="font-family: var(--font-serif); font-size: 2rem; color: var(--color-forest); margin: 0 0 12px 0;">
        <c:choose>
          <c:when test="${pageContext.errorData.statusCode == 404}">We Couldn't Find That Recipe</c:when>
          <c:otherwise>Something Went Off the Recipe</c:otherwise>
        </c:choose>
      </h1>

      <p style="color: var(--color-muted); font-size: 0.95rem; line-height: 1.6; margin: 0 0 28px 0;">
        <c:choose>
          <c:when test="${pageContext.errorData.statusCode == 404}">
            The culinary page you are searching for might have been moved, renamed, or is simmering on a different backburner.
          </c:when>
          <c:otherwise>
            An unexpected error occurred while processing your request. Rest assured, our kitchen team has been alerted and no ingredients were harmed.
          </c:otherwise>
        </c:choose>
      </p>

      <div style="display: flex; gap: 12px; justify-content: center; flex-wrap: wrap;">
        <a href="${pageContext.request.contextPath}/" class="auth-submit-btn" style="text-decoration: none; width: auto; padding: 12px 24px;">
          Return to Home <span>→</span>
        </a>
        <a href="${pageContext.request.contextPath}/recipes" class="auth-tab-btn" style="text-decoration: none; display: inline-flex; align-items: center; justify-content: center; padding: 12px 20px; border-radius: 999px;">
          Explore All Recipes
        </a>
      </div>

    </div>
  </main>

  <!-- Footer -->
  <footer>
    <div class="footer-bottom" style="border-top: none; padding-top: 0;">
      <span>© RecipeHub. Handcrafted with care for home cooks everywhere.</span>
      <span>Safe Enterprise Architecture · Jakarta EE 10</span>
    </div>
  </footer>

</body>
</html>
