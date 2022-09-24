create table car_user (
    user_name varchar,
    id serial primary key
);


insert into car_user (user_name) values ('Alice');
insert into car_user (user_name) values ('Bob');