CREATE TABLE store (
	store_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL DEFAULT '',
	is_deleted TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE product (
	product_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL DEFAULT '',
	description VARCHAR(255),
	sku VARCHAR(10) NOT NULL ,
	price DECIMAL(15,2) NOT NULL DEFAULT 0, 
	is_deleted TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE store_product (
	store_id BIGINT UNSIGNED NOT NULL,
	product_id BIGINT UNSIGNED NOT NULL
);

CREATE TABLE stock (
	product_id BIGINT UNSIGNED NOT NULL,
	store_id BIGINT UNSIGNED NOT NULL,
	quantity INT(11) NOT NULL DEFAULT 0,
	is_deleted TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE purchase_order (
	order_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	store_id BIGINT UNSIGNED NOT NULL,
	customer_id BIGINT UNSIGNED,
	order_amount DECIMAL(15, 2) NOT NULL DEFAULT 0,
	order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	status TINYINT(1) UNSIGNED NOT NULL default 0,
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	email VARCHAR(255),
	phone VARCHAR(20),
	address VARCHAR(255),
	tracking_number VARCHAR(100),
	is_deleted TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE customer (
   	customer_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL DEFAULT '',
	last_name VARCHAR(50) NOT NULL DEFAULT '',
	email VARCHAR(255) NOT NULL DEFAULT '',
	phone VARCHAR(20) NOT NULL DEFAULT '',
	address VARCHAR(255) NOT NULL DEFAULT '',
	is_deleted TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE order_item (
	order_id BIGINT UNSIGNED NOT NULL,
	product_id BIGINT UNSIGNED NOT NULL,
	quantity INT(11) NOT NULL,
	is_deleted TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE payment (
	payment_id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
	order_id BIGINT UNSIGNED,
	credit_card_number VARCHAR(32) NOT NULL,
	status TINYINT(1) UNSIGNED NOT NULL default 0,
	is_deleted TINYINT(1) UNSIGNED NOT NULL DEFAULT 0
);