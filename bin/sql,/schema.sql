CREATE DATABASE IF NOT EXISTS ecommerce;
USE ecommerce;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('CUSTOMER','ADMIN') DEFAULT 'CUSTOMER'
);

CREATE TABLE categories (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE products (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  image VARCHAR(500),
  category_id INT NOT NULL,
  FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE orders (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  status ENUM('PENDING','SHIPPED','DELIVERED') DEFAULT 'PENDING',
  address VARCHAR(300) NOT NULL,
  order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT INTO categories(name) VALUES ('Electronics'), ('Clothing'), ('Books');

INSERT INTO products(name, description, price, stock, image, category_id) VALUES
('Wireless Earbuds', 'Bluetooth 5.0 earbuds with charging case.', 1499.00, 25, '', 1),
('Smart Watch', 'Fitness tracker with heart-rate monitor.', 2999.00, 15, '', 1),
('Power Bank 10000mAh', 'Fast charging portable charger.', 999.00, 40, '', 1),
('Cotton T-Shirt', 'Comfortable round-neck t-shirt.', 399.00, 60, '', 2),
('Denim Jeans', 'Slim fit blue jeans.', 1299.00, 30, '', 2),
('Java Programming Book', 'Learn core Java step by step.', 599.00, 20, '', 3);