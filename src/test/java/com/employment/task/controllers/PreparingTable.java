package com.employment.task.controllers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@PropertySource({"classpath:test.properties"})
public class PreparingTable implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("Preparing data base for testing");
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(environment));

        String truncateTable = "truncate test_mydatabase.users";
        String insertFields = "INSERT INTO test_mydatabase.users" +
                "(email, first_name, last_name, birth_date, address, phone_number)" +
                "VALUES ('user1@example.com', 'John', 'Doe', '1992-01-01', '123 Main St', '123-456-7890')," +
                "       ('user2@example.com', 'Jane', 'Doe', '1993-02-02', '456 Elm St', '234-567-8901')," +
                "       ('user3@example.com', 'Alice', 'Smith', '1995-03-03', '789 Oak St', '345-678-9012')," +
                "       ('user4@example.com', 'Bob', 'Johnson', '2000-04-04', '321 Pine St', '456-789-0123')," +
                "       ('user5@example.com', 'Carol', 'Williams', '1990-05-05', '654 Cedar St', '567-890-1234')," +
                "       ('user6@example.com', 'David', 'Jones', '1990-06-06', '987 Maple St', '678-901-2345')," +
                "       ('user7@example.com', 'Eve', 'Brown', '1990-07-07', '789 Birch St', '789-012-3456')," +
                "       ('user8@example.com', 'Frank', 'Miller', '1991-08-08', '654 Walnut St', '890-123-4567')," +
                "       ('user9@example.com', 'Grace', 'Wilson', '1980-09-09', '321 Oak St', '901-234-5678')," +
                "       ('user10@example.com', 'Harry', 'Taylor', '1990-10-10', '123 Elm St', '012-345-6789');";

        jdbcTemplate.execute(truncateTable);
        jdbcTemplate.execute(insertFields);
    }

    public DataSource dataSource(ConfigurableEnvironment env) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + env.getProperty("db.host") + ":" + env.getProperty("db.port") + "/" + env.getProperty("db.name"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }
}