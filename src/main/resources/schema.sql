DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS members;

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    price NUMERIC(20),
    image_url VARCHAR(1000)
);

CREATE TABLE members (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    role VARCHAR(30) DEFAULT 'USER' NOT NULL
);

CREATE TABLE wishlist (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    UNIQUE(member_id, product_id)
)