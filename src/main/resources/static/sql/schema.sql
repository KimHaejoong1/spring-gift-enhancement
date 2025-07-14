CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(15) NOT NULL,
    price INT NOT NULL,
    imageUrl VARCHAR(255) NOT NULL,
    status VARCHAR(10) NOT NULL
);

CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(30) NOT NULL UNIQUE,
    passwordHash CHAR(60) NOT NULL
);

CREATE TABLE wishes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  memberId BIGINT REFERENCES members(id),
  productId BIGINT REFERENCES products(id),
  count INT,
  UNIQUE (memberId, productId)
);
