create table Products
(
    id varchar(25) not null
        primary key,
    name varchar(255) not null,
    expirationDate date,
    quantityInStock int,
    quantitySold int
)
    go