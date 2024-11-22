package com.java.payxpert.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.java.payxpert.dao.impl.EmployeeServiceImpl;
import com.java.payxpert.model.Employee;
import com.java.payxpert.exception.InvalidInputException;
import com.java.payxpert.util.ConnectionHelper;

public class InvalidEmployeeDataTest {

	@Mock
	private Connection mockConnection;

	@Mock
	private PreparedStatement mockPreparedStatement;

	@Mock
	private ResultSet mockResultSet;

	private EmployeeServiceImpl employeeService;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		employeeService = new EmployeeServiceImpl();
	}

	// Test Case: VerifyErrorHandlingForInvalidEmployeeData
	// Objective: Ensure the system handles invalid input data gracefully
	@Test(expected = InvalidInputException.class)
	public void testVerifyErrorHandlingForInvalidEmployeeData() throws Exception {
		// Arrange
		Employee employee = new Employee();
		// Set invalid data
		employee.setFirstName(""); // Empty name should throw exception
		employee.setEmail("invalid-email"); // Invalid email format
		employee.setSalary(-1000); // Negative salary

		when(ConnectionHelper.getConnection()).thenReturn(mockConnection);

		// Act
		employeeService.addEmployee(employee); // Should throw InvalidInputException
	}

	@Test(expected = InvalidInputException.class)
	public void testInvalidEmailFormat() throws Exception {
		// Arrange
		Employee employee = new Employee();
		employee.setFirstName("John");
		employee.setEmail("invalid-email"); // Invalid email format

		// Act
		employeeService.addEmployee(employee); // Should throw InvalidInputException
	}

	@Test(expected = InvalidInputException.class)
	public void testNegativeSalary() throws Exception {
		// Arrange
		Employee employee = new Employee();
		employee.setFirstName("John");
		employee.setEmail("john@example.com");
		employee.setSalary(-1000); // Negative salary

		// Act
		employeeService.addEmployee(employee); // Should throw InvalidInputException
	}
}
