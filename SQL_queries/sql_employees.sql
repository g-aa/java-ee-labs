create table employees(
code integer,
first_name varchar(24) not null,
last_name varchar(32) not null,
login varchar(16) not null,
password varchar(16),
constraint emp_code_pk primary key(code)
)


insert into employees(code, first_name, last_name, login, password) 
values (1, 'Andrey', 'Georgievskiy', 'ADM', '1234567890!');


insert into employees(code, first_name, last_name, login, password) 
values (2, 'Kirill', 'Petrov', 'STAR', 'asAS1!');


insert into employees(code, first_name, last_name, login, password) 
values (3, 'Victor', 'Sidorov', 'void', 'NULL?');


insert into employees(code, first_name, last_name, login, password) 
values (4, 'Ivan', 'Ivanov', 'Ivanov', 'Aa11111111');
