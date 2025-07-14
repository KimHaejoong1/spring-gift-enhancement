CREATE TABLE product (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(100) NOT NULL,
     price INT NOT NULL,
     image_url VARCHAR(255)
);

CREATE TABLE member(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL
);

CREATE TABLE wish(
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     member_id BIGINT NOT NULL,
     product_id BIGINT NOT NULL,
     quantity INT DEFAULT 1,
     FOREIGN KEY (member_id) REFERENCES member(id),
     FOREIGN KEY (product_id) REFERENCES product(id),
     UNIQUE(member_id, product_id)

)
