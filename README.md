# RecipeHub 🍳

A modern, full-stack Java web application for discovering recipes, organizing meal plans, and managing chef profiles. Built with an editorial food-magazine aesthetic inspired by modern culinary platforms.

---

## ✨ Features

- **Editorial Recipe Discovery**:
  - Warm ivory and forest green design with refined typography (`Playfair Display` and `DM Sans`).
  - Hero search bar with instant keyword filtering (by ingredients or dish name).
  - Category filtering for Breakfast, Lunch, Dinner, Vegetarian, and Desserts.
  - Interactive recipe cards with preparation times, cook times, servings, and category chips.
- **Detailed Recipe Pages (`/recipe?id=ID`)**:
  - High-resolution food photography.
  - Interactive ingredients checklist with strike-through completion.
  - Step-by-step culinary preparation instructions.
  - Print recipe and quick clipboard link-sharing utilities.
- **User Authentication & Session Management**:
  - Member Sign In and Create Account flows with BCrypt password hashing.
  - Remember Me functionality via secure HTTP cookies.
  - Server-side session management with session fixation protection (`request.changeSessionId()`).
  - Protected routes redirecting unauthenticated visitors to `/login`.
- **User Profile & Saved Recipes (`/profile`)**:
  - Live chef dashboard displaying registered name, email, avatar initial badge, and kitchen status.
  - Personal culinary bio with inline edit profile capabilities.
  - Real-time Saved Recipes section backed by MySQL `favorites` persistence.
- **Secure OTP-Based Password Reset**:
  - 3-step verification flow: Email Request → 6-Digit OTP Verification → New Password Setting.
  - Cryptographically secure 6-digit OTP generation with SHA-256 hash storage.
  - 5-minute time-to-live (TTL), rate-limiting (maximum 5 attempts), and 60-second resend cooldown.
  - Single-use reset tokens with automatic invalidation upon password update.
  - Anti-enumeration user feedback protecting email privacy.
  - Integrated Jakarta Mail SMTP service for transactional email delivery.

---

## 🛠️ Technology Stack

- **Backend**: Java 17, Jakarta Servlets 6.0, Jakarta Server Pages (JSP) & JSTL 3.0
- **Email Service**: Jakarta Mail 2.1 (`jakarta.mail-api` + `angus-mail`)
- **Security & Hashing**: jBCrypt (BCrypt 12 rounds), SHA-256 OTP hashing
- **Persistence**: MySQL 8+ via JDBC with 100% PreparedStatement parameterized queries
- **Build & Packaging**: Apache Maven 3.8+ (WAR packaging)
- **Servlet Container**: Apache Tomcat 10.1+ / 11.0+

---

## 📋 Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: Version 3.8+
- **MySQL Server**: Version 8.0+
- **Apache Tomcat**: Version 10.1+ or 11.0+

---

## 🚀 Setup & Installation

### 1. Database Setup

Log into MySQL and execute the schema and seed scripts:

```bash
mysql -u <your_mysql_user> -p < database/schema.sql
mysql -u <your_mysql_user> -p < database/seed.sql
```

### 2. Environment Configuration

Configure the following environment variables before launching your application:

#### Database Settings (Required):
```bash
export RECIPEHUB_DB_URL="jdbc:mysql://localhost:3306/recipehub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"
export RECIPEHUB_DB_USER="your_db_username"
export RECIPEHUB_DB_PASSWORD="your_db_password"
```

#### SMTP Settings (Optional, for live password reset emails):
```bash
export SMTP_HOST="smtp.gmail.com"
export SMTP_PORT="587"
export SMTP_USERNAME="your-email@gmail.com"
export SMTP_PASSWORD="your-app-password"   # Use a 16-character App Password
export SMTP_FROM="RecipeHub Kitchen <no-reply@recipehub.com>"
export SMTP_STARTTLS="true"
export SMTP_AUTH="true"
```

### 3. Build the Application

From the project root directory, run:

```bash
mvn clean package
```

This compiles all classes, runs the automated test suite, and builds `target/recipehub.war`.

### 4. Deploy to Tomcat

Copy the generated WAR file into Tomcat's `webapps` directory:

```bash
cp target/recipehub.war $CATALINA_HOME/webapps/recipehub.war
```

Open your browser and navigate to:
```
http://localhost:8080/recipehub/
```

---

## 📂 Project Structure

```
RecipeHub/
├── database/
│   ├── schema.sql           # Database schema & tables (users, recipes, favorites, password_resets)
│   └── seed.sql             # Curated seed recipes and categories
├── src/
│   ├── main/
│   │   ├── java/com/recipehub/
│   │   │   ├── dao/         # Data Access Object interfaces & JDBC implementations
│   │   │   ├── model/       # Domain models (User, Recipe, PasswordResetOtp)
│   │   │   ├── service/     # Email service interfaces & SMTP implementation
│   │   │   ├── servlet/     # Controllers (Home, RecipeDetail, Login, Register, Profile, Favorite, etc.)
│   │   │   └── util/        # JDBC Database Connection Factory
│   │   └── webapp/
│   │       ├── assets/      # CSS styles and JavaScript enhancements
│   │       └── WEB-INF/views/ # JSP templates (home, recipe-detail, auth, profile)
│   └── test/java/com/recipehub/ # Comprehensive JUnit test suites
├── pom.xml                  # Maven dependencies and build configuration
└── README.md                # Project documentation
```

---

## 🔒 Security Best Practices

- **Zero Hardcoded Secrets**: All database and SMTP credentials are read dynamically via environment variables or JVM system properties.
- **SQL Injection Prevention**: All SQL executions strictly use parameterized `PreparedStatement`.
- **Password Security**: Passwords are never stored in plaintext; salted and hashed with BCrypt.
- **OTP Protection**: Reset codes are stored as SHA-256 hashes and permanently deleted after single use.
- **Session Protection**: Protected pages enforce active authentication; logout invalidates sessions on the server.

---

## 📄 License

This project is licensed under the MIT License.
