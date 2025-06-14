-- Create database if not exists
CREATE DATABASE IF NOT EXISTS employee_management;
USE employee_management;

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

-- Create employees table with foreign key to departments
CREATE TABLE IF NOT EXISTS employees (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    position VARCHAR(255) NOT NULL,
    salary DOUBLE NOT NULL,
    department_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Insert default departments if needed
-- INSERT INTO departments (id, name, location) VALUES ('dept01', 'Human Resources', 'Building A');
-- INSERT INTO departments (id, name, location) VALUES ('dept02', 'Engineering', 'Building B');
