# RecipeHub – Online Recipe Sharing and Management System

## Project Overview

RecipeHub is a Java web application that allows users to explore, share, add, edit, and manage recipes through a simple interface.

## Features

* User registration and login
* Browse and view recipes
* Filter recipes by category
* Add, edit, and delete recipes
* Save favorite recipes
* View personal recipes and profile
* Change password

## Technologies Used

* Java
* Java Servlets and JSP
* JDBC
* MySQL
* HTML and CSS
* Apache Tomcat 10.1
* Maven
* IntelliJ IDEA

## Requirements

* JDK compatible with the project configuration
* Apache Maven
* MySQL Server
* Apache Tomcat 10.1
* IntelliJ IDEA or another Java IDE

## Setup and Run

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA as a Maven project.
3. Create a MySQL database named `recipe_db`.
4. Configure database credentials in the project's database connection class.
5. Create the required tables and configure the database schema.
6. Reload Maven dependencies and build the project.
7. Configure Tomcat 10.1 and deploy the web application.
8. Start Tomcat and open the application in your browser.

## Project Structure

* `src/main/java/com/recipehub/dao` – Database access classes and interfaces
* `src/main/java/com/recipehub/model` – Model classes
* `src/main/java/com/recipehub/servlet` – Servlet controllers
* `src/main/java/com/recipehub/util` – Database utilities
* `src/main/webapp` – Web pages and JSP files
* `pom.xml` – Maven project configuration

## Database

The application uses MySQL and JDBC for database connectivity and recipe-related operations.

**Note:** Configure your local database credentials before running the project. Never upload real passwords or other secrets to GitHub.

## Author

Aalok Kumar
