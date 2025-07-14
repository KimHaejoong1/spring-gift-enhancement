create table product (
  id bigint auto_increment primary key,
  name varchar(255),
  price int,
  imageUrl varchar(255)
);

create table member (
  id bigint auto_increment primary key,
  email varchar(255),
  password varchar(255)
);

create table wishlist (
  id bigint auto_increment primary key,
  product_id bigint,
  member_id bigint,
  foreign key(product_id) references product(id) on delete cascade,
  foreign key(member_id) references member(id) on delete cascade,
  unique (product_id, member_id)
);