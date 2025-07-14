create table product
(
   id bigint auto_increment,
   name varchar(100),
   price int,
   image_url varchar(255),
   primary key (id)
);

create table member
(
   id bigint auto_increment,
   email varchar(100),
   password varchar(255),
   salt varchar(255),
   primary key (id)
);

create table wish
(
   id bigint auto_increment,
    member_id bigint,
    product_id bigint,
    quantity bigint,
   primary key (id),
    foreign key (member_id) references member,
    foreign key (product_id) references product
);

