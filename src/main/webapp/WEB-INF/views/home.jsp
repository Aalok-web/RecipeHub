<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>RecipeHub — Discover Recipes You'll Love</title>
  <meta name="description" content="Discover, search and cook curated seasonal recipes. Foolproof steps, simple ingredients, and extraordinary flavours for passionate home cooks.">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,400..700;1,9..40,400..600&family=Playfair+Display:ital,wght@0,500..800;1,400..700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

  <!-- 1. Polished Navigation Bar with Prominent RecipeHub Logo -->
  <header class="topbar">
    <div class="brand-wrapper">
      <a class="brand" href="${pageContext.request.contextPath}/">
        <span class="brand-mark">R</span> recipe<span>hub</span>
      </a>
      <span class="brand-edition-tag">THE MODERN KITCHEN</span>
    </div>

    <!-- 2. Organized Navigation -->
    <nav class="main-nav" aria-label="Main Navigation">
      <a class="active" href="${pageContext.request.contextPath}/">Home</a>
      <a href="#trending">Explore Recipes</a>
      <a href="#categories">Categories</a>
      <a href="#quick-easy">Quick &amp; Easy</a>
      <a href="#about">About</a>
    </nav>

    <!-- Nav Actions: Favorites and Real Authentication -->
    <div class="nav-actions">
      <c:choose>
        <c:when test="${not empty sessionScope.currentUser}">
          <a class="nav-fav-btn" href="${pageContext.request.contextPath}/profile#saved" title="View Saved Recipes">
            <span>♥</span> Favorites <span class="fav-count-badge" id="favoritesCount" style="<c:if test='${favoriteCount > 0}'>display:inline-block;</c:if>"><c:out value="${favoriteCount > 0 ? favoriteCount : 0}"/></span>
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

  <!-- Flash Notification for Login / Registration / Logout -->
  <c:if test="${not empty sessionScope.flashMessage}">
    <div class="status-banner" style="background:#f0fdf4; border:1px solid #bbf7d0; color:#166534;">
      <div class="status-banner-content">
        <span class="status-banner-icon">✓</span>
        <div><strong><c:out value="${sessionScope.flashMessage}"/></strong></div>
      </div>
    </div>
    <c:remove var="flashMessage" scope="session"/>
  </c:if>
  <c:if test="${param.loggedOut == 'true'}">
    <div class="status-banner" style="background:#f8fafc; border:1px solid #e2e8f0; color:#334155;">
      <div class="status-banner-content">
        <span class="status-banner-icon">👋</span>
        <div>You have been signed out successfully. Come back soon!</div>
      </div>
    </div>
  </c:if>

  <!-- Notice Banner for Database Setup if pending -->
  <c:if test="${not empty dbError}">
    <div class="status-banner warning">
      <div class="status-banner-content">
        <span class="status-banner-icon">ℹ</span>
        <div>
          <strong>Database configuration pending:</strong>
          <span>To load recipes from MySQL, set environment variables <code>RECIPEHUB_DB_USER</code> and <code>RECIPEHUB_DB_PASSWORD</code>.</span>
        </div>
      </div>
      <a href="${pageContext.request.contextPath}/" class="btn-secondary" style="font-size:12px; padding:6px 14px;">Refresh</a>
    </div>
  </c:if>

  <main>
    <!-- 3. Beautiful Two-Column Hero Section -->
    <section class="hero">
      <div class="hero-copy">
        <div class="editorial-kicker">
          <span class="spark">✦</span> CHEF CURATED · SIMPLE INGREDIENTS · BIG FLAVOURS
        </div>
        <h1>Discover recipes<br><em>you'll love.</em></h1>
        <p class="hero-subtitle">Explore foolproof, chef-tested recipes designed to make everyday home cooking deeply satisfying, approachable, and memorable.</p>

        <!-- Working Search Bar -->
        <form class="search-form" action="${pageContext.request.contextPath}/recipes" method="get">
          <span class="search-icon">⌕</span>
          <input
            type="text"
            name="q"
            class="search-input"
            value="<c:out value='${search}'/>"
            placeholder="Search dishes, fresh ingredients, or cravings..."
            aria-label="Search recipes"
          >
          <c:if test="${not empty selectedCategory}">
            <input type="hidden" name="category" value="<c:out value='${selectedCategory}'/>">
          </c:if>
          <c:if test="${not empty search}">
            <a href="${pageContext.request.contextPath}/recipes<c:if test='${not empty selectedCategory}'>?category=${selectedCategory}</c:if>" class="clear-search-btn" title="Clear search">✕</a>
          </c:if>
          <button type="submit" class="search-button">Find Recipes</button>
        </form>

        <!-- Trending Search Hints -->
        <div class="trending-pills">
          <span>Popular:</span>
          <a class="trending-pill-link" data-search-query="Pasta" href="${pageContext.request.contextPath}/recipes?q=Pasta">Tuscan Pasta</a>
          <a class="trending-pill-link" data-search-query="Avocado" href="${pageContext.request.contextPath}/recipes?q=Avocado">Avocado Toast</a>
          <a class="trending-pill-link" data-search-query="Quinoa" href="${pageContext.request.contextPath}/recipes?q=Quinoa">Quinoa Salad</a>
          <a class="trending-pill-link" data-search-query="Tiramisu" href="${pageContext.request.contextPath}/recipes?q=Tiramisu">Tiramisu</a>
          <a class="trending-pill-link" data-search-query="Squash" href="${pageContext.request.contextPath}/recipes?q=Squash">Buddha Bowl</a>
        </div>

        <!-- Trust Badges & Chef Proof -->
        <div class="hero-proof">
          <div class="avatars">
            <span>A</span>
            <span>E</span>
            <span>M</span>
          </div>
          <div class="proof-copy">
            <div>
              <span class="stars-rating">★★★★★</span>
              <strong>Loved by home cooks worldwide</strong>
            </div>
            <small>Chef-tested proportions & foolproof pantry ingredients</small>
          </div>
        </div>
      </div>

      <!-- Hero Visual Food Photography Frame -->
      <div class="hero-visual">
        <div class="hero-visual-frame">
          <img
            src="https://images.unsplash.com/photo-1498837167922-ddd27525d352?auto=format&fit=crop&w=1400&q=88"
            alt="Fresh colorful vegetables and kitchen ingredients"
          >
        </div>
        <a class="floating-card" href="${pageContext.request.contextPath}/recipe?id=1" title="View Featured Recipe">
          <div class="floating-icon">✦</div>
          <div>
            <b>Featured Recipe of the Week</b>
            <small>Creamy Tuscan Garlic Pasta ↗</small>
          </div>
        </a>
        <div class="floating-badge">
          SEASONAL EDIT <span class="spark">✳</span>
        </div>
      </div>
    </section>

    <!-- 4. Attractive Category Chips Navigation -->
    <section class="category-section" id="categories">
      <div class="section-header">
        <div>
          <div class="eyebrow"><span class="spark">✦</span> EXPLORE BY OCCASION</div>
          <h2>Curated <em>categories.</em></h2>
        </div>
        <a class="view-recipe-link" href="${pageContext.request.contextPath}/recipes#trending">
          View all recipes <span>→</span>
        </a>
      </div>

      <nav class="category-nav" aria-label="Recipe categories">
        <a class="category-chip ${empty selectedCategory ? 'active' : ''}" href="${pageContext.request.contextPath}/recipes#trending">
          <span>🍽</span> All Recipes
        </a>
        <a class="category-chip ${selectedCategory == 'breakfast' ? 'active' : ''}" href="${pageContext.request.contextPath}/recipes?category=breakfast#trending">
          <span>🍳</span> Breakfast
        </a>
        <a class="category-chip ${selectedCategory == 'lunch' ? 'active' : ''}" href="${pageContext.request.contextPath}/recipes?category=lunch#trending">
          <span>🥗</span> Lunch
        </a>
        <a class="category-chip ${selectedCategory == 'dinner' ? 'active' : ''}" href="${pageContext.request.contextPath}/recipes?category=dinner#trending">
          <span>🍝</span> Dinner
        </a>
        <a class="category-chip ${selectedCategory == 'vegetarian' ? 'active' : ''}" href="${pageContext.request.contextPath}/recipes?category=vegetarian#trending">
          <span>🥑</span> Vegetarian
        </a>
        <a class="category-chip ${selectedCategory == 'desserts' ? 'active' : ''}" href="${pageContext.request.contextPath}/recipes?category=desserts#trending">
          <span>🍓</span> Desserts
        </a>
      </nav>
    </section>

    <!-- 5. Trending This Week Section with Premium Recipe Cards -->
    <section class="recipes-section" id="trending">
      <div class="section-header">
        <div>
          <div class="eyebrow"><span class="spark">✳</span> CURATED SELECTION</div>
          <h2>Trending <em>this week.</em></h2>
        </div>
        <span class="servings-tag" style="font-size:13px; font-weight:600; color:var(--ink-muted);">
          <c:out value="${recipes.size()}"/> delicious recipes available
        </span>
      </div>

      <!-- Active Filter Banner if filtered -->
      <c:if test="${not empty search or not empty selectedCategory}">
        <div class="filter-feedback">
          <span>Active filter:</span>
          <c:if test="${not empty selectedCategory}">
            <span class="filter-badge">Category: <c:out value="${selectedCategory}"/></span>
          </c:if>
          <c:if test="${not empty search}">
            <span class="filter-badge">Query: &ldquo;<c:out value="${search}"/>&rdquo;</span>
          </c:if>
          <a class="clear-filter-link" href="${pageContext.request.contextPath}/recipes#trending">Reset all filters ×</a>
        </div>
      </c:if>

      <!-- Recipe Cards Grid -->
      <div class="recipe-grid">
        <c:forEach var="recipe" items="${recipes}">
          <article class="recipe-card">
            <div class="recipe-card-media">
              <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}">
                <img
                  src="<c:out value='${recipe.imageUrl}'/>"
                  alt="<c:out value='${recipe.title}'/>"
                  loading="lazy"
                >
              </a>
              <span class="card-category-badge"><c:out value="${recipe.category}"/></span>
              <button
                class="heart-button <c:if test='${not empty favoriteIds and favoriteIds.contains(recipe.id)}'>saved</c:if>"
                type="button"
                data-recipe-id="${recipe.id}"
                aria-label="Save ${recipe.title} to favourites"
                title="Save recipe"
              ><c:choose><c:when test="${not empty favoriteIds and favoriteIds.contains(recipe.id)}">♥</c:when><c:otherwise>♡</c:otherwise></c:choose></button>
            </div>
            <div class="recipe-card-body">
              <div class="recipe-card-meta">
                <span class="meta-pill">⏱ <c:out value="${recipe.totalMinutes}"/> min (<c:out value="${recipe.cookMinutes}"/>m cook)</span>
                <span class="meta-difficulty">✦ EASY PREP</span>
              </div>
              <h3 class="recipe-card-title">
                <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}">
                  <c:out value="${recipe.title}"/>
                </a>
              </h3>
              <p class="recipe-card-desc"><c:out value="${recipe.description}"/></p>
              <div class="recipe-card-footer">
                <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}" class="view-recipe-link">
                  View recipe <span>↗</span>
                </a>
                <span class="servings-tag">🍽 <c:out value="${recipe.servings}"/> servings</span>
              </div>
            </div>
          </article>
        </c:forEach>

        <!-- Empty State -->
        <c:if test="${empty recipes}">
          <div class="empty-state">
            <span class="empty-state-icon">🥣</span>
            <h3>No recipes found</h3>
            <p>We couldn't find any dishes matching your current search or category filter.</p>
            <a class="btn-secondary" href="${pageContext.request.contextPath}/recipes#trending">
              Clear filters and view all recipes →
            </a>
          </div>
        </c:if>
      </div>
    </section>

    <!-- 6A. Browse by Category Visual Cards -->
    <section class="browse-category-section">
      <div class="section-header">
        <div>
          <div class="eyebrow"><span class="spark">✦</span> DISCOVER INSPIRATION</div>
          <h2>Browse by <em>palate.</em></h2>
        </div>
      </div>
      <div class="category-visual-grid">
        <a class="category-card" href="${pageContext.request.contextPath}/recipes?category=breakfast#trending">
          <div class="category-card-icon">🍳</div>
          <h4>Breakfast</h4>
          <p>Bright toasts, bowls &amp; energizing morning dishes</p>
        </a>
        <a class="category-card" href="${pageContext.request.contextPath}/recipes?category=lunch#trending">
          <div class="category-card-icon">🥗</div>
          <h4>Lunch</h4>
          <p>Nutrient-dense grain bowls, fresh salads &amp; light bites</p>
        </a>
        <a class="category-card" href="${pageContext.request.contextPath}/recipes?category=dinner#trending">
          <div class="category-card-icon">🍝</div>
          <h4>Dinner</h4>
          <p>Comforting pastas, rich sauces &amp; hearty family meals</p>
        </a>
        <a class="category-card" href="${pageContext.request.contextPath}/recipes?category=vegetarian#trending">
          <div class="category-card-icon">🥑</div>
          <h4>Vegetarian</h4>
          <p>Wholesome, plant-powered meals bursting with colour</p>
        </a>
        <a class="category-card" href="${pageContext.request.contextPath}/recipes?category=desserts#trending">
          <div class="category-card-icon">🍓</div>
          <h4>Desserts</h4>
          <p>Classic Italian tiramisu, artisanal sweets &amp; bakes</p>
        </a>
      </div>
    </section>

    <!-- 6B. Quick & Easy Meals Section -->
    <section class="quick-easy-section" id="quick-easy">
      <div class="section-header">
        <div>
          <div class="eyebrow"><span class="spark">⏱</span> UNDER 20 MINUTES</div>
          <h2>Quick &amp; easy <em>weekdays.</em></h2>
        </div>
        <span class="quick-easy-badge">FAST &amp; FRESH</span>
      </div>

      <div class="recipe-grid">
        <c:forEach var="recipe" items="${recipes}">
          <c:if test="${recipe.totalMinutes <= 20}">
            <article class="recipe-card">
              <div class="recipe-card-media">
                <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}">
                  <img src="<c:out value='${recipe.imageUrl}'/>" alt="<c:out value='${recipe.title}'/>" loading="lazy">
                </a>
                <span class="card-category-badge"><c:out value="${recipe.category}"/></span>
                <button class="heart-button" type="button" data-recipe-id="${recipe.id}" aria-label="Save recipe">♡</button>
              </div>
              <div class="recipe-card-body">
                <div class="recipe-card-meta">
                  <span class="meta-pill" style="color:var(--terracotta);">⚡ Ready in <c:out value="${recipe.totalMinutes}"/> min</span>
                  <span class="meta-difficulty">✦ QUICK</span>
                </div>
                <h3 class="recipe-card-title">
                  <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}">
                    <c:out value="${recipe.title}"/>
                  </a>
                </h3>
                <p class="recipe-card-desc"><c:out value="${recipe.description}"/></p>
                <div class="recipe-card-footer">
                  <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}" class="view-recipe-link">
                    View recipe <span>↗</span>
                  </a>
                  <span class="servings-tag">🍽 <c:out value="${recipe.servings}"/> servings</span>
                </div>
              </div>
            </article>
          </c:if>
        </c:forEach>
      </div>
    </section>

    <!-- 6C. About RecipeHub Section -->
    <section class="about-section" id="about">
      <div class="about-grid">
        <div class="about-copy">
          <div class="eyebrow"><span class="spark">✦</span> OUR CULINARY PHILOSOPHY</div>
          <h2>Cooking made <em>effortless.</em></h2>
          <p>At RecipeHub, we believe great food shouldn't require complex restaurant techniques or obscure pantry staples. Every recipe is meticulously tested to ensure foolproof proportions, bright flavours, and accessible ingredients.</p>
          <div class="about-features">
            <div class="about-feat-item">
              <span class="about-feat-icon">✦</span>
              <div>
                <strong>Foolproof Testing</strong>
                <small>Tested repeatedly in real home kitchens.</small>
              </div>
            </div>
            <div class="about-feat-item">
              <span class="about-feat-icon">✦</span>
              <div>
                <strong>Accessible Staples</strong>
                <small>Fresh, seasonal, easy-to-find ingredients.</small>
              </div>
            </div>
            <div class="about-feat-item">
              <span class="about-feat-icon">✦</span>
              <div>
                <strong>Step-by-Step Clarity</strong>
                <small>Clear instructions and interactive checklists.</small>
              </div>
            </div>
            <div class="about-feat-item">
              <span class="about-feat-icon">✦</span>
              <div>
                <strong>Pure Satisfaction</strong>
                <small>Meals you will genuinely love sharing.</small>
              </div>
            </div>
          </div>
        </div>
        <div class="about-visual">
          <img
            src="https://images.unsplash.com/photo-1556910103-1c02745aae4d?auto=format&fit=crop&w=1200&q=85"
            alt="Chef preparing fresh herbs and dough in a kitchen"
          >
        </div>
      </div>
    </section>

    <!-- 6D. Cooking Inspiration / Newsletter Card -->
    <section class="newsletter">
      <div>
        <div class="eyebrow" style="margin-bottom:8px;"><span class="spark">✦</span> KITCHEN INSPIRATION</div>
        <h2>Your next memorable meal<br>starts <em>right here.</em></h2>
        <p>Get weekly curated seasonal dishes, chef tips, and fresh cooking inspiration delivered directly to your inbox.</p>
        <form class="newsletter-form" id="newsletterForm">
          <input type="email" class="newsletter-input" placeholder="Enter your email address..." required aria-label="Your email">
          <button type="submit" class="newsletter-btn">Subscribe Free</button>
        </form>
      </div>
      <a class="back-to-top-btn" href="#" onclick="window.scrollTo({top:0,behavior:'smooth'});return false;">
        Back to top ↑
      </a>
    </section>
  </main>

  <!-- 6E. Professional Footer -->
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
          <li><a href="#about">About Our Philosophy</a></li>
          <li><a href="#quick-easy">Quick &amp; Easy Meals</a></li>
          <li><a href="#trending">Trending Recipes</a></li>
          <li><a href="#" onclick="document.getElementById('navFavoritesBtn').click(); return false;">Saved Favorites</a></li>
        </ul>
      </div>

      <div class="footer-col">
        <h5>Cook With Us</h5>
        <ul>
          <li><a href="#newsletterForm">Weekly Inspiration</a></li>
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
