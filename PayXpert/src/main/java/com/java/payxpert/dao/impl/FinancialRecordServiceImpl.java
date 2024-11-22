package com.java.payxpert.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.java.payxpert.dao.IFinancialRecordService;
import com.java.payxpert.exception.*;
import com.java.payxpert.model.FinancialRecord;
import com.java.payxpert.util.ConnectionHelper;

public class FinancialRecordServiceImpl implements IFinancialRecordService {

	@Override
	public boolean addFinancialRecord(FinancialRecord record) 
			throws FinancialRecordException, DatabaseConnectionException {
		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO financial_records (employee_id, record_date, description, amount, record_type) " +
						"VALUES (?, ?, ?, ?, ?)")) {

			validateFinancialRecord(record);

			stmt.setInt(1, record.getEmployeeId());
			stmt.setDate(2, new java.sql.Date(record.getRecordDate().getTime()));
			stmt.setString(3, record.getDescription());
			stmt.setDouble(4, record.getAmount());
			stmt.setString(5, record.getRecordType());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException | ClassNotFoundException e) {
			throw new DatabaseConnectionException("Error adding financial record: " + e.getMessage());
		}
	}

	@Override
	public FinancialRecord getFinancialRecordById(int recordId) 
			throws FinancialRecordException, DatabaseConnectionException {
		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM financial_records WHERE record_id = ?")) {

			stmt.setInt(1, recordId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return mapResultSetToFinancialRecord(rs);
			}
			throw new FinancialRecordException("Financial record not found with ID: " + recordId);

		} catch (SQLException | ClassNotFoundException e) {
			throw new DatabaseConnectionException("Error retrieving financial record: " + e.getMessage());
		}
	}

	@Override
	public List<FinancialRecord> getFinancialRecordsForEmployee(int employeeId) 
			throws FinancialRecordException, DatabaseConnectionException {
		List<FinancialRecord> records = new ArrayList<>();

		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM financial_records WHERE employee_id = ?")) {

			stmt.setInt(1, employeeId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				records.add(mapResultSetToFinancialRecord(rs));
			}
			return records;

		} catch (SQLException | ClassNotFoundException e) {
			throw new DatabaseConnectionException("Error retrieving financial records: " + e.getMessage());
		}
	}

	@Override
	public List<FinancialRecord> getFinancialRecordsForDate(String recordDate) 
			throws FinancialRecordException, DatabaseConnectionException {
		List<FinancialRecord> records = new ArrayList<>();

		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM financial_records WHERE DATE(record_date) = ?")) {

			stmt.setDate(1, java.sql.Date.valueOf(recordDate));
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				records.add(mapResultSetToFinancialRecord(rs));
			}
			return records;

		} catch (SQLException | ClassNotFoundException e) {
			throw new DatabaseConnectionException("Error retrieving financial records: " + e.getMessage());
		}
	}

	private FinancialRecord mapResultSetToFinancialRecord(ResultSet rs) throws SQLException {
		FinancialRecord record = new FinancialRecord();
		record.setRecordId(rs.getInt("record_id"));
		record.setEmployeeId(rs.getInt("employee_id"));
		record.setRecordDate(rs.getDate("record_date"));
		record.setDescription(rs.getString("description"));
		record.setAmount(rs.getDouble("amount"));
		record.setRecordType(rs.getString("record_type"));
		return record;
	}

	private void validateFinancialRecord(FinancialRecord record) throws FinancialRecordException {
		if (record.getEmployeeId() <= 0) {
			throw new FinancialRecordException("Invalid employee ID");
		}
		if (record.getRecordDate() == null) {
			throw new FinancialRecordException("Record date cannot be null");
		}
		if (record.getDescription() == null || record.getDescription().trim().isEmpty()) {
			throw new FinancialRecordException("Description cannot be empty");
		}
		if (record.getAmount() <= 0) {
			throw new FinancialRecordException("Amount must be greater than 0");
		}
		if (record.getRecordType() == null || record.getRecordType().trim().isEmpty()) {
			throw new FinancialRecordException("Record type cannot be empty");
		}
	}
}
