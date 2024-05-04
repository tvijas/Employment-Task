CREATE SCHEMA IF NOT EXISTS mydatabase;
USE mydatabase;
create table if not exists mydatabase.users
(
    `id`           int         NOT NULL AUTO_INCREMENT,
    `email`        varchar(45) NOT NULL,
    `first_name`   varchar(45) NOT NULL,
    `last_name`    varchar(45) NOT NULL,
    `birth_date`   date        NOT NULL,
    `address`      varchar(45) DEFAULT NULL,
    `phone_number` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

create schema if not exists test_mydatabase;
USE test_mydatabase;
create table if not exists test_mydatabase.users
(
    `id`           int         NOT NULL AUTO_INCREMENT,
    `email`        varchar(45) NOT NULL,
    `first_name`   varchar(45) NOT NULL,
    `last_name`    varchar(45) NOT NULL,
    `birth_date`   date        NOT NULL,
    `address`      varchar(45) DEFAULT NULL,
    `phone_number` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO test_mydatabase.users
(email, first_name, last_name, birth_date, address, phone_number)
VALUES ('user1@example.com', 'John', 'Doe', '1992-01-01', '123 Main St', '123-456-7890'),
       ('user2@example.com', 'Jane', 'Doe', '1993-02-02', '456 Elm St', '234-567-8901'),
       ('user3@example.com', 'Alice', 'Smith', '1995-03-03', '789 Oak St', '345-678-9012'),
       ('user4@example.com', 'Bob', 'Johnson', '2000-04-04', '321 Pine St', '456-789-0123'),
       ('user5@example.com', 'Carol', 'Williams', '1990-05-05', '654 Cedar St', '567-890-1234'),
       ('user6@example.com', 'David', 'Jones', '1990-06-06', '987 Maple St', '678-901-2345'),
       ('user7@example.com', 'Eve', 'Brown', '1990-07-07', '789 Birch St', '789-012-3456'),
       ('user8@example.com', 'Frank', 'Miller', '1991-08-08', '654 Walnut St', '890-123-4567'),
       ('user9@example.com', 'Grace', 'Wilson', '1980-09-09', '321 Oak St', '901-234-5678'),
       ('user10@example.com', 'Harry', 'Taylor', '1990-10-10', '123 Elm St', '012-345-6789');
