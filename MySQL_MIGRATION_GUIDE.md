# MySQL Migration Guide

This guide explains how to migrate the Employee Management application from H2 to MySQL.

## Prerequisites

1. Install MySQL Server (version 8.0 or newer recommended)
2. Have MySQL running locally on port 3306 (default)
3. Create a MySQL user with appropriate permissions or use the root user

## Configuration Changes (Already Done)

1. Added MySQL Connector dependency in `pom.xml`
2. Created environment-specific property files:
   - `application-dev.properties` - Uses H2 for development
   - `application-prod.properties` - Uses MySQL for production
3. Set default profile to `prod` in `application.properties`
4. Adjusted Hibernate dialect and DDL settings

## Steps to Complete the Migration

### 1. Start MySQL Server

Ensure MySQL Server is running on your machine

### 2. Set Up Database

Option A: Let Hibernate create the schema automatically
- The application will create the database and tables on startup with `spring.jpa.hibernate.ddl-auto=update`

Option B: Create the database manually
- Run the SQL script at `src/main/resources/db/mysql-schema.sql` in your MySQL client

### 3. Configure MySQL Connection

Verify and customize settings in `application-prod.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

Change the username and password to match your MySQL installation.

### 4. Run the Application

Start the Spring Boot application with the `prod` profile:
```
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

The console will show a MySQL connection test message indicating if the connection was successful.

### 5. Switching Between Environments

- For development with H2 (in-memory database):
  ```
  mvn spring-boot:run -Dspring-boot.run.profiles=dev
  ```

- For production with MySQL:
  ```
  mvn spring-boot:run -Dspring-boot.run.profiles=prod
  ```

### Troubleshooting

1. **Connection Refused**
   - Ensure MySQL is running
   - Verify port 3306 is not blocked by a firewall

2. **Access Denied**
   - Check username and password in `application-prod.properties`
   - Ensure the user has appropriate permissions

3. **Database Not Found**
   - The application is configured to create the database if it doesn't exist
   - If using a restricted MySQL user, create the database manually and grant permissions

4. **Table Creation Failures**
   - Check MySQL logs for specific errors
   - Consider setting `spring.jpa.hibernate.ddl-auto=create` temporarily to force recreation of tables

## Data Persistence

Unlike H2's in-memory mode, MySQL will persist data between application restarts. The data initialization that populates sample data will only run if the database is empty.
