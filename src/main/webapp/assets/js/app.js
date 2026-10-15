// RecipeHub Progressive Enhancements & Platform Interactions
document.addEventListener('DOMContentLoaded', () => {
  // Context path detection
  const contextPath = window.location.pathname.startsWith('/recipehub') ? '/recipehub' : '';

  // 1. Saved Recipes (LocalStorage & Server synchronization)
  const STORAGE_KEY = 'recipehub_saved_recipes';
  let savedIds = [];
  try {
    savedIds = JSON.parse(localStorage.getItem(STORAGE_KEY) || '[]');
  } catch (e) {
    savedIds = [];
  }

  // Sync server-rendered saved status into savedIds
  document.querySelectorAll('.heart-button.saved').forEach((btn) => {
    const id = String(btn.dataset.recipeId || '');
    if (id && !savedIds.includes(id)) {
      savedIds.push(id);
    }
  });

  function updateFavoritesCount() {
    const badge = document.getElementById('favoritesCount');
    if (badge) {
      badge.textContent = savedIds.length;
      badge.style.display = savedIds.length > 0 ? 'inline-block' : 'none';
    }
  }
  updateFavoritesCount();

  // Update initial heart button states
  document.querySelectorAll('.heart-button').forEach((btn) => {
    const id = String(btn.dataset.recipeId || '');
    if (id && savedIds.includes(id)) {
      btn.classList.add('saved');
      btn.textContent = '♥';
      btn.setAttribute('aria-pressed', 'true');
    }

    btn.addEventListener('click', async (e) => {
      e.preventDefault();
      e.stopPropagation();
      const recipeId = String(btn.dataset.recipeId || '');
      if (!recipeId) return;

      // Try backend synchronization if user is logged in
      try {
        const resp = await fetch(contextPath + '/favorite/toggle', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest',
            'Accept': 'application/json'
          },
          body: 'recipeId=' + encodeURIComponent(recipeId)
        });

        if (resp.status === 401) {
          showToast('Please sign in to save recipes to your profile');
          setTimeout(() => {
            window.location.href = contextPath + '/login';
          }, 1200);
          return;
        }

        const data = await resp.json();
        if (data && data.success) {
          const isFav = !!data.favorite;
          document.querySelectorAll('.heart-button[data-recipe-id="' + recipeId + '"]').forEach(b => {
            if (isFav) {
              b.classList.add('saved');
              b.textContent = '♥';
              b.setAttribute('aria-pressed', 'true');
            } else {
              b.classList.remove('saved');
              b.textContent = '♡';
              b.setAttribute('aria-pressed', 'false');
            }
          });

          if (isFav) {
            if (!savedIds.includes(recipeId)) savedIds.push(recipeId);
            showToast('Saved to your profile favorites ♥');
          } else {
            savedIds = savedIds.filter(item => item !== recipeId);
            showToast('Removed from favorites');
          }
          try { localStorage.setItem(STORAGE_KEY, JSON.stringify(savedIds)); } catch(err) {}
          updateFavoritesCount();
          return;
        }
      } catch (networkErr) {
        // Fall through to client-side toggle if network unavailable
      }

      // Fallback client-only toggle for offline/unauthenticated guests
      const isSaved = btn.classList.contains('saved');
      if (isSaved) {
        btn.classList.remove('saved');
        btn.textContent = '♡';
        btn.setAttribute('aria-pressed', 'false');
        savedIds = savedIds.filter(item => item !== recipeId);
        showToast('Removed from favourites');
      } else {
        btn.classList.add('saved');
        btn.textContent = '♥';
        btn.setAttribute('aria-pressed', 'true');
        if (recipeId && !savedIds.includes(recipeId)) {
          savedIds.push(recipeId);
        }
        showToast('Saved to your favourites ♥');
      }

      try {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(savedIds));
      } catch (err) {}
      updateFavoritesCount();
    });
  });

  // 2. Favorites Modal / Drawer
  const favBtn = document.getElementById('navFavoritesBtn');
  const favModal = document.getElementById('favoritesModal');
  const favModalList = document.getElementById('favoritesModalList');
  if (favBtn && favModal) {
    favBtn.addEventListener('click', () => {
      if (savedIds.length === 0) {
        favModalList.innerHTML = '<p style="color:var(--ink-muted); text-align:center; padding:20px 0;">You have not saved any recipes yet. Click the ♡ heart on any recipe card to save it here!</p>';
      } else {
        // Collect recipe cards that match saved IDs
        let html = '<div style="display:flex; flex-direction:column; gap:12px; margin-top:16px;">';
        document.querySelectorAll('.recipe-card').forEach((card) => {
          const heart = card.querySelector('.heart-button');
          if (heart && savedIds.includes(String(heart.dataset.recipeId))) {
            const titleEl = card.querySelector('.recipe-card-title a');
            const link = titleEl ? titleEl.getAttribute('href') : '#';
            const title = titleEl ? titleEl.textContent.trim() : 'Recipe';
            const categoryEl = card.querySelector('.card-category-badge');
            const category = categoryEl ? categoryEl.textContent.trim() : '';
            html += `<a href="${link}" style="display:flex; align-items:center; justify-content:space-between; padding:12px 16px; background:var(--bg-ivory); border:1px solid var(--border-editorial); border-radius:12px; transition:0.2s;">
              <div>
                <strong style="color:var(--ink-primary); font-size:15px; display:block;">${title}</strong>
                <small style="color:var(--forest-700); font-size:11px; font-weight:700; text-transform:uppercase;">${category}</small>
              </div>
              <span style="color:var(--forest-700); font-weight:700;">View →</span>
            </a>`;
          }
        });
        html += '</div>';
        favModalList.innerHTML = html;
      }
      favModal.classList.add('active');
    });
  }



  // Modal Closers
  document.querySelectorAll('.modal-close-btn, .modal-overlay').forEach((el) => {
    el.addEventListener('click', (e) => {
      if (e.target === el || el.classList.contains('modal-close-btn')) {
        document.querySelectorAll('.modal-overlay').forEach(m => m.classList.remove('active'));
      }
    });
  });

  // 4. Quick Trending Pills on Hero
  document.querySelectorAll('.trending-pill-link').forEach((pill) => {
    pill.addEventListener('click', (e) => {
      const term = pill.dataset.searchQuery;
      const searchForm = document.querySelector('.search-form');
      const searchInput = document.querySelector('.search-input');
      if (term && searchForm && searchInput) {
        e.preventDefault();
        searchInput.value = term;
        searchForm.submit();
      }
    });
  });

  // 5. Ingredient Checklist Interactivity
  document.querySelectorAll('.ingredient-checkbox').forEach((checkbox) => {
    checkbox.addEventListener('change', () => {
      const parent = checkbox.closest('.ingredient-item');
      if (parent) {
        parent.classList.toggle('checked', checkbox.checked);
      }
    });
  });

  // 6. Newsletter Subscription
  const newsletterForm = document.getElementById('newsletterForm');
  if (newsletterForm) {
    newsletterForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const input = newsletterForm.querySelector('input[type="email"]');
      if (input && input.value.trim()) {
        showToast('Thank you for subscribing to RecipeHub Inspiration!');
        input.value = '';
      }
    });
  }

  // 7. Share & Print Recipe Handlers
  const shareBtn = document.getElementById('shareRecipeBtn');
  if (shareBtn) {
    shareBtn.addEventListener('click', async () => {
      if (navigator.clipboard) {
        try {
          await navigator.clipboard.writeText(window.location.href);
          showToast('Recipe link copied to clipboard!');
        } catch (err) {
          showToast('Share link ready: ' + window.location.href);
        }
      } else {
        showToast('Share link ready: ' + window.location.href);
      }
    });
  }

  const printBtn = document.getElementById('printRecipeBtn');
  if (printBtn) {
    printBtn.addEventListener('click', () => {
      window.print();
    });
  }

  // 8. Image Loading Error Fallbacks (ensure images do not break)
  document.querySelectorAll('img').forEach((img) => {
    img.addEventListener('error', () => {
      const parent = img.parentElement;
      if (parent) {
        parent.classList.add('image-unavailable');
      }
      img.style.display = 'none';
    });
  });
});

// Toast notification helper
function showToast(message) {
  let toast = document.querySelector('.toast-notice');
  if (!toast) {
    toast = document.createElement('div');
    toast.className = 'toast-notice';
    document.body.appendChild(toast);
  }
  toast.textContent = message;
  toast.classList.add('show');
  clearTimeout(window._toastTimeout);
  window._toastTimeout = setTimeout(() => {
    toast.classList.remove('show');
  }, 3000);
}
