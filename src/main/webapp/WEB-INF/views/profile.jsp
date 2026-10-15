<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${profileUser.name}"/> — Chef Profile · RecipeHub</title>
  <meta name="description" content="Manage your RecipeHub profile, saved recipes, and culinary contributions.">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,400..700;1,9..40,400..600&family=Playfair+Display:ital,wght@0,500..800;1,400..700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=2.3">

  <style>
    .profile-hero {
      background: linear-gradient(135deg, #183728 0%, #224b37 100%);
      color: #ffffff;
      padding: 56px 5% 48px;
      position: relative;
    }
    .profile-hero-inner {
      max-width: 1180px;
      margin: 0 auto;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 32px;
      flex-wrap: wrap;
    }
    .profile-avatar-row {
      display: flex;
      align-items: center;
      gap: 24px;
    }
    .profile-avatar-badge {
      width: 90px;
      height: 90px;
      border-radius: 50%;
      background: #d4a373;
      color: #183728;
      font-family: var(--font-serif, Georgia, serif);
      font-size: 40px;
      font-weight: 700;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 4px solid rgba(255, 255, 255, 0.25);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
      flex-shrink: 0;
    }
    .profile-info h1 {
      font-family: var(--font-serif, Georgia, serif);
      font-size: 32px;
      color: #ffffff;
      margin: 0 0 6px 0;
      letter-spacing: -0.5px;
    }
    .profile-email {
      font-size: 14.5px;
      color: #cbd5e1;
      display: flex;
      align-items: center;
      gap: 6px;
      margin-bottom: 10px;
    }
    .profile-bio-text {
      font-size: 14.5px;
      color: #e2e8f0;
      line-height: 1.5;
      max-width: 580px;
      margin: 0 0 10px 0;
      font-style: italic;
    }
    .profile-badges {
      display: flex;
      gap: 10px;
      align-items: center;
      flex-wrap: wrap;
    }
    .pill-badge {
      display: inline-flex;
      align-items: center;
      gap: 5px;
      padding: 4px 12px;
      border-radius: 999px;
      font-size: 12px;
      font-weight: 600;
      letter-spacing: 0.04em;
      text-transform: uppercase;
    }
    .pill-badge.role {
      background: rgba(212, 163, 115, 0.25);
      color: #faedcd;
      border: 1px solid rgba(212, 163, 115, 0.4);
    }
    .pill-badge.date {
      background: rgba(255, 255, 255, 0.12);
      color: #f1f5f9;
      border: 1px solid rgba(255, 255, 255, 0.2);
    }
    .profile-actions {
      display: flex;
      gap: 12px;
      align-items: center;
    }
    .btn-edit-profile {
      background: #ffffff;
      color: #183728;
      border: 0;
      padding: 11px 22px;
      border-radius: 999px;
      font-weight: 600;
      font-size: 13.5px;
      cursor: pointer;
      text-decoration: none;
      display: inline-flex;
      align-items: center;
      gap: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      transition: all 0.2s ease;
    }
    .btn-edit-profile:hover {
      background: #f8fafc;
      transform: translateY(-1px);
    }
    .btn-profile-logout {
      background: transparent;
      color: #fee2e2;
      border: 1px solid rgba(254, 226, 226, 0.35);
      padding: 10px 20px;
      border-radius: 999px;
      font-weight: 600;
      font-size: 13.5px;
      cursor: pointer;
      text-decoration: none;
      display: inline-flex;
      align-items: center;
      gap: 6px;
      transition: all 0.2s ease;
    }
    .btn-profile-logout:hover {
      background: rgba(239, 68, 68, 0.2);
      border-color: #ef4444;
      color: #ffffff;
    }

    /* Stats strip */
    .profile-stats-strip {
      background: #ffffff;
      border-bottom: 1px solid var(--border-editorial, #e7ded4);
      padding: 16px 5%;
    }
    .profile-stats-inner {
      max-width: 1180px;
      margin: 0 auto;
      display: flex;
      gap: 40px;
      align-items: center;
    }
    .profile-stat-box {
      display: flex;
      flex-direction: column;
    }
    .profile-stat-num {
      font-family: var(--font-serif, Georgia, serif);
      font-size: 24px;
      font-weight: 700;
      color: #183728;
      line-height: 1.1;
    }
    .profile-stat-lbl {
      font-size: 12px;
      text-transform: uppercase;
      letter-spacing: 0.08em;
      color: #718096;
      font-weight: 600;
    }

    /* Tab switcher for profile */
    .profile-tab-nav {
      max-width: 1180px;
      margin: 32px auto 24px;
      padding: 0 5%;
      display: flex;
      gap: 12px;
      border-bottom: 2px solid #e7ded4;
    }
    .profile-tab-btn {
      background: transparent;
      border: 0;
      border-bottom: 3px solid transparent;
      padding: 12px 18px;
      font-size: 15px;
      font-weight: 600;
      color: #5f6b64;
      cursor: pointer;
      margin-bottom: -2px;
      transition: all 0.18s ease;
      font-family: inherit;
    }
    .profile-tab-btn:hover {
      color: #183728;
    }
    .profile-tab-btn.active {
      color: #183728;
      border-bottom-color: #183728;
    }

    .profile-content-area {
      max-width: 1180px;
      margin: 0 auto 64px;
      padding: 0 5%;
    }
    .profile-section-panel {
      display: none;
    }
    .profile-section-panel.active {
      display: block;
    }

    /* Saved recipe card with remove button */
    .saved-recipe-card {
      background: #ffffff;
      border: 1px solid var(--border-editorial, #e7ded4);
      border-radius: 14px;
      overflow: hidden;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
      display: flex;
      flex-direction: column;
      transition: transform 0.2s ease, box-shadow 0.2s ease;
    }
    .saved-recipe-card:hover {
      transform: translateY(-3px);
      box-shadow: 0 10px 24px rgba(24, 55, 40, 0.08);
    }
    .saved-card-media {
      position: relative;
      height: 200px;
      overflow: hidden;
    }
    .saved-card-media img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }
    .saved-recipe-card:hover .saved-card-media img {
      transform: scale(1.04);
    }
    .saved-card-body {
      padding: 20px;
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }
    .saved-card-category {
      font-size: 11px;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      color: #c45d3c;
      margin-bottom: 6px;
    }
    .saved-card-title {
      font-family: var(--font-serif, Georgia, serif);
      font-size: 19px;
      color: #183728;
      margin: 0 0 8px 0;
      line-height: 1.3;
    }
    .saved-card-meta {
      font-size: 13px;
      color: #718096;
      margin-bottom: 16px;
      display: flex;
      gap: 16px;
    }
    .saved-card-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 14px;
      border-top: 1px solid #f1ece4;
    }
    .btn-view-recipe {
      color: #183728;
      font-weight: 700;
      font-size: 13.5px;
      text-decoration: none;
    }
    .btn-view-recipe:hover {
      text-decoration: underline;
    }
    .btn-remove-saved {
      background: #fef2f2;
      color: #991b1b;
      border: 1px solid #fecaca;
      border-radius: 999px;
      padding: 6px 12px;
      font-size: 12px;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.15s ease;
    }
    .btn-remove-saved:hover {
      background: #fee2e2;
    }

    /* Edit Profile Form Card */
    .edit-profile-card {
      background: #ffffff;
      border: 1px solid var(--border-editorial, #e7ded4);
      border-radius: 16px;
      padding: 36px 40px;
      max-width: 620px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.04);
    }

    /* Empty state */
    .empty-culinary-state {
      background: #ffffff;
      border: 2px dashed #ded4c7;
      border-radius: 16px;
      padding: 48px 32px;
      text-align: center;
      max-width: 580px;
      margin: 24px auto;
    }
    .empty-culinary-icon {
      font-size: 48px;
      margin-bottom: 16px;
    }
    .empty-culinary-title {
      font-family: var(--font-serif, Georgia, serif);
      font-size: 22px;
      color: #183728;
      margin: 0 0 8px 0;
    }
    .empty-culinary-desc {
      color: #718096;
      font-size: 14px;
      line-height: 1.6;
      margin: 0 0 20px 0;
    }

    @media (max-width: 768px) {
      .profile-hero {
        padding: 36px 5% 32px;
      }
      .profile-hero-inner {
        flex-direction: column;
        align-items: flex-start;
      }
      .profile-avatar-row {
        flex-direction: column;
        align-items: flex-start;
      }
      .profile-actions {
        width: 100%;
        justify-content: flex-start;
      }
      .edit-profile-card {
        padding: 24px 18px;
      }
    }
  </style>
</head>
<body>

  <!-- Topbar Navigation -->
  <header class="topbar">
    <div class="brand-wrapper">
      <a class="brand" href="${pageContext.request.contextPath}/">
        <span class="brand-mark">R</span> recipe<span>hub</span>
      </a>
      <span class="brand-edition-tag">MEMBER KITCHEN</span>
    </div>

    <nav class="main-nav" aria-label="Main Navigation">
      <a href="${pageContext.request.contextPath}/">Home</a>
      <a href="${pageContext.request.contextPath}/recipes">Explore Recipes</a>
      <a href="${pageContext.request.contextPath}/#categories">Categories</a>
      <a href="${pageContext.request.contextPath}/#about">About</a>
    </nav>

    <div class="nav-actions">
      <div class="user-profile-menu">
        <span class="user-avatar">👤</span>
        <span class="user-name" title="Signed in as <c:out value='${profileUser.name}'/>">
          <c:out value="${profileUser.name}"/>
        </span>
        <a class="nav-logout-btn" href="${pageContext.request.contextPath}/logout" title="Sign Out">
          Sign Out
        </a>
      </div>
    </div>
  </header>

  <!-- Flash Messages -->
  <c:if test="${not empty sessionScope.flashMessage}">
    <div class="status-banner" style="background:#f0fdf4; border-bottom:1px solid #bbf7d0; color:#166534; padding:12px 5%; text-align:center;">
      <strong>✓ <c:out value="${sessionScope.flashMessage}"/></strong>
    </div>
    <c:remove var="flashMessage" scope="session"/>
  </c:if>

  <c:if test="${not empty error}">
    <div class="status-banner" style="background:#fef2f2; border-bottom:1px solid #fecaca; color:#991b1b; padding:12px 5%; text-align:center;">
      <strong>⚠️ <c:out value="${error}"/></strong>
    </div>
  </c:if>

  <!-- Profile Hero Section -->
  <section class="profile-hero">
    <div class="profile-hero-inner">
      <div class="profile-avatar-row">
        <div class="profile-avatar-badge" title="<c:out value='${profileUser.name}'/>">
          <c:out value="${profileUser.initial}"/>
        </div>
        <div class="profile-info">
          <h1><c:out value="${profileUser.name}"/></h1>
          <div class="profile-email">
            <span>✉</span> <c:out value="${profileUser.email}"/>
          </div>
          <p class="profile-bio-text">
            <c:choose>
              <c:when test="${not empty profileUser.bio}">
                "<c:out value="${profileUser.bio}"/>"
              </c:when>
              <c:otherwise>
                "Home cook passionate about fresh seasonal ingredients and extraordinary flavours."
              </c:otherwise>
            </c:choose>
          </p>
          <div class="profile-badges">
            <span class="pill-badge role">
              ✦ <c:out value="${profileUser.role == 'ADMIN' ? 'Master Chef / Admin' : 'Verified Home Cook'}"/>
            </span>
            <span class="pill-badge date">
              Joined RecipeHub
            </span>
          </div>
        </div>
      </div>

      <div class="profile-actions">
        <button type="button" class="btn-edit-profile" onclick="switchProfileTab('edit')">
          ✎ Edit Profile
        </button>
        <a href="${pageContext.request.contextPath}/logout" class="btn-profile-logout">
          Sign Out →
        </a>
      </div>
    </div>
  </section>

  <!-- Profile Stats Strip -->
  <section class="profile-stats-strip">
    <div class="profile-stats-inner">
      <div class="profile-stat-box">
        <span class="profile-stat-num"><c:out value="${savedCount}"/></span>
        <span class="profile-stat-lbl">Saved Recipes</span>
      </div>
      <div class="profile-stat-box">
        <span class="profile-stat-num"><c:out value="${contributedCount}"/></span>
        <span class="profile-stat-lbl">My Recipes</span>
      </div>
      <div class="profile-stat-box">
        <span class="profile-stat-num">Active</span>
        <span class="profile-stat-lbl">Kitchen Status</span>
      </div>
    </div>
  </section>

  <!-- Profile Tab Navigation -->
  <nav class="profile-tab-nav" role="tablist">
    <button
      type="button"
      id="tabBtnSaved"
      class="profile-tab-btn ${not isEditMode ? 'active' : ''}"
      onclick="switchProfileTab('saved')"
      role="tab"
    >
      ♥ Saved Recipes (<c:out value="${savedCount}"/>)
    </button>
    <button
      type="button"
      id="tabBtnMy"
      class="profile-tab-btn"
      onclick="switchProfileTab('my')"
      role="tab"
    >
      👨‍🍳 My Recipes (<c:out value="${contributedCount}"/>)
    </button>
    <button
      type="button"
      id="tabBtnEdit"
      class="profile-tab-btn ${isEditMode ? 'active' : ''}"
      onclick="switchProfileTab('edit')"
      role="tab"
    >
      ✎ Account Settings &amp; Bio
    </button>
  </nav>

  <!-- Main Profile Content Area -->
  <main class="profile-content-area">

    <!-- TAB 1: SAVED RECIPES -->
    <div id="panelSaved" class="profile-section-panel ${not isEditMode ? 'active' : ''}">
      <c:choose>
        <c:when test="${not empty savedRecipes}">
          <div class="recipe-grid" style="display:grid; grid-template-columns:repeat(auto-fill, minmax(320px, 1fr)); gap:24px;">
            <c:forEach var="recipe" items="${savedRecipes}">
              <article class="saved-recipe-card">
                <div class="saved-card-media">
                  <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}">
                    <img src="<c:out value='${recipe.imageUrl}'/>" alt="<c:out value='${recipe.title}'/>" loading="lazy">
                  </a>
                </div>
                <div class="saved-card-body">
                  <div>
                    <div class="saved-card-category"><c:out value="${recipe.category}"/></div>
                    <h3 class="saved-card-title">
                      <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}" style="color:inherit; text-decoration:none;">
                        <c:out value="${recipe.title}"/>
                      </a>
                    </h3>
                    <div class="saved-card-meta">
                      <span>⏱ <c:out value="${recipe.totalMinutes}"/> mins</span>
                      <span>🍽 <c:out value="${recipe.servings}"/> servings</span>
                    </div>
                  </div>
                  <div class="saved-card-actions">
                    <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}" class="btn-view-recipe">
                      View Recipe →
                    </a>
                    <form action="${pageContext.request.contextPath}/favorite/toggle" method="post" style="margin:0;">
                      <input type="hidden" name="recipeId" value="${recipe.id}">
                      <button type="submit" class="btn-remove-saved" title="Remove from your saved list">
                        ✕ Remove
                      </button>
                    </form>
                  </div>
                </div>
              </article>
            </c:forEach>
          </div>
        </c:when>
        <c:otherwise>
          <div class="empty-culinary-state">
            <div class="empty-culinary-icon">📖</div>
            <h3 class="empty-culinary-title">Your Recipe Box is Empty</h3>
            <p class="empty-culinary-desc">
              You haven't saved any recipes to your favorites yet. When you discover a recipe you love, click the heart icon to save it for easy access anytime.
            </p>
            <a href="${pageContext.request.contextPath}/recipes" class="auth-submit-btn" style="text-decoration:none; display:inline-flex; width:auto; padding:12px 28px;">
              Explore All Recipes →
            </a>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- TAB 2: MY RECIPES -->
    <div id="panelMy" class="profile-section-panel">
      <c:choose>
        <c:when test="${not empty myRecipes}">
          <div class="recipe-grid" style="display:grid; grid-template-columns:repeat(auto-fill, minmax(320px, 1fr)); gap:24px;">
            <c:forEach var="recipe" items="${myRecipes}">
              <article class="saved-recipe-card">
                <div class="saved-card-media">
                  <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}">
                    <img src="<c:out value='${recipe.imageUrl}'/>" alt="<c:out value='${recipe.title}'/>" loading="lazy">
                  </a>
                </div>
                <div class="saved-card-body">
                  <div>
                    <div class="saved-card-category"><c:out value="${recipe.category}"/></div>
                    <h3 class="saved-card-title">
                      <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}" style="color:inherit; text-decoration:none;">
                        <c:out value="${recipe.title}"/>
                      </a>
                    </h3>
                    <div class="saved-card-meta">
                      <span>⏱ <c:out value="${recipe.totalMinutes}"/> mins</span>
                      <span>🍽 <c:out value="${recipe.servings}"/> servings</span>
                    </div>
                  </div>
                  <div class="saved-card-actions">
                    <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}" class="btn-view-recipe">
                      View Recipe →
                    </a>
                    <span style="font-size:12px; color:#166534; font-weight:600;">✓ Contributed</span>
                  </div>
                </div>
              </article>
            </c:forEach>
          </div>
        </c:when>
        <c:otherwise>
          <div class="empty-culinary-state">
            <div class="empty-culinary-icon">🥣</div>
            <h3 class="empty-culinary-title">No Contributed Recipes Yet</h3>
            <p class="empty-culinary-desc">
              You haven't published any personal recipes yet. Browse our curated seasonal collection or bookmark recipes to your saved list.
            </p>
            <a href="${pageContext.request.contextPath}/recipes" class="auth-submit-btn" style="text-decoration:none; display:inline-flex; width:auto; padding:12px 28px;">
              Browse Recipes →
            </a>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- TAB 3: EDIT PROFILE -->
    <div id="panelEdit" class="profile-section-panel ${isEditMode ? 'active' : ''}">
      <div class="edit-profile-card">
        <h2 style="font-family:var(--font-serif, Georgia, serif); color:#183728; font-size:22px; margin:0 0 8px 0;">
          Edit Your Culinary Profile
        </h2>
        <p style="color:#718096; font-size:14px; margin:0 0 24px 0;">
          Update your public member name and kitchen bio shown across RecipeHub.
        </p>

        <form action="${pageContext.request.contextPath}/profile" method="post" novalidate>
          <div class="form-group" style="margin-bottom:18px;">
            <label class="form-label" for="profileName">Full Name</label>
            <div class="form-input-wrapper">
              <span class="input-icon">👤</span>
              <input
                type="text"
                id="profileName"
                name="name"
                class="form-input"
                value="<c:out value='${profileUser.name}'/>"
                required
                minlength="2"
                maxlength="100"
              >
            </div>
            <small class="form-hint" style="color:#718096; font-size:12px; margin-top:4px;">Minimum 2 characters.</small>
          </div>

          <div class="form-group" style="margin-bottom:18px;">
            <label class="form-label" for="profileEmailStatic">Registered Email</label>
            <div class="form-input-wrapper">
              <span class="input-icon">✉</span>
              <input
                type="text"
                id="profileEmailStatic"
                class="form-input"
                value="<c:out value='${profileUser.email}'/>"
                disabled
                style="background:#f1eee8; color:#718096; cursor:not-allowed;"
              >
            </div>
            <small class="form-hint" style="color:#718096; font-size:12px; margin-top:4px;">Email address cannot be modified once registered.</small>
          </div>

          <div class="form-group" style="margin-bottom:24px;">
            <label class="form-label" for="profileBio">Kitchen Bio &amp; Passions</label>
            <textarea
              id="profileBio"
              name="bio"
              class="form-input"
              rows="4"
              maxlength="300"
              placeholder="Tell other home cooks about your culinary style, favourite cuisines, or secret ingredients..."
              style="resize:vertical; padding:12px 14px; font-family:inherit;"
            ><c:out value="${profileUser.bio}"/></textarea>
            <small class="form-hint" style="color:#718096; font-size:12px; margin-top:4px;">Maximum 300 characters.</small>
          </div>

          <div style="display:flex; gap:12px; align-items:center;">
            <button type="submit" class="auth-submit-btn" style="width:auto; padding:12px 28px;">
              Save Changes <span>✓</span>
            </button>
            <button type="button" class="auth-tab-btn" onclick="switchProfileTab('saved')" style="padding:12px 20px;">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>

  </main>

  <!-- Footer -->
  <footer>
    <div class="footer-bottom">
      <span>© RecipeHub. Handcrafted with care for home cooks everywhere.</span>
      <span>Safe Enterprise Architecture · Jakarta EE 10</span>
    </div>
  </footer>

  <script>
    function switchProfileTab(tabName) {
      document.querySelectorAll('.profile-tab-btn').forEach(b => b.classList.remove('active'));
      document.querySelectorAll('.profile-section-panel').forEach(p => p.classList.remove('active'));

      if (tabName === 'edit') {
        document.getElementById('tabBtnEdit')?.classList.add('active');
        document.getElementById('panelEdit')?.classList.add('active');
      } else if (tabName === 'my') {
        document.getElementById('tabBtnMy')?.classList.add('active');
        document.getElementById('panelMy')?.classList.add('active');
      } else {
        document.getElementById('tabBtnSaved')?.classList.add('active');
        document.getElementById('panelSaved')?.classList.add('active');
      }
    }

    // Auto-switch to edit tab if hash or query param requested
    document.addEventListener('DOMContentLoaded', function () {
      if (window.location.hash === '#edit') {
        switchProfileTab('edit');
      } else if (window.location.hash === '#my') {
        switchProfileTab('my');
      } else if (window.location.hash === '#saved') {
        switchProfileTab('saved');
      }
    });
  </script>
</body>
</html>
