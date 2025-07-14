CREATE TABLE ITEMS
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL CHECK (PRICE >= 0),
    image_url VARCHAR(255)
);

CREATE TABLE USERS (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    role     VARCHAR(50) NOT NULL
    );

CREATE TABLE WISH_ITEMS (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    item_id    BIGINT NOT NULL,
    quantity   INT    NOT NULL CHECK (quantity >= 1),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);