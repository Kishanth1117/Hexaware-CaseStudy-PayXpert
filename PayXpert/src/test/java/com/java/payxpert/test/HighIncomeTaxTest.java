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
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
        taxService = new TaxServiceImpl();
    }
    
    // Test Case: VerifyTaxCalculationForHighIncomeEmployee
    // Objective: Test the system's ability to calculate taxes for a high-income employee
    @Test
    public void testVerifyTaxCalculationForHighIncomeEmployee() throws Exception {
        // Arrange
        int employeeId = 1;
        String taxYear = "2023";
        double taxableIncome = 1500000.0; // High income
        
        when(ConnectionHelper.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("total_income")).thenReturn(taxableIncome);
        
        // Act
        Tax tax = taxService.calculateTax(employeeId, taxYear);
        
        // Assert
        assertNotNull(tax);
        assertEquals(employeeId, tax.getEmployeeId());
        assertEquals(taxYear, tax.getTaxYear());
        assertEquals(taxableIncome, tax.getTaxableIncome(), 0.01);
        // For high income (>1000000), tax should be more than 30%
        assertTrue(tax.getTaxPercentage() >= 30.0);
        assertTrue(tax.getTaxAmount() > 0);
    }
}
