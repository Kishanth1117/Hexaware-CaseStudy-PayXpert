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
