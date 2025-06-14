-- Insert sample departments
INSERT INTO department (id, name, description) VALUES 
('DEPT001', 'Engineering', 'Software development and engineering department') ON CONFLICT (id) DO NOTHING;
INSERT INTO department (id, name, description) VALUES 
('DEPT002', 'Marketing', 'Marketing and sales department') ON CONFLICT (id) DO NOTHING;
INSERT INTO department (id, name, description) VALUES 
('DEPT003', 'Human Resources', 'HR department handling employee management') ON CONFLICT (id) DO NOTHING;
INSERT INTO department (id, name, description) VALUES 
('DEPT004', 'Finance', 'Finance and accounting department') ON CONFLICT (id) DO NOTHING;

-- Insert sample employees
INSERT INTO employee (id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id) VALUES 
('EMP001', 'John', 'Doe', 'john.doe@example.com', '555-1234', '2022-01-15', 'Software Engineer', 85000.00, 'DEPT001') ON CONFLICT (id) DO NOTHING;
INSERT INTO employee (id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id) VALUES 
('EMP002', 'Jane', 'Smith', 'jane.smith@example.com', '555-5678', '2022-02-20', 'Marketing Specialist', 65000.00, 'DEPT002') ON CONFLICT (id) DO NOTHING;
INSERT INTO employee (id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id) VALUES 
('EMP003', 'Michael', 'Johnson', 'michael.johnson@example.com', '555-9012', '2022-03-10', 'HR Manager', 75000.00, 'DEPT003') ON CONFLICT (id) DO NOTHING;
INSERT INTO employee (id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id) VALUES 
('EMP004', 'Emily', 'Williams', 'emily.williams@example.com', '555-3456', '2022-04-05', 'Financial Analyst', 70000.00, 'DEPT004') ON CONFLICT (id) DO NOTHING;
INSERT INTO employee (id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id) VALUES 
('EMP005', 'David', 'Brown', 'david.brown@example.com', '555-7890', '2022-05-15', 'Senior Developer', 95000.00, 'DEPT001') ON CONFLICT (id) DO NOTHING;

-- Insert sample products
INSERT INTO product (id, name, description, price) VALUES 
('PROD001', 'Basic Plan', 'Basic employee management subscription', 19.99) ON CONFLICT (id) DO NOTHING;
INSERT INTO product (id, name, description, price) VALUES 
('PROD002', 'Pro Plan', 'Professional employee management subscription with advanced features', 49.99) ON CONFLICT (id) DO NOTHING;
INSERT INTO product (id, name, description, price) VALUES 
('PROD003', 'Enterprise Plan', 'Enterprise-level employee management solution', 99.99) ON CONFLICT (id) DO NOTHING;
