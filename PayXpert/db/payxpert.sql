-- Create the database
CREATE DATABASE IF NOT EXISTS payxpert;
USE payxpert;

-- Create employees table
CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    hire_date DATE NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'OTHER'))
);

-- Create payroll table
CREATE TABLE payroll (
    payroll_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    basic_salary DECIMAL(10,2) NOT NULL,
    overtime_pay DECIMAL(10,2),
    deductions DECIMAL(10,2),
    net_salary DECIMAL(10,2) NOT NULL,
    pay_period_start DATE NOT NULL,
    pay_period_end DATE NOT NULL,
    payment_date DATE NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Create tax table
CREATE TABLE tax (
    tax_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    tax_year VARCHAR(4) NOT NULL,
    taxable_income DECIMAL(10,2) NOT NULL,
    tax_amount DECIMAL(10,2) NOT NULL,
    tax_percentage DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Create financial_records table
CREATE TABLE financial_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    record_date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    record_type VARCHAR(50) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Employee Table Entries
INSERT INTO employees (first_name, last_name, email, phone_number, hire_date, job_title, department, salary, gender) 
VALUES 
('John', 'Doe', 'john.doe@email.com', '1234567890', '2023-01-15', 'Software Engineer', 'IT', 75000.00, 'MALE'),
('Jane', 'Smith', 'jane.smith@email.com', '2345678901', '2023-02-20', 'HR Manager', 'Human Resources', 65000.00, 'FEMALE'),
('Mike', 'Johnson', 'mike.j@email.com', '3456789012', '2023-03-10', 'Senior Developer', 'IT', 95000.00, 'MALE'),
('Sarah', 'Williams', 'sarah.w@email.com', '4567890123', '2023-04-05', 'Financial Analyst', 'Finance', 70000.00, 'FEMALE'),
('David', 'Brown', 'david.b@email.com', '5678901234', '2023-05-01', 'Project Manager', 'IT', 85000.00, 'MALE');

-- Payroll Table Entries
INSERT INTO payroll (employee_id, pay_period_start, pay_period_end, basic_salary, overtime_pay, deductions, net_salary, payment_date) 
VALUES 
(1, '2023-11-01', '2023-11-30', 6250.00, 500.00, 1000.00, 5750.00, '2023-11-30'),
(2, '2023-11-01', '2023-11-30', 5416.67, 0.00, 800.00, 4616.67, '2023-11-30'),
(3, '2023-11-01', '2023-11-30', 7916.67, 1000.00, 1500.00, 7416.67, '2023-11-30'),
(4, '2023-11-01', '2023-11-30', 5833.33, 300.00, 900.00, 5233.33, '2023-11-30'),
(5, '2023-11-01', '2023-11-30', 7083.33, 700.00, 1200.00, 6583.33, '2023-11-30');

-- Tax Table Entries
INSERT INTO tax (employee_id, tax_year, taxable_income, tax_amount, tax_percentage) 
VALUES 
(1, '2023', 75000.00, 15000.00, 20.00),
(2, '2023', 65000.00, 12000.00, 18.46),
(3, '2023', 95000.00, 23750.00, 25.00),
(4, '2023', 70000.00, 13500.00, 19.29),
(5, '2023', 85000.00, 19500.00, 22.94);

-- Financial Records Table Entries
INSERT INTO financial_records (employee_id, record_date, description, amount, record_type) 
VALUES 
(1, '2023-11-15', 'Performance Bonus', 5000.00, 'BONUS'),
(2, '2023-11-15', 'Health Insurance', 200.00, 'DEDUCTION'),
(3, '2023-11-15', 'Project Completion Bonus', 7500.00, 'BONUS'),
(4, '2023-11-15', 'Professional Development', 1000.00, 'DEDUCTION'),
(5, '2023-11-15', 'Sales Commission', 3000.00, 'INCOME');

-- Disable foreign key checks to avoid constraint issues
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all tables
DROP TABLE IF EXISTS financial_records;
DROP TABLE IF EXISTS tax;
DROP TABLE IF EXISTS payroll;
DROP TABLE IF EXISTS employees;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

select * from employees;
select * from financial_records;
select * from payroll;
select * from tax;