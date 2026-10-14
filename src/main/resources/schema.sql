-- ========================================================
-- RecipeHub (Online Recipe Sharing System) Database Schema
-- Database: recipe_db
-- ========================================================

CREATE DATABASE IF NOT EXISTS recipe_db;
USE recipe_db;

-- --------------------------------------------------------
-- 1. Table structure for table `users`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- 2. Table structure for table `recipes`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS recipes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    ingredients TEXT,
    instructions TEXT,
    category VARCHAR(100),
    image VARCHAR(255),
    cooking_time INT,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- 3. Table structure for table `favorites`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS favorites (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_fav (user_id, recipe_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

-- ========================================================
-- Seed Data for Testing and Demonstration
-- ========================================================

-- Insert Sample Users (Pass: password123)
INSERT INTO users (id, name, email, password, role) VALUES
(1, 'Aalok Kumar', 'aalok@example.com', 'Aalok@12345', 'ADMIN'),
(2, 'Chef Gordon', 'gordon@example.com', 'chef123', 'USER')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Insert Sample Recipes
INSERT INTO recipes (id, title, description, ingredients, instructions, category, image, cooking_time, user_id) VALUES
(1, 'Fluffy Buttermilk Pancakes', 'Golden brown, light and fluffy breakfast pancakes served with maple syrup.', '2 cups all-purpose flour\n2 tsp baking powder\n1/2 tsp baking soda\n2 tbsp sugar\n1/2 tsp salt\n2 cups buttermilk\n2 large eggs\n1/4 cup melted butter', '1. In a bowl, whisk dry ingredients together.\n2. In a separate bowl, whisk buttermilk, eggs, and melted butter.\n3. Combine wet and dry ingredients gently; do not overmix.\n4. Pour batter onto a hot greased griddle.\n5. Cook until bubbles form, flip, and cook until golden brown.', 'Breakfast', 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?auto=format&fit=crop&w=800&q=80', 20, 1),
(2, 'Creamy Garlic Parmesan Pasta', 'Rich, comforting fettuccine tossed in a silky garlic parmesan cream sauce.', '400g fettuccine pasta\n3 tbsp butter\n4 cloves garlic (minced)\n1 cup heavy cream\n1 cup grated Parmesan cheese\nSalt and black pepper to taste\nFresh parsley', '1. Boil pasta in salted water until al dente.\n2. In a skillet, melt butter over medium heat and sauté minced garlic.\n3. Pour in heavy cream and simmer gently for 3 minutes.\n4. Stir in freshly grated parmesan cheese until smooth.\n5. Toss pasta with sauce and garnish with parsley.', 'Lunch', 'https://images.unsplash.com/photo-1621996346565-e3d5d6281781?auto=format&fit=crop&w=800&q=80', 25, 1),
(3, 'Paneer Butter Masala', 'A classic North Indian curry made with soft paneer cubes in a spiced tomato cream gravy.', '250g paneer (cubed)\n3 large tomatoes (pureed)\n1 large onion (chopped)\n2 tbsp ginger-garlic paste\n2 tbsp butter + 1 tbsp oil\n1/2 cup fresh cream\n1 tsp kasuri methi\n1 tsp garam masala\n1 tsp red chili powder', '1. Heat oil and butter in a pan; sauté onions until golden.\n2. Add ginger-garlic paste and tomato puree; cook until oil separates.\n3. Add spices and simmer with 1/2 cup water.\n4. Add paneer cubes and gently fold.\n5. Finish with fresh cream and crushed kasuri methi.', 'Dinner', 'https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=800&q=80', 35, 2),
(4, 'Warm Molten Chocolate Lava Cake', 'Decadent individual chocolate cakes with an irresistibly gooey melted chocolate center.', '100g dark chocolate (chopped)\n100g unsalted butter\n2 large eggs + 2 yolks\n1/2 cup powdered sugar\n3 tbsp all-purpose flour\nPinch of salt', '1. Melt dark chocolate and butter together in a heatproof bowl.\n2. Whisk eggs, yolks, and powdered sugar until light and pale.\n3. Fold chocolate mixture and flour into eggs.\n4. Pour into buttered ramekins dusted with cocoa powder.\n5. Bake at 200°C (400°F) for 12 minutes. Serve immediately with vanilla ice cream.', 'Dessert', 'https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=800&q=80', 25, 2)
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- Sample Favorite
INSERT IGNORE INTO favorites (user_id, recipe_id) VALUES (1, 3), (2, 1);
