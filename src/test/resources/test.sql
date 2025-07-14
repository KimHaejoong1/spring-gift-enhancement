DROP TABLE product;
DROP TABLE member;

CREATE TABLE product (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         price BIGINT NOT NULL,
                         image_url VARCHAR(500) NOT NULL,
                         is_kakao_approved BOOLEAN NOT NULL DEFAULT FALSE,
                         PRIMARY KEY (id)
);

CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        email VARCHAR(200) NOT NULL,
                        salt VARCHAR(100) NOT NULL,
                        password VARCHAR(200) NOT NULL,
                        role VARCHAR(50) NOT NULL DEFAULT 'USER',
                        PRIMARY KEY (id),
                        UNIQUE (email)
);

INSERT INTO product (name, price, image_url)
VALUES
    ('한우300g', 110000, 'https://gwchild745.firstmall.kr/data/goods/1/2020/12/39585_tmp_94fdc57a45d832cde487fb6b217e1b810344large.jpg'),
    ('텀블러', 50000, 'https://i.namu.wiki/i/ErhzrRljjqaG0FLsqz0i0S6jAgBLDeV-_az6Dj5MFR_lLFXu8-eRdEFLuuyfDVQm-yLXBb0odSOycvjqwqIiauUYL3XPSy-AG-1u9m6vgXj7qHMsil3ofNME7qJpjns_Vmkxofk7WvWBTwCc-zhpvQ.webp'),
    ('립스틱', 50000, 'https://i.namu.wiki/i/0GQWD4DtVb4MCjTzYV3lvvFjQE2J05KIoU2-5TPX1Zvq3TXum5D1vYcBkVJndm0YrUntKAwNLRw-qgJ2gtD_Tic8zHCnbTEX5-OEUoiVQ_p_-hS9uD0S8zDak7jcwNtvPvqqjhjRT_NbvPMGt-6HaA.webp'),
    ('향수', 35000, 'https://image.oliveyoung.co.kr/cfimages/cf-goods/uploads/images/thumbnails/10/0000/0020/A00000020498534ko.jpg?qt=80');

INSERT INTO member (email, salt, password, role) VALUES ('admin@daum.net', '3OaHrnPIBjVIDVXt7EtDDg==', 'XnRT8uekMpgwh3R7l7X9PSECCtQd9DJYFQyHqbJPirM=', 'ADMIN');