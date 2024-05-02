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