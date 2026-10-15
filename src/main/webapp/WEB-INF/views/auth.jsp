<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="activeMode" value="${not empty mode ? mode : 'login'}" />
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Account Authentication — RecipeHub</title>
  <meta name="description" content="Sign in or register for RecipeHub. Save your favourite recipes, organize weekly meals, and join our culinary community.">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,400..700;1,9..40,400..600&family=Playfair+Display:ital,wght@0,500..800;1,400..700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=2.4">

  <!-- Embedded Critical Authentication Styles (strictly single-form display) -->
  <style>
    .auth-form {
      display: none !important;
    }
    .auth-form.active {
      display: flex !important;
      flex-direction: column;
      gap: 18px;
    }
    .auth-form[hidden] {
      display: none !important;
      visibility: hidden !important;
    }

    .auth-card {
      position: relative;
      background: #ffffff;
      border: 1px solid var(--border-editorial, #e7ded4);
      border-radius: var(--radius-xl, 20px);
      padding: 42px 46px;
      max-width: 490px;
      width: 100%;
      margin: 0 auto;
      box-shadow: 0 16px 36px -8px rgba(24, 55, 40, 0.08), 0 4px 12px rgba(0, 0, 0, 0.04);
      transition: all 0.25s ease;
    }

    .auth-tabs {
      display: flex;
      background: #f2eee8;
      border: 1px solid #e5ded4;
      border-radius: 999px;
      padding: 4px;
      margin-bottom: 24px;
      gap: 2px;
    }

    .auth-tab-btn {
      flex: 1;
      border: 0;
      background: transparent;
      padding: 9px 12px;
      border-radius: 999px;
      font-size: 13px;
      font-weight: 600;
      font-family: inherit;
      color: #5f6b64;
      cursor: pointer;
      transition: all 0.18s ease;
      white-space: nowrap;
      text-align: center;
      text-decoration: none;
    }

    .auth-tab-btn:hover {
      color: #183728;
    }

    .auth-tab-btn.active {
      background: #ffffff;
      color: #183728;
      box-shadow: 0 2px 8px rgba(24, 55, 40, 0.08);
    }

    .form-input-static {
      background: #f4f1ea;
      border: 1px solid #e5ded4;
      border-radius: var(--radius-sm, 8px);
      padding: 10px 14px;
      font-size: 14px;
      color: #183728;
      font-weight: 600;
    }

    .btn-resend-otp {
      background: transparent;
      border: 0;
      color: #c45d3c;
      font-weight: 600;
      font-size: 13px;
      cursor: pointer;
      padding: 6px 12px;
      border-radius: 999px;
      transition: all 0.15s ease;
      font-family: inherit;
    }

    .btn-resend-otp:hover:not(:disabled) {
      color: #9c3f22;
      text-decoration: underline;
    }

    .btn-resend-otp:disabled {
      color: #94a3b8;
      cursor: not-allowed;
    }

    .auth-footer-prompt {
      margin-top: 14px;
      padding-top: 16px;
      border-top: 1px solid #f0e9df;
      text-align: center;
      font-size: 13.5px;
      color: #5f6b64;
      line-height: 1.6;
    }

    .auth-footer-prompt a {
      color: #c45d3c;
      font-weight: 600;
      text-decoration: none;
      transition: color 0.15s ease;
    }

    .auth-footer-prompt a:hover {
      color: #9c3f22;
      text-decoration: underline;
    }

    .auth-sub-links {
      display: flex;
      justify-content: space-between;
      gap: 10px;
      margin-top: 8px;
      font-size: 12.5px;
    }

    @media (max-width: 540px) {
      .auth-card {
        padding: 28px 20px;
        border-radius: 16px;
      }
      .auth-tabs {
        padding: 3px;
      }
      .auth-tab-btn {
        padding: 8px 4px;
        font-size: 11.5px;
      }
      .auth-header h2 {
        font-size: 24px;
      }
      .auth-sub-links {
        flex-direction: column;
        align-items: center;
        gap: 6px;
      }
    }
  </style>
</head>
<body class="auth-body">

  <!-- Header -->
  <header class="topbar">
    <div class="brand-wrapper">
      <a class="brand" href="${pageContext.request.contextPath}/">
        <span class="brand-mark">R</span> recipe<span>hub</span>
      </a>
      <span class="brand-edition-tag">MEMBER ACCESS</span>
    </div>
    <a href="${pageContext.request.contextPath}/" class="back-link">
      ← Back to Recipes
    </a>
  </header>

  <!-- Main Card Container -->
  <main class="auth-main-wrapper">
    <div class="auth-card">

      <!-- Eyebrow & Headline -->
      <div class="auth-header">
        <div class="eyebrow" style="justify-content:center;"><span class="spark">✦</span> THE RECIPEHUB KITCHEN</div>
        <h2 id="authHeading">
          <c:choose>
            <c:when test="${activeMode == 'register'}">Create your account</c:when>
            <c:when test="${activeMode == 'forgot'}">
              <c:choose>
                <c:when test="${step == 'verifyOtp'}">Verify Verification Code</c:when>
                <c:when test="${step == 'setNewPassword'}">Set New Password</c:when>
                <c:otherwise>Reset your password</c:otherwise>
              </c:choose>
            </c:when>
            <c:otherwise>Welcome back to RecipeHub</c:otherwise>
          </c:choose>
        </h2>
        <p class="auth-subtext" id="authSubtext">
          <c:choose>
            <c:when test="${activeMode == 'register'}">Join home cooks discovering and sharing culinary joy.</c:when>
            <c:when test="${activeMode == 'forgot'}">
              <c:choose>
                <c:when test="${step == 'verifyOtp'}">Enter the 6-digit code sent to your email to confirm your identity.</c:when>
                <c:when test="${step == 'setNewPassword'}">Choose a new, secure password for your RecipeHub account.</c:when>
                <c:otherwise>Enter your email to receive a secure 6-digit OTP code.</c:otherwise>
              </c:choose>
            </c:when>
            <c:otherwise>Sign in to access your saved recipes and weekly kitchen edit.</c:otherwise>
          </c:choose>
        </p>
      </div>

      <!-- Server-Side Alerts -->
      <c:if test="${not empty error}">
        <div class="auth-alert error" role="alert">
          <span class="alert-icon">⚠️</span>
          <div class="alert-text"><c:out value="${error}"/></div>
        </div>
      </c:if>

      <c:if test="${not empty message}">
        <div class="auth-alert success" role="alert">
          <span class="alert-icon">✓</span>
          <div class="alert-text"><c:out value="${message}"/></div>
        </div>
      </c:if>

      <!-- Client-Side Validation Alert Box -->
      <div class="auth-alert error" id="clientErrorBox" style="display:none;" role="alert">
        <span class="alert-icon">⚠️</span>
        <div class="alert-text" id="clientErrorMsg"></div>
      </div>

      <!-- Tab Switcher (Navigation for all three forms) -->
      <nav class="auth-tabs" role="tablist" aria-label="Authentication Options">
        <button
          type="button"
          id="tabBtnLogin"
          class="auth-tab-btn ${activeMode == 'login' ? 'active' : ''}"
          onclick="switchAuthTab('login')"
          role="tab"
          aria-selected="${activeMode == 'login'}"
          aria-controls="loginForm"
        >
          Sign In
        </button>
        <button
          type="button"
          id="tabBtnRegister"
          class="auth-tab-btn ${activeMode == 'register' ? 'active' : ''}"
          onclick="switchAuthTab('register')"
          role="tab"
          aria-selected="${activeMode == 'register'}"
          aria-controls="registerForm"
        >
          Create Account
        </button>
        <button
          type="button"
          id="tabBtnForgot"
          class="auth-tab-btn ${activeMode == 'forgot' ? 'active' : ''}"
          onclick="switchAuthTab('forgot')"
          role="tab"
          aria-selected="${activeMode == 'forgot'}"
          aria-controls="forgotForm"
        >
          Forgot Password
        </button>
      </nav>

      <!-- ===================================================================
           FORM 1: SIGN IN (Default View)
           =================================================================== -->
      <form
        id="loginForm"
        class="auth-form ${activeMode == 'login' ? 'active' : ''}"
        ${activeMode == 'login' ? '' : 'hidden style="display:none !important;" aria-hidden="true"'}
        action="${pageContext.request.contextPath}/login"
        method="post"
        novalidate
      >
        <div class="form-group">
          <label class="form-label" for="loginEmail">Email Address</label>
          <div class="form-input-wrapper">
            <span class="input-icon">✉</span>
            <input
              type="email"
              id="loginEmail"
              name="email"
              class="form-input"
              value="<c:out value='${email}'/>"
              placeholder="you@example.com"
              required
              autocomplete="email"
            >
          </div>
        </div>

        <div class="form-group">
          <div style="display:flex; justify-content:space-between; align-items:center;">
            <label class="form-label" for="loginPassword" style="margin-bottom:0;">Password</label>
            <a
              href="${pageContext.request.contextPath}/login?tab=forgot"
              onclick="switchAuthTab('forgot'); return false;"
              class="form-inline-link"
              title="Reset your password"
            >
              Forgot password?
            </a>
          </div>
          <div class="form-input-wrapper" style="margin-top:6px;">
            <span class="input-icon">🔒</span>
            <input
              type="password"
              id="loginPassword"
              name="password"
              class="form-input"
              placeholder="••••••••"
              required
              autocomplete="current-password"
            >
            <button
              type="button"
              class="toggle-password-btn"
              onclick="togglePasswordVisibility('loginPassword', this)"
              title="Toggle password visibility"
              aria-label="Show password"
            >
              👁
            </button>
          </div>
        </div>

        <div class="form-row-remember">
          <label class="checkbox-container">
            <input type="checkbox" name="rememberMe" id="loginRememberMe" ${rememberMe ? 'checked' : ''}>
            <span class="custom-checkmark"></span>
            <span class="checkbox-label">Remember me for 30 days</span>
          </label>
        </div>

        <button type="submit" class="auth-submit-btn">
          Sign In to RecipeHub <span>→</span>
        </button>

        <div class="auth-footer-prompt">
          Don't have a RecipeHub account yet?
          <br>
          <a
            href="${pageContext.request.contextPath}/login?tab=register"
            onclick="switchAuthTab('register'); return false;"
          >
            Create an account
          </a>
        </div>
      </form>

      <!-- ===================================================================
           FORM 2: CREATE ACCOUNT / SIGN UP
           =================================================================== -->
      <form
        id="registerForm"
        class="auth-form ${activeMode == 'register' ? 'active' : ''}"
        ${activeMode == 'register' ? '' : 'hidden style="display:none !important;" aria-hidden="true"'}
        action="${pageContext.request.contextPath}/register"
        method="post"
        novalidate
      >
        <div class="form-group">
          <label class="form-label" for="regName">Full Name</label>
          <div class="form-input-wrapper">
            <span class="input-icon">👤</span>
            <input
              type="text"
              id="regName"
              name="name"
              class="form-input"
              value="<c:out value='${name}'/>"
              placeholder="e.g. Jordan Miller"
              required
              autocomplete="name"
            >
          </div>
        </div>

        <div class="form-group">
          <label class="form-label" for="regEmail">Email Address</label>
          <div class="form-input-wrapper">
            <span class="input-icon">✉</span>
            <input
              type="email"
              id="regEmail"
              name="email"
              class="form-input"
              value="<c:out value='${email}'/>"
              placeholder="you@example.com"
              required
              autocomplete="email"
            >
          </div>
        </div>

        <div class="form-group">
          <label class="form-label" for="regPassword">Password</label>
          <div class="form-input-wrapper">
            <span class="input-icon">🔒</span>
            <input
              type="password"
              id="regPassword"
              name="password"
              class="form-input"
              placeholder="At least 6 characters"
              required
              autocomplete="new-password"
            >
            <button
              type="button"
              class="toggle-password-btn"
              onclick="togglePasswordVisibility('regPassword', this)"
              title="Toggle password visibility"
              aria-label="Show password"
            >
              👁
            </button>
          </div>
          <small class="form-hint">Must be at least 6 characters long.</small>
        </div>

        <div class="form-group">
          <label class="form-label" for="regConfirmPassword">Confirm Password</label>
          <div class="form-input-wrapper">
            <span class="input-icon">🔒</span>
            <input
              type="password"
              id="regConfirmPassword"
              name="confirmPassword"
              class="form-input"
              placeholder="Re-enter your password"
              required
              autocomplete="new-password"
            >
            <button
              type="button"
              class="toggle-password-btn"
              onclick="togglePasswordVisibility('regConfirmPassword', this)"
              title="Toggle password visibility"
              aria-label="Show password"
            >
              👁
            </button>
          </div>
        </div>

        <button type="submit" class="auth-submit-btn">
          Create Account <span>→</span>
        </button>

        <div class="auth-footer-prompt">
          Already have an account?
          <a
            href="${pageContext.request.contextPath}/login?tab=login"
            onclick="switchAuthTab('login'); return false;"
          >
            Sign in here
          </a>
          <div class="auth-sub-links" style="margin-top:10px;">
            <span>Lost your credentials?</span>
            <a
              href="${pageContext.request.contextPath}/login?tab=forgot"
              onclick="switchAuthTab('forgot'); return false;"
            >
              Forgot password?
            </a>
          </div>
        </div>
      </form>

      <!-- ===================================================================
           FORM 3: SECURE OTP-BASED FORGOT PASSWORD
           =================================================================== -->
      <div
        id="forgotContainer"
        class="auth-form ${activeMode == 'forgot' ? 'active' : ''}"
        ${activeMode == 'forgot' ? '' : 'hidden style="display:none !important;" aria-hidden="true"'}
      >
        <c:choose>

          <%-- STEP 3: Enter new password with verified resetToken --%>
          <c:when test="${step == 'setNewPassword'}">
            <form
              id="forgotFormStep3"
              action="${pageContext.request.contextPath}/forgot-password"
              method="post"
              style="display:flex; flex-direction:column; gap:18px;"
              novalidate
            >
              <input type="hidden" name="action" value="updatePassword">
              <input type="hidden" name="email" value="<c:out value='${email}'/>">
              <input type="hidden" name="resetToken" value="<c:out value='${resetToken}'/>">

              <div class="form-group">
                <label class="form-label">Verified Email</label>
                <div class="form-input-static">
                  ✉ <c:out value="${email}"/>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label" for="newPassword">New Password</label>
                <div class="form-input-wrapper">
                  <span class="input-icon">🔒</span>
                  <input
                    type="password"
                    id="newPassword"
                    name="newPassword"
                    class="form-input"
                    placeholder="Minimum 6 characters"
                    required
                    autocomplete="new-password"
                  >
                  <button
                    type="button"
                    class="toggle-password-btn"
                    onclick="togglePasswordVisibility('newPassword', this)"
                    title="Toggle password visibility"
                    aria-label="Show password"
                  >
                    👁
                  </button>
                </div>
                <small class="form-hint">At least 6 characters in length.</small>
              </div>

              <div class="form-group">
                <label class="form-label" for="confirmNewPassword">Confirm New Password</label>
                <div class="form-input-wrapper">
                  <span class="input-icon">🔒</span>
                  <input
                    type="password"
                    id="confirmNewPassword"
                    name="confirmPassword"
                    class="form-input"
                    placeholder="Re-enter your new password"
                    required
                    autocomplete="new-password"
                  >
                  <button
                    type="button"
                    class="toggle-password-btn"
                    onclick="togglePasswordVisibility('confirmNewPassword', this)"
                    title="Toggle password visibility"
                    aria-label="Show password"
                  >
                    👁
                  </button>
                </div>
              </div>

              <button type="submit" class="auth-submit-btn">
                Update Password <span>✓</span>
              </button>
            </form>
          </c:when>

          <%-- STEP 2: Enter 6-digit OTP code --%>
          <c:when test="${step == 'verifyOtp'}">
            <form
              id="forgotFormStep2"
              action="${pageContext.request.contextPath}/forgot-password"
              method="post"
              style="display:flex; flex-direction:column; gap:18px;"
              novalidate
            >
              <input type="hidden" name="action" value="verifyOtp">
              <input type="hidden" name="email" value="<c:out value='${email}'/>">

              <div class="form-group">
                <label class="form-label">Destination Email</label>
                <div class="form-input-static">
                  ✉ <c:out value="${email}"/>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label" for="otpCode">6-Digit Verification Code</label>
                <div class="form-input-wrapper">
                  <span class="input-icon">🔑</span>
                  <input
                    type="text"
                    id="otpCode"
                    name="otpCode"
                    class="form-input"
                    style="letter-spacing:6px; font-weight:700; font-size:18px; text-align:center;"
                    placeholder="••••••"
                    maxlength="6"
                    pattern="[0-9]{6}"
                    required
                    autocomplete="one-time-code"
                    autofocus
                  >
                </div>
                <small class="form-hint">Code expires in <strong>5 minutes</strong>. Enter the 6 digits sent to your inbox.</small>
              </div>

              <button type="submit" class="auth-submit-btn">
                Verify Code <span>✓</span>
              </button>
            </form>

            <!-- Resend OTP Option with 60s cooldown -->
            <form
              id="resendOtpForm"
              action="${pageContext.request.contextPath}/forgot-password"
              method="post"
              style="text-align:center; margin-top:8px;"
            >
              <input type="hidden" name="action" value="resendOtp">
              <input type="hidden" name="email" value="<c:out value='${email}'/>">
              <button
                type="submit"
                id="resendOtpBtn"
                class="btn-resend-otp"
                <c:if test="${not empty cooldownSeconds and cooldownSeconds > 0}">disabled</c:if>
              >
                ↻ Resend Code <span id="cooldownTimer"><c:if test="${not empty cooldownSeconds and cooldownSeconds > 0}">(${cooldownSeconds}s)</c:if></span>
              </button>
            </form>
          </c:when>

          <%-- STEP 1: Enter email to request OTP (Default) --%>
          <c:otherwise>
            <form
              id="forgotFormStep1"
              action="${pageContext.request.contextPath}/forgot-password"
              method="post"
              style="display:flex; flex-direction:column; gap:18px;"
              novalidate
            >
              <input type="hidden" name="action" value="requestOtp">

              <div class="form-group">
                <label class="form-label" for="forgotEmail">Your Account Email</label>
                <div class="form-input-wrapper">
                  <span class="input-icon">✉</span>
                  <input
                    type="email"
                    id="forgotEmail"
                    name="email"
                    class="form-input"
                    value="<c:out value='${email}'/>"
                    placeholder="you@example.com"
                    required
                    autocomplete="email"
                  >
                </div>
                <small class="form-hint">A single-use 6-digit code will be generated and sent to your email.</small>
              </div>

              <button type="submit" class="auth-submit-btn">
                Send Verification Code <span>→</span>
              </button>
            </form>
          </c:otherwise>

        </c:choose>

        <div class="auth-footer-prompt">
          Remember your password?
          <a
            href="${pageContext.request.contextPath}/login?tab=login"
            onclick="switchAuthTab('login'); return false;"
          >
            Return to Sign In
          </a>
          <div class="auth-sub-links" style="margin-top:10px;">
            <span>Need a new account?</span>
            <a
              href="${pageContext.request.contextPath}/login?tab=register"
              onclick="switchAuthTab('register'); return false;"
            >
              Create Account
            </a>
          </div>
        </div>
      </div>

    </div>
  </main>

  <!-- Footer -->
  <footer>
    <div class="footer-bottom" style="border-top:none; padding-top:0;">
      <span>© RecipeHub. Handcrafted with care for home cooks everywhere.</span>
      <span>Safe BCrypt Password Hashing · Jakarta EE 10</span>
    </div>
  </footer>

  <!-- Auth Interactions & Strict Single-Form Switcher Script -->
  <script>
    /**
     * Switches the active authentication form and strictly hides the others.
     * Updates heading text, ARIA attributes, tab buttons, and browser URL.
     */
    function switchAuthTab(mode, updateHistory) {
      if (typeof updateHistory === 'undefined') {
        updateHistory = true;
      }

      const forms = {
        login: document.getElementById('loginForm'),
        register: document.getElementById('registerForm'),
        forgot: document.getElementById('forgotContainer')
      };

      const tabBtns = {
        login: document.getElementById('tabBtnLogin'),
        register: document.getElementById('tabBtnRegister'),
        forgot: document.getElementById('tabBtnForgot')
      };

      const heading = document.getElementById('authHeading');
      const subtext = document.getElementById('authSubtext');
      const errorBox = document.getElementById('clientErrorBox');
      if (errorBox) {
        errorBox.style.display = 'none';
      }

      // 1. Strictly hide ALL forms and remove them from keyboard navigation
      Object.keys(forms).forEach(function(key) {
        const f = forms[key];
        if (f) {
          f.classList.remove('active');
          f.setAttribute('hidden', '');
          f.setAttribute('aria-hidden', 'true');
          f.style.setProperty('display', 'none', 'important');
          f.querySelectorAll('input, button[type="submit"]').forEach(function(el) {
            el.setAttribute('tabindex', '-1');
          });
        }
      });

      // 2. Deactivate ALL tab buttons
      Object.keys(tabBtns).forEach(function(key) {
        const b = tabBtns[key];
        if (b) {
          b.classList.remove('active');
          b.setAttribute('aria-selected', 'false');
        }
      });

      // 3. Reveal and activate ONLY the chosen form
      const chosenMode = (mode === 'register' || mode === 'forgot') ? mode : 'login';
      const targetForm = forms[chosenMode];
      const targetBtn = tabBtns[chosenMode];

      if (targetForm) {
        targetForm.removeAttribute('hidden');
        targetForm.setAttribute('aria-hidden', 'false');
        targetForm.classList.add('active');
        targetForm.style.removeProperty('display');
        targetForm.style.setProperty('display', 'flex', 'important');
        targetForm.querySelectorAll('input, button[type="submit"]').forEach(function(el) {
          el.removeAttribute('tabindex');
        });
      }

      if (targetBtn) {
        targetBtn.classList.add('active');
        targetBtn.setAttribute('aria-selected', 'true');
      }

      // 4. Update Header Copy
      if (heading && subtext) {
        if (chosenMode === 'register') {
          heading.textContent = 'Create your account';
          subtext.textContent = 'Join home cooks discovering and sharing culinary joy.';
        } else if (chosenMode === 'forgot') {
          const step = '${step}';
          if (step === 'verifyOtp') {
            heading.textContent = 'Verify Verification Code';
            subtext.textContent = 'Enter the 6-digit code sent to your email to confirm your identity.';
          } else if (step === 'setNewPassword') {
            heading.textContent = 'Set New Password';
            subtext.textContent = 'Choose a new, secure password for your RecipeHub account.';
          } else {
            heading.textContent = 'Reset your password';
            subtext.textContent = 'Enter your email to receive a secure 6-digit OTP code.';
          }
        } else {
          heading.textContent = 'Welcome back to RecipeHub';
          subtext.textContent = 'Sign in to access your saved recipes and weekly kitchen edit.';
        }
      }

      // 5. Synchronize Browser URL Query Parameter
      if (updateHistory && window.history && window.history.replaceState) {
        try {
          const url = new URL(window.location.href);
          url.searchParams.set('tab', chosenMode);
          window.history.replaceState({ tab: chosenMode }, '', url.toString());
        } catch (ignored) {}
      }
    }

    /**
     * Show / Hide password toggle
     */
    function togglePasswordVisibility(inputId, btn) {
      const input = document.getElementById(inputId);
      if (!input) return;
      if (input.type === 'password') {
        input.type = 'text';
        btn.textContent = '🙈';
        btn.setAttribute('aria-label', 'Hide password');
      } else {
        input.type = 'password';
        btn.textContent = '👁';
        btn.setAttribute('aria-label', 'Show password');
      }
    }

    /**
     * Displays a clean client-side validation error banner inside the card
     */
    function showClientError(msg) {
      const box = document.getElementById('clientErrorBox');
      const txt = document.getElementById('clientErrorMsg');
      if (box && txt) {
        txt.textContent = msg;
        box.style.display = 'flex';
        box.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
      }
    }

    // Client-Side Validation for Sign In
    document.getElementById('loginForm')?.addEventListener('submit', function (e) {
      const email = document.getElementById('loginEmail')?.value.trim();
      const password = document.getElementById('loginPassword')?.value;
      if (!email || !password) {
        e.preventDefault();
        showClientError('Please enter both your email address and password.');
      }
    });

    // Client-Side Validation for Registration
    document.getElementById('registerForm')?.addEventListener('submit', function (e) {
      const name = document.getElementById('regName')?.value.trim();
      const email = document.getElementById('regEmail')?.value.trim();
      const password = document.getElementById('regPassword')?.value;
      const confirm = document.getElementById('regConfirmPassword')?.value;

      if (!name || name.length < 2) {
        e.preventDefault();
        showClientError('Please provide your full name (minimum 2 characters).');
        return;
      }
      if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        e.preventDefault();
        showClientError('Please provide a valid email address.');
        return;
      }
      if (!password || password.length < 6) {
        e.preventDefault();
        showClientError('Password must be at least 6 characters in length.');
        return;
      }
      if (password !== confirm) {
        e.preventDefault();
        showClientError('Passwords do not match. Please re-enter.');
        return;
      }
    });

    // Client-Side Validation for OTP Step 1
    document.getElementById('forgotFormStep1')?.addEventListener('submit', function (e) {
      const email = document.getElementById('forgotEmail')?.value.trim();
      if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        e.preventDefault();
        showClientError('Please provide a valid email address.');
      }
    });

    // Client-Side Validation for OTP Step 2
    document.getElementById('forgotFormStep2')?.addEventListener('submit', function (e) {
      const otp = document.getElementById('otpCode')?.value.trim();
      if (!otp || !/^[0-9]{6}$/.test(otp)) {
        e.preventDefault();
        showClientError('Please enter the exact 6-digit numeric verification code.');
      }
    });

    // Client-Side Validation for OTP Step 3
    document.getElementById('forgotFormStep3')?.addEventListener('submit', function (e) {
      const p1 = document.getElementById('newPassword')?.value;
      const p2 = document.getElementById('confirmNewPassword')?.value;
      if (!p1 || p1.length < 6) {
        e.preventDefault();
        showClientError('New password must be at least 6 characters in length.');
        return;
      }
      if (p1 !== p2) {
        e.preventDefault();
        showClientError('New passwords do not match. Please re-enter.');
        return;
      }
    });

    // Resend OTP Cooldown Timer
    (function initCooldownTimer() {
      let seconds = parseInt('${cooldownSeconds}', 10);
      if (isNaN(seconds) || seconds <= 0) return;

      const timerSpan = document.getElementById('cooldownTimer');
      const btn = document.getElementById('resendOtpBtn');
      if (!timerSpan || !btn) return;

      btn.disabled = true;
      const interval = setInterval(function () {
        seconds--;
        if (seconds <= 0) {
          clearInterval(interval);
          timerSpan.textContent = '';
          btn.disabled = false;
        } else {
          timerSpan.textContent = '(' + seconds + 's)';
        }
      }, 1000);
    })();

    // Handle browser back/forward buttons
    window.addEventListener('popstate', function (e) {
      if (e.state && e.state.tab) {
        switchAuthTab(e.state.tab, false);
      } else {
        const params = new URLSearchParams(window.location.search);
        const tab = params.get('tab') || 'login';
        switchAuthTab(tab, false);
      }
    });

    // Initial check on DOM ready to ensure correct single form is shown
    document.addEventListener('DOMContentLoaded', function () {
      const params = new URLSearchParams(window.location.search);
      const urlTab = params.get('tab');
      const hash = window.location.hash ? window.location.hash.substring(1) : null;
      const initialMode = urlTab || hash || '${activeMode}' || 'login';
      switchAuthTab(initialMode, false);
    });
  </script>
</body>
</html>
