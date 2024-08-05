drop database if exists pos;

create database pos;

use pos;

create table customer(
                         id VARCHAR(15) PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         address VARCHAR(255) NOT NULL,
                         contact VARCHAR(20) NOT NULL
);

create table item(
                     code VARCHAR(15) PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     price DECIMAL(10,2) NOT NULL,
                     qty INT NOT NULL
);

create table orders(
                       order_id VARCHAR(15) PRIMARY KEY,
                       date DATE NOT NULL,
                       cust_id VARCHAR(15) NOT NULL,
                       discount DECIMAL(4,1),
                       total DECIMAL(10,2) NOT NULL,
                       CONSTRAINT FOREIGN KEY (cust_id) REFERENCES customer(id) ON DELETE CASCADE ON UPDATE CASCADE
);

create table order_details(
                              order_id VARCHAR(15) PRIMARY KEY,
                              item_code VARCHAR(15) NOT NULL,
                              unit_price DECIMAL(10,2) NOT NULL,
                              qty INT,
                              CONSTRAINT FOREIGN KEY (item_code) REFERENCES item(code) ON DELETE CASCADE ON UPDATE CASCADE
);