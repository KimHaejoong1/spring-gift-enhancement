create table if not exists member (
                        id binary(16),
                        email varchar(255),
                        password varchar(255),
                        role varchar(255),
                        primary key(id)
);

create table if not exists product (
                        id binary(16),
                        name varchar(255),
                        price int,
                        image_url text,
                        member_id binary(16) not null,
                        primary key (id)
);

create table if not exists wish_product(
                        id binary(16),
                        quantity int,
                        owner_id binary(16),
                        product_id binary(16),
                        primary key(id)
)