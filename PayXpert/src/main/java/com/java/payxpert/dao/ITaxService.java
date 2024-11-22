package com.java.payxpert.dao;

import java.util.List;
import com.java.payxpert.model.Tax;
import com.java.payxpert.exception.*;

public interface ITaxService {
	Tax calculateTax(int employeeId, String taxYear) 
			throws TaxCalculationException, DatabaseConnectionException, ClassNotFoundException;

	Tax getTaxById(int taxId) 
			throws TaxCalculationException, DatabaseConnectionException, ClassNotFoundException;

	List<Tax> getTaxesForEmployee(int employeeId) 
			throws TaxCalculationException, DatabaseConnectionException, ClassNotFoundException;

	List<Tax> getTaxesForYear(String taxYear) 
			throws TaxCalculationException, DatabaseConnectionException, ClassNotFoundException;
}

