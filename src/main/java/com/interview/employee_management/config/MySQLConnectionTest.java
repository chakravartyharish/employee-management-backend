package com.interview.employee_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Tests MySQL connection on application startup when using the prod profile.
 * Will log success or failure messages to help verify the database is properly configured.
 */
@Profile("prod")
@Component
public class MySQLConnectionTest implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Try to execute a simple query
            String dbProductName = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
            System.out.println("========================================");
            System.out.println("MySQL CONNECTION SUCCESSFUL");
            System.out.println("Connected to: " + dbProductName);
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("MySQL CONNECTION FAILED");
            System.err.println("Error: " + e.getMessage());
            System.err.println("========================================");
            System.err.println("Please check MySQL is running and credentials are correct in application-prod.properties");
            
            // Don't throw the exception as we want the application to continue startup
            // This is just a diagnostic test
        }
    }
}
