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


	@Test(expected = InvalidInputException.class)
	public void testVerifyErrorHandlingForInvalidEmployeeData() throws Exception {

		Employee employee = new Employee();

		employee.setFirstName(""); 
		employee.setEmail("invalid-email"); 
		employee.setSalary(-1000); 

		when(ConnectionHelper.getConnection()).thenReturn(mockConnection);

		employeeService.addEmployee(employee); 
	}

	@Test(expected = InvalidInputException.class)
	public void testInvalidEmailFormat() throws Exception {

		Employee employee = new Employee();
		employee.setFirstName("John");
		employee.setEmail("invalid-email"); 

		employeeService.addEmployee(employee); 
	}

	@Test(expected = InvalidInputException.class)
	public void testNegativeSalary() throws Exception {
		// Arrange
		Employee employee = new Employee();
		employee.setFirstName("John");
		employee.setEmail("john@example.com");
		employee.setSalary(-1000); 

		
		employeeService.addEmployee(employee); 
	}
}
