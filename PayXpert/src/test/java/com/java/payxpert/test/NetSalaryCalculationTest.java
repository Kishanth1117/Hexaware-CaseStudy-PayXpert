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
import com.java.payxpert.exception.*;

public class NetSalaryCalculationTest {

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

	// Test Case 1: CalculateGrossSalaryForEmployee
	// Objective: Verify that the system correctly calculates the gross salary for an employee
	@Test
	public void testCalculateGrossSalaryForEmployee() throws Exception {
		// Arrange
		int employeeId = 1;
		double basicSalary = 50000.0;
		double overtime = 5000.0;
		double expectedGrossSalary = basicSalary + overtime;

		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getDouble("basic_salary")).thenReturn(basicSalary);
		when(mockResultSet.getDouble("overtime_pay")).thenReturn(overtime);

		// Act
		PayRoll payroll = payrollService.generatePayroll(employeeId, "2023-01-01", "2023-01-31");

		// Assert
		assertEquals(expectedGrossSalary, payroll.getBasicSalary() + payroll.getOvertime(), 0.01);
	}

	// Test Case 2: CalculateNetSalaryAfterDeductions
	// Objective: Ensure that the system accurately calculates the net salary after deductions
	@Test
	public void testCalculateNetSalaryAfterDeductions() throws Exception {
		// Arrange
		int employeeId = 1;
		double basicSalary = 50000.0;
		double overtime = 5000.0;
		double deductions = 10000.0;
		double expectedNetSalary = basicSalary + overtime - deductions;

		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getDouble("basic_salary")).thenReturn(basicSalary);
		when(mockResultSet.getDouble("overtime_pay")).thenReturn(overtime);
		when(mockResultSet.getDouble("deductions")).thenReturn(deductions);

		// Act
		PayRoll payroll = payrollService.generatePayroll(employeeId, "2023-01-01", "2023-01-31");

		// Assert
		assertEquals(expectedNetSalary, payroll.getNetSalary(), 0.01);
	}

	// Test Case 3: VerifyTaxCalculationForHighIncomeEmployee
	// Objective: Test the system's ability to calculate taxes for a high-income employee
	@Test
	public void testVerifyTaxCalculationForHighIncomeEmployee() throws Exception {
		// Arrange
		int employeeId = 1;
		double basicSalary = 1500000.0; // High income
		double overtime = 100000.0;
		double expectedTaxRate = 0.30; // 30% for high income

		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getDouble("basic_salary")).thenReturn(basicSalary);
		when(mockResultSet.getDouble("overtime_pay")).thenReturn(overtime);

		// Act
		PayRoll payroll = payrollService.generatePayroll(employeeId, "2023-01-01", "2023-01-31");

		// Assert
		assertTrue(payroll.getDeductions() > (basicSalary + overtime) * expectedTaxRate);
	}

	// Test Case 4: ProcessPayrollForMultipleEmployees
	// Objective: Test the end-to-end payroll processing for a batch of employees
	@Test
	public void testProcessPayrollForMultipleEmployees() throws Exception {
		// Arrange
		String startDate = "2023-01-01";
		String endDate = "2023-01-31";

		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true, true, true, false); // 3 employees

		// Act
		List<PayRoll> payrolls = payrollService.getPayrollsForPeriod(startDate, endDate);

		// Assert
		assertNotNull(payrolls);
		assertEquals(3, payrolls.size());
	}

	// Test Case 5: VerifyErrorHandlingForInvalidEmployeeData
	// Objective: Ensure the system handles invalid input data gracefully
	@Test(expected = PayrollGenerationException.class)
	public void testVerifyErrorHandlingForInvalidEmployeeData() throws Exception {
		// Arrange
		int invalidEmployeeId = -1;

		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(false);

		// Act
		payrollService.generatePayroll(invalidEmployeeId, "2023-01-01", "2023-01-31");
		// Should throw PayrollGenerationException
	}
}
