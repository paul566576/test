create table customers
(
    id    int          not null auto_increment,
    email varchar(45)  not null unique,
    pwd   varchar(200) not null,
    role  varchar(45)  not null,
    PRIMARY KEY (id)
);

insert into customers (email, pwd, role)
values
    ('admin@test.com', '{bcrypt}$2a$12$mdmjzY2p3qkC/Qb5flL/6OZJJ2EXkSEC51mHMkuHd8Tp/JbNxb6YO', 'admin'),
       ('user@test.com', '{bcrypt}$2a$12$mdmjzY2p3qkC/Qb5flL/6OZJJ2EXkSEC51mHMkuHd8Tp/JbNxb6YO', 'read'),
       ('user1@test.com', '{bcrypt}$2a$12$lG6rQEz2ncfIFyJF23Zc4eX4KugTVLIfZgCP5UUc7uXrrqnN/SWgy', 'read');
# User1234!
