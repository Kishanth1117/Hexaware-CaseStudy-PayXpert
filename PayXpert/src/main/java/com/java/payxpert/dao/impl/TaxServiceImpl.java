package com.java.payxpert.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.java.payxpert.dao.ITaxService;
import com.java.payxpert.exception.*;
import com.java.payxpert.model.Tax;
import com.java.payxpert.util.ConnectionHelper;  // Changed from DBUtil

public class TaxServiceImpl implements ITaxService {

	@Override
	public Tax calculateTax(int employeeId, String taxYear) 
			throws TaxCalculationException, DatabaseConnectionException, ClassNotFoundException {
		try (Connection conn = ConnectionHelper.getConnection()) {
			// Get annual taxable income
			double taxableIncome = calculateTaxableIncome(conn, employeeId, taxYear);
			if (taxableIncome <= 0) {
				throw new TaxCalculationException("No taxable income found for the specified year");
			}

			// Calculate tax amount based on slabs
			double taxAmount = calculateTaxAmount(taxableIncome);

			// Calculate tax percentage (rounded to 2 decimal places)
			double taxPercentage = Math.round((taxAmount / taxableIncome) * 10000.0) / 100.0;

			// Create and save tax record
			Tax tax = new Tax();
			tax.setEmployeeId(employeeId);
			tax.setTaxYear(taxYear);
			tax.setTaxableIncome(taxableIncome);
			tax.setTaxAmount(taxAmount);
			tax.setTaxPercentage(taxPercentage);

			saveTaxRecord(conn, tax);
			return tax;

		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error calculating tax: " + e.getMessage());
		}
	}

	@Override
	public Tax getTaxById(int taxId) throws TaxCalculationException, DatabaseConnectionException, ClassNotFoundException {
		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM tax WHERE tax_id = ?")) {

			ps.setInt(1, taxId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return extractTaxFromResultSet(rs);
			} else {
				throw new TaxCalculationException("Tax record not found with ID: " + taxId);
			}

		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error retrieving tax record: " + e.getMessage());
		}
	}

	@Override
	public List<Tax> getTaxesForEmployee(int employeeId) 
			throws TaxCalculationException, DatabaseConnectionException, ClassNotFoundException {
		List<Tax> taxes = new ArrayList<>();

		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM tax WHERE employee_id = ?")) {

			ps.setInt(1, employeeId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				taxes.add(extractTaxFromResultSet(rs));
			}

			return taxes;

		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error retrieving tax records: " + e.getMessage());
		}
	}

	@Override
	public List<Tax> getTaxesForYear(String taxYear) 
			throws TaxCalculationException, DatabaseConnectionException, ClassNotFoundException {
		List<Tax> taxes = new ArrayList<>();

		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM tax WHERE tax_year = ?")) {

			ps.setString(1, taxYear);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				taxes.add(extractTaxFromResultSet(rs));
			}

			return taxes;

		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error retrieving tax records: " + e.getMessage());
		}
	}

	private boolean employeeExists(Connection conn, int employeeId) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM employees WHERE employee_id = ?")) {
			ps.setInt(1, employeeId);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}

	private double calculateTaxableIncome(Connection conn, int employeeId, String taxYear) throws SQLException {
		String sql = "SELECT salary FROM employees WHERE employee_id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, employeeId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getDouble("salary");
			}
			return 0.0;
		}
	}

	private double calculateTaxAmount(double taxableIncome) {
		double taxAmount = 0.0;

		// Tax slabs calculation
		if (taxableIncome <= 250000) {
			taxAmount = 0.0;
		} else if (taxableIncome <= 500000) {
			taxAmount = (taxableIncome - 250000) * 0.05;
		} else if (taxableIncome <= 1000000) {
			// First 250000: 0
			// 250000-500000: (500000-250000)*0.05 = 12500
			// 500000-taxableIncome: (taxableIncome-500000)*0.20
			taxAmount = 12500 + ((taxableIncome - 500000) * 0.20);
		} else {
			// First 250000: 0
			// 250000-500000: 12500
			// 500000-1000000: 100000
			// Above 1000000: (taxableIncome-1000000)*0.30
			taxAmount = 112500 + ((taxableIncome - 1000000) * 0.30);
		}

		return Math.round(taxAmount * 100.0) / 100.0; // Round to 2 decimal places
	}

	private void saveTaxRecord(Connection conn, Tax tax) throws SQLException {
		String sql = "INSERT INTO tax (employee_id, tax_year, taxable_income, tax_amount, tax_percentage) " +
				"VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, tax.getEmployeeId());
			ps.setString(2, tax.getTaxYear());
			ps.setDouble(3, tax.getTaxableIncome());
			ps.setDouble(4, tax.getTaxAmount());
			ps.setDouble(5, Math.round(tax.getTaxPercentage() * 100.0) / 100.0); // Round percentage

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				tax.setTaxId(rs.getInt(1));
			}
		}
	}

	private Tax extractTaxFromResultSet(ResultSet rs) throws SQLException {
		Tax tax = new Tax();
		tax.setTaxId(rs.getInt("tax_id"));
		tax.setEmployeeId(rs.getInt("employee_id"));
		tax.setTaxYear(rs.getString("tax_year"));
		tax.setTaxableIncome(rs.getDouble("taxable_income"));
		tax.setTaxAmount(rs.getDouble("tax_amount"));
		tax.setTaxPercentage(rs.getDouble("tax_percentage"));
		return tax;
	}
}

