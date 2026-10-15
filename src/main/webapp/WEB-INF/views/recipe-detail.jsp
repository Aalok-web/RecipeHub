<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <c:choose>
    <c:when test="${not empty recipe}">
      <title><c:out value="${recipe.title}"/> — RecipeHub</title>
      <meta name="description" content="<c:out value='${recipe.description}'/>">
    </c:when>
    <c:otherwise>
      <title>Recipe Not Found — RecipeHub</title>
    </c:otherwise>
  </c:choose>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,400..700;1,9..40,400..600&family=Playfair+Display:ital,wght@0,500..800;1,400..700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

  <!-- Polished Navigation Bar with Prominent RecipeHub Logo -->
  <header class="topbar">
    <div class="brand-wrapper">
      <a class="brand" href="${pageContext.request.contextPath}/">
        <span class="brand-mark">R</span> recipe<span>hub</span>
      </a>
      <span class="brand-edition-tag">THE MODERN KITCHEN</span>
    </div>

    <!-- Organized Navigation -->
    <nav class="main-nav" aria-label="Main Navigation">
      <a href="${pageContext.request.contextPath}/">Home</a>
      <a class="active" href="${pageContext.request.contextPath}/recipes#trending">Explore Recipes</a>
      <a href="${pageContext.request.contextPath}/#categories">Categories</a>
      <a href="${pageContext.request.contextPath}/#quick-easy">Quick &amp; Easy</a>
      <a href="${pageContext.request.contextPath}/#about">About</a>
    </nav>

    <!-- Nav Actions: Favorites and Real Authentication -->
    <div class="nav-actions">
      <c:choose>
        <c:when test="${not empty sessionScope.currentUser}">
          <a class="nav-fav-btn" href="${pageContext.request.contextPath}/profile#saved" title="View Saved Recipes">
            <span>♥</span> Favorites
          </a>
          <div class="user-profile-menu">
            <a href="${pageContext.request.contextPath}/profile" class="user-profile-link" title="View Profile & Saved Recipes" style="display:inline-flex; align-items:center; gap:8px; text-decoration:none; color:inherit;">
              <span class="user-avatar" style="background:#183728; color:#fff; border-radius:50%; width:28px; height:28px; display:inline-flex; align-items:center; justify-content:center; font-size:12px; font-weight:700;">
                <c:out value="${sessionScope.currentUser.initial}"/>
              </span>
              <span class="user-name" title="Signed in as <c:out value='${sessionScope.currentUser.name}'/>">
                <c:out value="${sessionScope.currentUser.name}"/>
              </span>
            </a>
            <a class="nav-profile-pill" href="${pageContext.request.contextPath}/profile" title="My Profile & Saved Recipes" style="padding:6px 12px; border-radius:8px; font-size:12px; font-weight:600; text-decoration:none; background:rgba(24,55,40,0.08); color:#183728; transition:all 0.2s ease;">
              Profile
            </a>
            <a class="nav-logout-btn" href="${pageContext.request.contextPath}/logout" title="Sign Out">
              Sign Out
            </a>
          </div>
        </c:when>
        <c:otherwise>
          <button class="nav-fav-btn" id="navFavoritesBtn" type="button" title="View Saved Recipes">
            <span>♥</span> Favorites <span class="fav-count-badge" id="favoritesCount" style="display:none;">0</span>
          </button>
          <a class="nav-signin-btn" href="${pageContext.request.contextPath}/login">
            Sign In <span>→</span>
          </a>
        </c:otherwise>
      </c:choose>
    </div>
  </header>

  <main class="detail-page-container">

    <!-- Not Found / Error State -->
    <c:if test="${notFound or empty recipe}">
      <div class="empty-state" style="margin-top: 48px;">
        <span class="empty-state-icon">🍽</span>
        <h3>Recipe not found</h3>
        <p><c:out value="${errorMessage != null ? errorMessage : 'The recipe you requested could not be found or has been removed.'}"/></p>
        <a class="back-link" href="${pageContext.request.contextPath}/" style="display:inline-flex;">
          ← Back to Recipes
        </a>
      </div>
    </c:if>

    <!-- Normal Recipe Detail View -->
    <c:if test="${not empty recipe and not notFound}">
      <!-- Navigation & Breadcrumbs -->
      <div class="detail-nav-bar">
        <a href="${pageContext.request.contextPath}/" class="back-link">
          ← Back to Recipes
        </a>
        <div class="breadcrumbs">
          <a href="${pageContext.request.contextPath}/">Home</a>
          <span>/</span>
          <a href="${pageContext.request.contextPath}/recipes?category=${recipe.category.toLowerCase()}"><c:out value="${recipe.category}"/></a>
          <span>/</span>
          <span><c:out value="${recipe.title}"/></span>
        </div>
      </div>

      <!-- Recipe Header -->
      <section class="detail-header">
        <div class="detail-header-badge-row">
          <span class="detail-category-badge"><c:out value="${recipe.category}"/></span>
          <span class="eyebrow" style="margin-bottom:0;"><span class="spark">✦</span> CHEF TESTED RECIPE</span>
        </div>

        <h1 class="detail-title"><c:out value="${recipe.title}"/></h1>
        <p class="detail-description"><c:out value="${recipe.description}"/></p>

        <!-- Quick Facts Strip -->
        <div class="quick-facts-strip">
          <div class="fact-item">
            <div class="fact-icon">⏱</div>
            <div class="fact-text">
              <small>Total Time</small>
              <strong><c:out value="${recipe.totalMinutes}"/> min</strong>
            </div>
          </div>

          <div class="fact-item">
            <div class="fact-icon">🔪</div>
            <div class="fact-text">
              <small>Preparation Time</small>
              <strong><c:out value="${recipe.prepMinutes}"/> min prep</strong>
            </div>
          </div>

          <div class="fact-item">
            <div class="fact-icon">🔥</div>
            <div class="fact-text">
              <small>Cooking Time</small>
              <strong><c:out value="${recipe.cookMinutes}"/> min cook</strong>
            </div>
          </div>

          <div class="fact-item">
            <div class="fact-icon">👀</div>
            <div class="fact-text">
              <small>Views since restart</small>
              <strong><c:out value="${recipeViewCount}"/></strong>
            </div>
          </div>

          <div class="fact-item">
            <div class="fact-icon">🍽</div>
            <div class="fact-text">
              <small>Servings</small>
              <strong><c:out value="${recipe.servings}"/> servings</strong>
            </div>
          </div>

          <!-- Quick Actions -->
          <div class="detail-actions">
            <button class="action-btn" id="printRecipeBtn" type="button" title="Print recipe">
              <span>🖨</span> Print
            </button>
            <button class="action-btn" id="shareRecipeBtn" type="button" title="Copy shareable link">
              <span>🔗</span> Share
            </button>
            <button
              class="heart-button <c:if test='${isFavorite}'>saved</c:if>"
              type="button"
              data-recipe-id="${recipe.id}"
              aria-label="Save ${recipe.title} to favourites"
              title="Save recipe"
              style="position:static;"
            ><c:choose><c:when test="${isFavorite}">♥</c:when><c:otherwise>♡</c:otherwise></c:choose></button>
          </div>
        </div>
      </section>

      <!-- Large Food Image Hero Frame -->
      <div class="detail-image-hero">
        <img
          src="<c:out value='${recipe.imageUrl}'/>"
          alt="<c:out value='${recipe.title}'/>"
          loading="eager"
        >
      </div>

      <!-- Main Recipe Content: Ingredients Sidebar + Step-by-Step Instructions -->
      <div class="detail-content-grid">

        <!-- Ingredients Sidebar -->
        <aside class="ingredients-sidebar">
          <div class="ingredients-header">
            <h3>Ingredients</h3>
            <span class="count-chip"><c:out value="${recipe.ingredientList.size()}"/> items</span>
          </div>

          <p style="font-size:12.5px; color:var(--ink-muted); margin:-10px 0 18px;">
            Check off ingredients as you prepare them:
          </p>

          <ul class="ingredients-list">
            <c:forEach var="ingredient" items="${recipe.ingredientList}">
              <li class="ingredient-item">
                <input type="checkbox" class="ingredient-checkbox" aria-label="Mark <c:out value='${ingredient}'/> as prepared">
                <span><c:out value="${ingredient}"/></span>
              </li>
            </c:forEach>
          </ul>
        </aside>

        <!-- Instructions Main Column -->
        <section class="instructions-column">
          <div>
            <h3>Cooking Instructions</h3>
            <p style="color:var(--ink-body); font-size:14.5px; margin-top:4px;">
              Follow these simple kitchen steps for a delicious, foolproof home-cooked meal.
            </p>
          </div>

          <c:forEach var="step" items="${recipe.instructionList}" varStatus="status">
            <article class="step-card">
              <div class="step-number">
                <c:choose>
                  <c:when test="${status.count < 10}">0${status.count}</c:when>
                  <c:otherwise>${status.count}</c:otherwise>
                </c:choose>
              </div>
              <div class="step-text">
                <c:out value="${step}"/>
              </div>
            </article>
          </c:forEach>

          <!-- Chef's Pro Tip -->
          <div class="chef-note-box">
            <div class="chef-note-icon">💡</div>
            <div class="chef-note-content">
              <strong>Chef's Kitchen Note</strong>
              <p>For the best culinary experience, prep all ingredients (mise en place) before heating the pan. Taste as you season, and allow resting time when recommended.</p>
            </div>
          </div>
        </section>
      </div>

      <!-- Related Recipes Suggestions -->
      <c:if test="${not empty relatedRecipes}">
        <section class="related-section">
          <div class="related-section-header">
            <div class="eyebrow"><span class="spark">✳</span> MORE INSPIRATION</div>
            <h3>You might also enjoy</h3>
          </div>
          <div class="recipe-grid">
            <c:forEach var="related" items="${relatedRecipes}">
              <article class="recipe-card">
                <div class="recipe-card-media">
                  <a href="${pageContext.request.contextPath}/recipe?id=${related.id}">
                    <img src="<c:out value='${related.imageUrl}'/>" alt="<c:out value='${related.title}'/>" loading="lazy">
                  </a>
                  <span class="card-category-badge"><c:out value="${related.category}"/></span>
                  <button class="heart-button" type="button" data-recipe-id="${related.id}" aria-label="Save recipe">♡</button>
                </div>
                <div class="recipe-card-body">
                  <div class="recipe-card-meta">
                    <span class="meta-pill">⏱ <c:out value="${related.totalMinutes}"/> min</span>
                    <span class="meta-difficulty">✦ EASY</span>
                  </div>
                  <h3 class="recipe-card-title">
                    <a href="${pageContext.request.contextPath}/recipe?id=${related.id}">
                      <c:out value="${related.title}"/>
                    </a>
                  </h3>
                  <p class="recipe-card-desc"><c:out value="${related.description}"/></p>
                  <div class="recipe-card-footer">
                    <a href="${pageContext.request.contextPath}/recipe?id=${related.id}" class="view-recipe-link">
                      View recipe <span>↗</span>
                    </a>
                    <span class="servings-tag">🍽 <c:out value="${related.servings}"/> servings</span>
                  </div>
                </div>
              </article>
            </c:forEach>
          </div>
        </section>
      </c:if>

      <!-- Back to Top / Recipes Footer Bar -->
      <div style="margin-top: 50px; text-align: center;">
        <a href="${pageContext.request.contextPath}/" class="back-link" style="display:inline-flex; font-size:15px; padding:12px 26px;">
          ← Back to all recipes
        </a>
      </div>
    </c:if>

  </main>

  <!-- Footer -->
  <footer>
    <div class="footer-top">
      <div class="footer-brand-col">
        <a class="brand" href="${pageContext.request.contextPath}/">
          <span class="brand-mark">R</span> recipe<span>hub</span>
        </a>
        <p>A modern culinary discovery platform designed for curious home cooks who love good food, seasonal ingredients, and relaxed kitchen moments.</p>
      </div>

      <div class="footer-col">
        <h5>Explore Recipes</h5>
        <ul>
          <li><a href="${pageContext.request.contextPath}/recipes?category=breakfast">Breakfast &amp; Brunch</a></li>
          <li><a href="${pageContext.request.contextPath}/recipes?category=lunch">Fresh Lunches</a></li>
          <li><a href="${pageContext.request.contextPath}/recipes?category=dinner">Family Dinners</a></li>
          <li><a href="${pageContext.request.contextPath}/recipes?category=vegetarian">Plant-Based Bowls</a></li>
          <li><a href="${pageContext.request.contextPath}/recipes?category=desserts">Artisanal Desserts</a></li>
        </ul>
      </div>

      <div class="footer-col">
        <h5>RecipeHub Platform</h5>
        <ul>
          <li><a href="${pageContext.request.contextPath}/#about">About Our Philosophy</a></li>
          <li><a href="${pageContext.request.contextPath}/#quick-easy">Quick &amp; Easy Meals</a></li>
          <li><a href="${pageContext.request.contextPath}/#trending">Trending Recipes</a></li>
          <li><a href="#" onclick="document.getElementById('navFavoritesBtn').click(); return false;">Saved Favorites</a></li>
        </ul>
      </div>

      <div class="footer-col">
        <h5>Cook With Us</h5>
        <ul>
          <li><a href="${pageContext.request.contextPath}/#newsletterForm">Weekly Inspiration</a></li>
          <li><a href="${pageContext.request.contextPath}/login">Chef Community &amp; Sign In</a></li>
          <li><a href="${pageContext.request.contextPath}/">Home Kitchen Edit</a></li>
        </ul>
      </div>
    </div>

    <div class="footer-bottom">
      <span>© RecipeHub. Handcrafted with care for home cooks everywhere.</span>
      <span>Java 17 · Jakarta Servlet 6.0 · JSP &amp; JSTL · MySQL</span>
    </div>
  </footer>

  <!-- Favorites Drawer Modal -->
  <div class="modal-overlay" id="favoritesModal">
    <div class="modal-card">
      <button class="modal-close-btn" type="button" aria-label="Close modal">✕</button>
      <div class="eyebrow"><span class="spark">♥</span> YOUR SAVED COLLECTION</div>
      <h3 style="margin-bottom:8px;">Saved Recipes</h3>
      <div id="favoritesModalList"></div>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
