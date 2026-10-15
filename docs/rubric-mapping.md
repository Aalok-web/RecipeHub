# Assignment Rubric Mapping — RecipeHub

Team: BYTE_CODERS  
Leader: Aalok Kumar  
Members: Shalini Tomar, Jagriti Niranjan

This document maps assignment topics to the current source tree. It describes implemented code, not just planned slides.

## Java / OOP concepts

| Topic | Evidence in project | Status / explanation |
|---|---|---|
| Encapsulation and domain models | `src/main/java/com/recipehub/model/Recipe.java`, `User.java`, `PasswordResetOtp.java` | Model state is accessed through methods. |
| Inheritance | Servlet classes extend `jakarta.servlet.http.HttpServlet`; `DataAccessException` extends `RuntimeException` | Implemented. |
| Interfaces and polymorphism | `dao/RecipeDAO.java`, `dao/UserDAO.java`; `dao/impl/JdbcRecipeDAO.java`, `JdbcUserDAO.java`; `service/EmailService.java` and `SmtpEmailService.java` | Contracts are separated from concrete implementations. |
| Exception handling | DAO methods declare SQL exceptions; servlet controllers handle request/database failures; custom `DataAccessException` and `ValidationException` types | Implemented; error paths should be included in testing evidence. |
| Collections and Generics | `List<Recipe>`, `ArrayList<Recipe>`, `Set<Long>`, typed DAO results | Implemented. |
| Multithreading and synchronization | `service/RecipeViewTracker.java` uses `ConcurrentHashMap<Long, LongAdder>`; `RecipeViewTrackerTest.java` submits concurrent updates with an `ExecutorService` | Implemented with a concurrency test. No manual `synchronized` block is needed because the concurrent utilities provide thread-safe updates. |

## Java Web and database topics

| Topic | Evidence in project | Status / explanation |
|---|---|---|
| Problem understanding / solution design | Recipe discovery, categories, detailed instructions, accounts, favorites, profile and password reset | Implemented in the web application. |
| JDBC connectivity | `util/Database.java` | Connection factory supports configured JDBC/JNDI connection paths. |
| Database operations | `dao/impl/JdbcRecipeDAO.java`, `JdbcUserDAO.java` | Parameterized SQL, CRUD, authentication/profile/favorites and reset operations. |
| Servlets and web integration | `servlet/` controllers; JSPs in `src/main/webapp/WEB-INF/views/` | Servlet request handling forwards to JSP views. |
| Automated tests | `src/test/java/` | JUnit tests cover models, OTP behavior, database connection behavior, and concurrent view counting. |
| Build verification | `.github/workflows/maven.yml` | GitHub Actions runs `mvn clean verify` on pushes and pull requests. Confirm the Actions run is green before presenting a build-success claim. |

## Important limitations to disclose

- Recipe view counts are held in application memory. They reset when the web app restarts and are not stored in MySQL.
- A passing automated build is not the same as an end-to-end test against a live MySQL/Tomcat deployment.
- Add genuine screenshots from the running website and a successful local or GitHub Actions build to the final presentation. Do not label mockups or unrun tests as real evidence.
