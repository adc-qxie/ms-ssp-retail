
INSERT INTO store (store_id, name)
VALUES (1, 'my first test store');

INSERT INTO product(product_id, store_id, name, description, sku, price)
VALUES(1, 1, 'toy car 1', 'a toy car', 'abc123', '10.00');

INSERT INTO product(product_id, store_id, name, description, sku, price)
VALUES(2, 1, 'toy car 2', 'a toy car', 'abc124', '20.00');

INSERT INTO stock(product_id, store_id, quantity)
VALUES(1, 1, 10);

INSERT INTO stock(product_id, stock_id, quantity)
VALUES(2, 1, 1);

INSERT INTO store (store_id, name)
VALUES (2, 'my second test store');

INSERT INTO product(product_id, store_id, name, description, sku, price)
VALUES(3, 2, 'shoes 1', 'sneaker shoes', 'shoe1', '100.00');

INSERT INTO product(product_id, store_id, name, description, sku, price)
VALUES(4, 2, 'shoes 2', 'boots', 'shoe2', '200.00');

INSERT INTO stock(product_id, store_id, quantity)
VALUES(3, 2, 10);

INSERT INTO stock(product_id, store_id, quantity)
VALUES(4, 2, 5);
