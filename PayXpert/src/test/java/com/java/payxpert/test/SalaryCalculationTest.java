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

import com.java.payxpert.dao.impl.PayrollServiceImpl;
import com.java.payxpert.model.PayRoll;

public class SalaryCalculationTest {

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


	@Test
	public void testCalculateGrossSalaryForEmployee() throws Exception {

		int employeeId = 1;
		double basicSalary = 50000.0;
		double overtime = 5000.0;
		double expectedGrossSalary = basicSalary + overtime;

		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getDouble("basic_salary")).thenReturn(basicSalary);
		when(mockResultSet.getDouble("overtime_pay")).thenReturn(overtime);


		PayRoll payroll = payrollService.generatePayroll(employeeId, "2023-01-01", "2023-01-31");


		assertEquals(expectedGrossSalary, payroll.getBasicSalary() + payroll.getOvertime(), 0.01);
	}
}