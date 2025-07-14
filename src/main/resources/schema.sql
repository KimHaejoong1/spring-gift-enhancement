create table product (
    id bigint auto_increment primary key,
    name varchar(255) not null,
    price bigint not null,
    image_url varchar(1000)
);

create table member (
    id bigint auto_increment primary key,
    email varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(50) not null
);

create table wishlist (
    id bigint auto_increment primary key,
    member_id bigint not null,
    product_id bigint not null,
    created_date timestamp(6) not null,
    foreign key (member_id) references member(id) on delete cascade,
    foreign key (product_id) references product(id) on delete cascade,
    unique (member_id, product_id)
);