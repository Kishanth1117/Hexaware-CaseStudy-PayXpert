package com.java.payxpert.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.java.payxpert.dao.IEmployeeService;
import com.java.payxpert.exception.*;
import com.java.payxpert.model.Employee;
import com.java.payxpert.model.Gender;
import com.java.payxpert.util.ConnectionHelper;

public class EmployeeServiceImpl implements IEmployeeService {

	@Override
	public Employee getEmployeeById(int employeeId) 
			throws EmployeeNotFoundException, DatabaseConnectionException, ClassNotFoundException {
		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM employees WHERE employee_id = ?")) {

			stmt.setInt(1, employeeId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Employee employee = new Employee();
				employee.setEmployeeId(rs.getInt("employee_id"));
				employee.setFirstName(rs.getString("first_name"));
				employee.setLastName(rs.getString("last_name"));
				employee.setEmail(rs.getString("email"));
				employee.setPhoneNumber(rs.getString("phone_number"));
				employee.setHireDate(rs.getDate("hire_date"));
				employee.setJobTitle(rs.getString("job_title"));
				employee.setDepartment(rs.getString("department"));
				employee.setSalary(rs.getDouble("salary"));
				employee.setGender(Gender.valueOf(rs.getString("gender")));
				return employee;
			} else {
				throw new EmployeeNotFoundException("Employee not found with ID: " + employeeId);
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error accessing employee data: " + e.getMessage());
		}
	}

	@Override
	public List<Employee> getAllEmployees() 
			throws DatabaseConnectionException, ClassNotFoundException {
		List<Employee> employees = new ArrayList<>();

		try (Connection conn = ConnectionHelper.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

			while (rs.next()) {
				Employee employee = new Employee();
				employee.setEmployeeId(rs.getInt("employee_id"));
				employee.setFirstName(rs.getString("first_name"));
				employee.setLastName(rs.getString("last_name"));
				employee.setEmail(rs.getString("email"));
				employee.setPhoneNumber(rs.getString("phone_number"));
				employee.setHireDate(rs.getDate("hire_date"));
				employee.setJobTitle(rs.getString("job_title"));
				employee.setDepartment(rs.getString("department"));
				employee.setSalary(rs.getDouble("salary"));
				employee.setGender(Gender.valueOf(rs.getString("gender")));
				employees.add(employee);
			}
			return employees;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error accessing employee data: " + e.getMessage());
		}
	}

	@Override
	public boolean addEmployee(Employee employee) 
			throws InvalidInputException, DatabaseConnectionException, ClassNotFoundException {
		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO employees (first_name, last_name, email, phone_number, " +
								"hire_date, job_title, department, salary, gender) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

			validateEmployee(employee);

			stmt.setString(1, employee.getFirstName());
			stmt.setString(2, employee.getLastName());
			stmt.setString(3, employee.getEmail());
			stmt.setString(4, employee.getPhoneNumber());
			stmt.setDate(5, new java.sql.Date(employee.getHireDate().getTime()));
			stmt.setString(6, employee.getJobTitle());
			stmt.setString(7, employee.getDepartment());
			stmt.setDouble(8, employee.getSalary());
			stmt.setString(9, employee.getGender().toString());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error adding employee: " + e.getMessage());
		}
	}

	@Override
	public boolean updateEmployee(Employee employee) 
			throws InvalidInputException, DatabaseConnectionException, EmployeeNotFoundException, ClassNotFoundException {
		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"UPDATE employees SET first_name=?, last_name=?, email=?, phone_number=?, " +
								"hire_date=?, job_title=?, department=?, salary=?, gender=? " +
						"WHERE employee_id=?")) {

			validateEmployee(employee);

			stmt.setString(1, employee.getFirstName());
			stmt.setString(2, employee.getLastName());
			stmt.setString(3, employee.getEmail());
			stmt.setString(4, employee.getPhoneNumber());
			stmt.setDate(5, new java.sql.Date(employee.getHireDate().getTime()));
			stmt.setString(6, employee.getJobTitle());
			stmt.setString(7, employee.getDepartment());
			stmt.setDouble(8, employee.getSalary());
			stmt.setString(9, employee.getGender().toString());
			stmt.setInt(10, employee.getEmployeeId());

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected == 0) {
				throw new EmployeeNotFoundException("Employee not found with ID: " + employee.getEmployeeId());
			}
			return true;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error updating employee: " + e.getMessage());
		}
	}

	@Override
	public boolean removeEmployee(int employeeId) 
			throws DatabaseConnectionException, EmployeeNotFoundException, ClassNotFoundException {
		Connection conn = null;
		try {
			conn = ConnectionHelper.getConnection();
			conn.setAutoCommit(false); // Start transaction

			try {

				deleteEmployeeDeductions(conn, employeeId);    
				deleteAttendanceRecords(conn, employeeId);     
				deleteFinancialRecords(conn, employeeId);
				deletePayrollRecords(conn, employeeId);
				deleteTaxRecords(conn, employeeId);

				PreparedStatement stmt = conn.prepareStatement(
						"DELETE FROM employees WHERE employee_id = ?");
				stmt.setInt(1, employeeId);
				int rowsAffected = stmt.executeUpdate();

				if (rowsAffected == 0) {
					throw new EmployeeNotFoundException("Employee not found with ID: " + employeeId);
				}

				conn.commit(); 
				return true;

			} catch (Exception e) {
				conn.rollback(); 
				throw e;
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error removing employee: " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					throw new DatabaseConnectionException("Error : " + e.getMessage());
				}
			}
		}
	}

	private void deleteEmployeeDeductions(Connection conn, int employeeId) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM employee_deductions WHERE employee_id = ?");
		stmt.setInt(1, employeeId);
		stmt.executeUpdate();
	}

	private void deleteAttendanceRecords(Connection conn, int employeeId) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM attendance WHERE employee_id = ?");
		stmt.setInt(1, employeeId);
		stmt.executeUpdate();
	}

	private void deleteFinancialRecords(Connection conn, int employeeId) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM financial_records WHERE employee_id = ?");
		stmt.setInt(1, employeeId);
		stmt.executeUpdate();
	}

	private void deletePayrollRecords(Connection conn, int employeeId) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM payroll WHERE employee_id = ?");
		stmt.setInt(1, employeeId);
		stmt.executeUpdate();
	}

	private void deleteTaxRecords(Connection conn, int employeeId) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM tax WHERE employee_id = ?");
		stmt.setInt(1, employeeId);
		stmt.executeUpdate();
	}

	private void validateEmployee(Employee employee) throws InvalidInputException {
		if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
			throw new InvalidInputException("First name cannot be empty");
		}
		if (!employee.getFirstName().matches("^[A-Za-z\\s]{2,50}$")) {
			throw new InvalidInputException("First name must be 2-50 characters long and contain only letters");
		}
		if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
			throw new InvalidInputException("Last name cannot be empty");
		}
		if (!employee.getLastName().matches("^[A-Za-z\\s]{2,50}$")) {
			throw new InvalidInputException("Last name must be 2-50 characters long and contain only letters");
		}
		if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
			throw new InvalidInputException("Email cannot be empty");
		}
		if (!employee.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new InvalidInputException("Invalid email format");
		}
		if (employee.getPhoneNumber() == null || employee.getPhoneNumber().trim().isEmpty()) {
			throw new InvalidInputException("Phone number cannot be empty");
		}
		if (!employee.getPhoneNumber().matches("^\\d{10}$")) {
			throw new InvalidInputException("Phone number must be 10 digits");
		}
		if (employee.getHireDate() == null) {
			throw new InvalidInputException("Hire date cannot be null");
		}
		if (employee.getHireDate().after(new java.util.Date())) {
			throw new InvalidInputException("Hire date cannot be in the future");
		}
		if (employee.getJobTitle() == null || employee.getJobTitle().trim().isEmpty()) {
			throw new InvalidInputException("Job title cannot be empty");
		}
		if (employee.getJobTitle().length() < 2 || employee.getJobTitle().length() > 100) {
			throw new InvalidInputException("Job title must be between 2 and 100 characters");
		}
		if (employee.getDepartment() == null || employee.getDepartment().trim().isEmpty()) {
			throw new InvalidInputException("Department cannot be empty");
		}
		if (employee.getDepartment().length() < 2 || employee.getDepartment().length() > 100) {
			throw new InvalidInputException("Department must be between 2 and 100 characters");
		}
		if (employee.getSalary() <= 0) {
			throw new InvalidInputException("Salary must be greater than 0");
		}
		if (employee.getSalary() > 1000000) { 
			throw new InvalidInputException("Salary exceeds maximum limit");
		}
		if (employee.getGender() == null) {
			throw new InvalidInputException("Gender cannot be null");
		}

	}
}
