create table products
(
    id        bigint auto_increment primary key,
    name      varchar(255)   NOT NULL,
    price     decimal(10, 2) NOT NULL,
    image_url varchar(512)
);

create table members
(
    id       bigint auto_increment primary key,
    email    varchar(255) NOT NULL unique,
    password varchar(255) NOT NULL
);

create table wishlists
(
    id         bigint auto_increment primary key,
    member_id  bigint,
    product_id bigint,
    quantity   int
)