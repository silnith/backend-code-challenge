
create table Address
(
    id        bigint              not null primary key auto_increment,
    address1  varchar(255)        not null,
    address2  varchar(255),
    city      varchar(255)        not null,
    state     varchar(100)        not null,
    postal    varchar(10)         not null
);

create table User
(
    id        bigint              not null primary key,
    firstName varchar(255)        not null,
    lastName  varchar(255)        not null,
    username  varchar(255) unique not null,
    password  varchar(255)        not null, -- WHAT!? NOT ENCRYPTED!? ;-)
    addressId bigint,
    constraint user_address_fk foreign key (addressId) references Address(id)
);

insert into Address
    (id, address1, address2, city, state, postal)
values
    (1, '1600 Pennsylvania Ave.', 'c/o Fools', 'Washington', 'District of Columbia', '20500'),
    (2, '1590 Pennsylvania Ave.', 'Apt. 42', 'Washington', 'District of Columbia', '20500');

insert into User
    (id, firstName, lastName, username, password, addressId)
values
    (1, 'Phil', 'Ingwell', 'PhilIngwell', 'Password123', 1),
    (2, 'Anna', 'Conda', 'AnnaConda', 'Password234', 2);
