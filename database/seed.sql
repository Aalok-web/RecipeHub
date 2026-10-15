USE recipehub;
INSERT IGNORE INTO categories(name, slug) VALUES
('Breakfast','breakfast'),('Lunch','lunch'),('Dinner','dinner'),('Desserts','desserts'),('Vegetarian','vegetarian');

-- 1. Creamy Tuscan Pasta (Dinner)
INSERT INTO recipes(category_id,title,description,ingredients,instructions,image_url,prep_minutes,cook_minutes,servings)
SELECT c.id,'Creamy Tuscan Pasta','Silky garlic cream sauce, sun-dried tomatoes and fresh basil.',
'Pasta; garlic; cream; sun-dried tomatoes; spinach; parmesan; basil',
'Boil pasta in salted water until al dente. Sauté minced garlic and sun-dried tomatoes in olive oil. Stir in heavy cream and fresh spinach until wilted. Toss with warm pasta, then finish with grated parmesan and fresh basil.',
'https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=1200&q=85',10,20,2
FROM categories c WHERE c.slug='dinner'
AND NOT EXISTS (SELECT 1 FROM recipes WHERE title='Creamy Tuscan Pasta');

-- 2. Avocado Garden Toast (Breakfast)
INSERT INTO recipes(category_id,title,description,ingredients,instructions,image_url,prep_minutes,cook_minutes,servings)
SELECT c.id,'Avocado Garden Toast','A bright, fresh toast with smashed avocado and herbs.',
'Sourdough bread; ripe avocados; fresh lemon juice; red chilli flakes; mixed fresh herbs; extra virgin olive oil; sea salt',
'Toast sourdough slices until golden and crisp. Mash ripe avocado with fresh lemon juice, sea salt, and black pepper. Generously spread over warm toast. Garnish with chopped fresh herbs, red chilli flakes, and a drizzle of olive oil.',
'https://images.unsplash.com/photo-1525351484163-7529414344d8?auto=format&fit=crop&w=1200&q=85',8,5,2
FROM categories c WHERE c.slug='breakfast'
AND NOT EXISTS (SELECT 1 FROM recipes WHERE title='Avocado Garden Toast');

-- 3. Berry Breakfast Bowl (Breakfast)
INSERT INTO recipes(category_id,title,description,ingredients,instructions,image_url,prep_minutes,cook_minutes,servings)
SELECT c.id,'Berry Breakfast Bowl','Creamy yogurt, seasonal berries and crunchy granola.',
'Greek yogurt; fresh strawberries; fresh blueberries; toasted granola; raw wildflower honey; chia seeds; mint sprigs',
'Spoon chilled Greek yogurt into serving bowls as the creamy base. Scatter freshly sliced strawberries and blueberries over the surface. Sprinkle with toasted crunchy granola and chia seeds. Drizzle golden honey across the top and garnish with fresh mint.',
'https://images.unsplash.com/photo-1511690743698-d9d85f2fbf38?auto=format&fit=crop&w=1200&q=85',5,0,1
FROM categories c WHERE c.slug='breakfast'
AND NOT EXISTS (SELECT 1 FROM recipes WHERE title='Berry Breakfast Bowl');

-- 4. Mediterranean Quinoa Salad (Lunch)
INSERT INTO recipes(category_id,title,description,ingredients,instructions,image_url,prep_minutes,cook_minutes,servings)
SELECT c.id,'Mediterranean Quinoa Salad','Nutrient-dense quinoa tossed with crisp vegetables, kalamata olives and feta.',
'Cooked quinoa; cherry tomatoes; cucumber; kalamata olives; crumbled feta; red onion; extra virgin olive oil; lemon juice; dried oregano',
'Cook quinoa and let it cool completely to room temperature. Halve cherry tomatoes, dice cucumber, and thinly slice red onion. Whisk extra virgin olive oil, fresh lemon juice, dried oregano, salt, and pepper in a small bowl. Toss quinoa, chopped vegetables, and kalamata olives together with the dressing. Fold in crumbled feta gently and serve chilled.',
'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=1200&q=85',15,15,3
FROM categories c WHERE c.slug='lunch'
AND NOT EXISTS (SELECT 1 FROM recipes WHERE title='Mediterranean Quinoa Salad');

-- 5. Classic Italian Tiramisu (Desserts)
INSERT INTO recipes(category_id,title,description,ingredients,instructions,image_url,prep_minutes,cook_minutes,servings)
SELECT c.id,'Classic Italian Tiramisu','Velvety mascarpone cream layered with espresso-dipped ladyfingers and dusted with cocoa.',
'Ladyfingers; mascarpone cheese; fresh brewed espresso; egg yolks; granulated sugar; unsweetened cocoa powder; pure vanilla extract',
'Brew strong espresso and allow it to cool completely. Whisk egg yolks with granulated sugar until pale and fluffy, then gently fold in mascarpone and vanilla extract. Briefly dip each ladyfinger into the cooled espresso and layer in the bottom of a serving dish. Spread half of the mascarpone cream over the ladyfingers, repeat with another layer, and dust with fine cocoa powder. Chill in the refrigerator for at least 4 hours before serving.',
'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?auto=format&fit=crop&w=1200&q=85',25,0,6
FROM categories c WHERE c.slug='desserts'
AND NOT EXISTS (SELECT 1 FROM recipes WHERE title='Classic Italian Tiramisu');

-- 6. Roasted Butternut Squash Buddha Bowl (Vegetarian)
INSERT INTO recipes(category_id,title,description,ingredients,instructions,image_url,prep_minutes,cook_minutes,servings)
SELECT c.id,'Roasted Butternut Squash Buddha Bowl','Caramelized squash, spiced chickpeas, baby spinach, avocado and creamy tahini.',
'Butternut squash cubes; spiced chickpeas; baby spinach; brown rice; ripe avocado; tahini; minced garlic; lemon juice; maple syrup; toasted sesame seeds',
'Preheat oven to 200°C (400°F). Toss butternut squash cubes with olive oil, smoked paprika, and sea salt, then roast for 25 minutes until caramelized. Warm chickpeas in a pan with ground cumin. Whisk tahini with minced garlic, lemon juice, pure maple syrup, and warm water until velvety smooth. Assemble bowls with cooked brown rice and baby spinach, then arrange roasted squash, chickpeas, and sliced avocado on top. Drizzle with tahini dressing and toasted sesame seeds.',
'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=1200&q=85',15,25,2
FROM categories c WHERE c.slug='vegetarian'
AND NOT EXISTS (SELECT 1 FROM recipes WHERE title='Roasted Butternut Squash Buddha Bowl');

