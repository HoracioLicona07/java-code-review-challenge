-- Productos semilla
INSERT INTO products (name, description, price, stock, category, active, created_at, updated_at)
VALUES
  ('Laptop HP 15', 'Laptop HP 15 pulgadas, 8GB RAM, 256GB SSD', 12999.99, 10, 'ELECTRONICS', true, NOW(), NOW()),
  ('Mouse Logitech MX', 'Mouse inalámbrico ergonómico', 899.50, 50, 'ELECTRONICS', true, NOW(), NOW()),
  ('Teclado Mecánico', 'Teclado mecánico RGB switches Blue', 1200.00, 30, 'ELECTRONICS', true, NOW(), NOW()),
  ('Silla Gamer', 'Silla gaming con soporte lumbar', 4500.00, 5, 'FURNITURE', true, NOW(), NOW()),
  ('Monitor 24"', 'Monitor Full HD 144Hz', 5200.00, 8, 'ELECTRONICS', true, NOW(), NOW()),
  ('Webcam HD', 'Webcam 1080p con micrófono', 650.00, 20, 'ELECTRONICS', true, NOW(), NOW()),
  ('Auriculares Sony', 'Auriculares inalámbricos con cancelación de ruido', 3200.00, 15, 'ELECTRONICS', false, NOW(), NOW()),
  ('Libreta Rhodia', 'Libreta A5 cuadriculada 80 hojas', 120.00, 100, 'STATIONERY', true, NOW(), NOW());

-- Clientes semilla
INSERT INTO customers (name, email, phone, active, created_at)
VALUES
  ('Carlos Méndez', 'carlos.mendez@email.com', '5512345678', true, NOW()),
  ('Ana García', 'ana.garcia@email.com', '5587654321', true, NOW()),
  ('Luis Rodríguez', 'luis.rod@email.com', '5598765432', true, NOW()),
  ('María López', 'maria.lopez@email.com', '5523456789', false, NOW());
