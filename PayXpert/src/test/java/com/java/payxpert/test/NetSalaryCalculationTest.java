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
import com.java.payxpert.util.ConnectionHelper;

public class NetSalaryCalculationTest {

	@Mock
	private Connection mockConnection;

	@Mock
	private PreparedStatement mockPreparedStatement;

	@Mock
	private ResultSet mockResultSet;

	private PayrollServiceImpl payrollService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		payrollService = new PayrollServiceImpl();


		try (var mockedStatic = mockStatic(ConnectionHelper.class)) {
			mockedStatic.when(ConnectionHelper::getConnection).thenReturn(mockConnection);
		}


		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
	}

	@Test
	public void testCalculateNetSalaryAfterDeductions() throws Exception {

		int employeeId = 1;
		double basicSalary = 75000.00; 
		double expectedMonthlySalary = basicSalary / 12;
		double overtimeHours = 6.0;


		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getDouble("salary")).thenReturn(basicSalary);
		when(mockResultSet.getDouble("total_overtime")).thenReturn(overtimeHours);


		PayRoll payroll = payrollService.generatePayroll(employeeId, "2024-01-01", "2024-01-31");


		assertNotNull("Payroll should not be null", payroll);
		assertEquals("Basic salary should match", expectedMonthlySalary, payroll.getBasicSalary(), 0.01);
		assertTrue("Overtime pay should be greater than 0", payroll.getOvertime() > 0);
		assertTrue("Net salary should be less than gross salary", 
				payroll.getNetSalary() < (payroll.getBasicSalary() + payroll.getOvertime()));
	}
}