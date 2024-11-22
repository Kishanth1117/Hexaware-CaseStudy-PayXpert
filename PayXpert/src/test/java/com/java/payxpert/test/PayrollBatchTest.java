package com.java.payxpert.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.java.payxpert.dao.impl.PayrollServiceImpl;
import com.java.payxpert.model.PayRoll;
import com.java.payxpert.util.ConnectionHelper;

public class PayrollBatchTest {

	@Mock
	private Connection mockConnection;

	@Mock
	private PreparedStatement mockPreparedStatement;

	@Mock
	private ResultSet mockResultSet;

	private PayrollServiceImpl payrollService;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		payrollService = new PayrollServiceImpl();
	}

	// Test Case: ProcessPayrollForMultipleEmployees
	// Objective: Test the end-to-end payroll processing for a batch of employees
	@Test
	public void testProcessPayrollForMultipleEmployees() throws Exception {
		// Arrange
		String startDate = "2023-01-01";
		String endDate = "2023-01-31";

		when(ConnectionHelper.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true, true, true, false); // 3 employees

		// Mock data for each employee
		when(mockResultSet.getInt("employee_id")).thenReturn(1, 2, 3);
		when(mockResultSet.getDouble("basic_salary")).thenReturn(5000.0, 6000.0, 7000.0);

		// Act
		List<PayRoll> payrolls = payrollService.getPayrollsForPeriod(startDate, endDate);

		// Assert
		assertNotNull(payrolls);
		assertEquals(3, payrolls.size());
		assertTrue(payrolls.stream().allMatch(p -> p.getBasicSalary() > 0));
	}
}
