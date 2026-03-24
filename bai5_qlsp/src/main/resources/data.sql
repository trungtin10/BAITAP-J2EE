-- Tắt kiểm tra khóa ngoại để xóa và chèn dữ liệu không bị lỗi
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu cũ
DELETE FROM product;
DELETE FROM category;

-- Chèn dữ liệu mẫu cho Category
INSERT INTO category (id, name) VALUES (1, 'Điện thoại');
INSERT INTO category (id, name) VALUES (2, 'Laptop');
INSERT INTO category (id, name) VALUES (3, 'Phụ kiện');

-- Chèn dữ liệu mẫu cho Product
INSERT INTO product (name, price, image, category_id) VALUES ('iPhone 15 Pro Max', 30000000, 'iphone15.jpg', 1);
INSERT INTO product (name, price, image, category_id) VALUES ('Samsung Galaxy S24 Ultra', 28000000, 's24.jpg', 1);
INSERT INTO product (name, price, image, category_id) VALUES ('MacBook Pro M3', 45000000, 'macbook.jpg', 2);
INSERT INTO product (name, price, image, category_id) VALUES ('Dell XPS 15', 40000000, 'dellxps.jpg', 2);
INSERT INTO product (name, price, image, category_id) VALUES ('AirPods Pro 2', 5000000, 'airpods.jpg', 3);
INSERT INTO product (name, price, image, category_id) VALUES ('Logitech MX Master 3S', 2500000, 'mouse.jpg', 3);

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;
