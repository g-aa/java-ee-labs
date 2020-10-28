create table employeehistory(
id integer,
position varchar(24) not null,
manager integer,
hire date not null,
dismiss date,
code integer,
constraint emphist_id_pk primary key(id),
constraint emphist_code_fk foreign key(code) references employees(code),
constraint emphist_manager_check check(manager > 0),
constraint emphist_dismiss_check check(dismiss >= hire)
);


insert into employeehistory(id, position, manager, hire, dismiss, code)
values (101, 'manager', 33, '15.05.2012', '25.10.2015', 2);

insert into employeehistory(id, position, manager, hire, dismiss, code)
values (102, 'clerk', 7, '17.11.2015', '25.01.2018', 2);

insert into employeehistory(id, position, manager, hire, dismiss, code)
values (103, 'engineer', 24, '11.02.2012', '08.06.2016', 1);

insert into employeehistory(id, position, manager, hire, dismiss, code)
values (104, 'accountant', 15, '01.08.2010', '23.12.2013', 3);

insert into employeehistory(id, position, manager, hire, dismiss, code)
values (105, 'manager', 32, '11.05.2014', '13.10.2017', 3);

insert into employeehistory(id, position, manager, hire, dismiss, code)
values (106, 'clerk', 15, '05.06.2009', '20.10.2016', 4);

insert into employeehistory(id, position, manager, hire, dismiss, code)
values (107, 'engineer', 24, '01.02.2013', '13.11.2018', 1);