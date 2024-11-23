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

import com.java.payxpert.dao.impl.TaxServiceImpl;
import com.java.payxpert.model.Tax;
import com.java.payxpert.util.ConnectionHelper;

public class HighIncomeTaxTest {

	@Mock
	private Connection mockConnection;

	@Mock
	private PreparedStatement mockPreparedStatement;

	@Mock
	private ResultSet mockResultSet;

	private TaxServiceImpl taxService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		taxService = new TaxServiceImpl();

		try (var mockedStatic = mockStatic(ConnectionHelper.class)) {
			mockedStatic.when(ConnectionHelper::getConnection).thenReturn(mockConnection);
		}

		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
	}

	@Test
	public void testVerifyTaxCalculationForHighIncomeEmployee() throws Exception {

		int employeeId = 3;
		String taxYear = "2024";
		double salary = 95000.00;

		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getDouble("salary")).thenReturn(salary);

		Tax tax = taxService.calculateTax(employeeId, taxYear);

		assertNotNull("Tax calculation should not be null", tax);
		assertEquals("Employee ID should match", employeeId, tax.getEmployeeId());
		assertEquals("Tax year should match", taxYear, tax.getTaxYear());
		assertEquals("Taxable income should match salary", salary, tax.getTaxableIncome(), 0.01);
		assertEquals("Tax amount should be correctly calculated", 30500.00, tax.getTaxAmount(), 0.01);
		assertEquals("Tax percentage should be correctly calculated", 32.11, tax.getTaxPercentage(), 0.01);
	}
}