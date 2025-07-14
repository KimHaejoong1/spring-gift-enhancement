INSERT INTO products(name, price, image_url) VALUES('p1', 1000, 'url1');
INSERT INTO products(name, price, image_url) VALUES('p2', 2000, 'url2');
INSERT INTO products(name, price, image_url) VALUES('p3', 3000, 'url3');
INSERT INTO products(name, price, image_url) VALUES('p4', 4000, 'url4');
INSERT INTO products(name, price, image_url) VALUES('p5', 5000, 'url5');

INSERT INTO members(email, password) VALUES('member1@mem', '$2a$10$141vsuswYHZmSMgqvTUP/u2sfY1881BOE32M7n8UdlHG/2XZ7IEuO');
INSERT INTO members(email, password) VALUES('member2@mem', '$2a$10$.2HSQpYl0kRbMqUrnb7V9eGJOimko1SIP0w.1TCN37LW0VNxbdzLq');
INSERT INTO members(email, password) VALUES('member3@mem', '$2a$10$Sy9sN2w9D66Tsdm3flXjQeEmb/6QhIZlSiKykUMZ9lLJrcgG/IdJ.');
INSERT INTO members(email, password) VALUES('member4@mem', '$2a$10$Xp52WG3ZkvthBuYk3r0Q6.58UAyX5qCoFymPG4.UYl6kQpxWSHWr2');
INSERT INTO members(email, password) VALUES('member5@mem', '$2a$10$cVqSwvAyY2P1RlJMyj3ZJeqcMHzpJPvky8PLf0n1qjyy9zzu6FLk.');

INSERT INTO wishlists(member_id, product_id, quantity) VALUES (1, 1, 5);
INSERT INTO wishlists(member_id, product_id, quantity) VALUES (1, 2, 5);
INSERT INTO wishlists(member_id, product_id, quantity) VALUES (1, 3, 5);

INSERT INTO wishlists(member_id, product_id, quantity) VALUES (2, 1, 5);
INSERT INTO wishlists(member_id, product_id, quantity) VALUES (2, 2, 5);
INSERT INTO wishlists(member_id, product_id, quantity) VALUES (2, 3, 5);

INSERT INTO wishlists(member_id, product_id, quantity) VALUES (3, 1, 5);
INSERT INTO wishlists(member_id, product_id, quantity) VALUES (3, 2, 5);
INSERT INTO wishlists(member_id, product_id, quantity) VALUES (3, 3, 5);