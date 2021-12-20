-- create schema bike_app;

create table bike (
	id serial primary key,
	brand varchar(100) not null,
	model varchar(100) not null
);
