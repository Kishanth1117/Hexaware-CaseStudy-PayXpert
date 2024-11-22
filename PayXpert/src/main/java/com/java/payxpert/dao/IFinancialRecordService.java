package com.java.payxpert.dao;

import java.util.List;
import com.java.payxpert.model.FinancialRecord;
import com.java.payxpert.exception.*;

public interface IFinancialRecordService {
	boolean addFinancialRecord(FinancialRecord record) 
			throws FinancialRecordException, DatabaseConnectionException, ClassNotFoundException;

	FinancialRecord getFinancialRecordById(int recordId) 
			throws FinancialRecordException, DatabaseConnectionException, ClassNotFoundException;

	List<FinancialRecord> getFinancialRecordsForEmployee(int employeeId) 
			throws FinancialRecordException, DatabaseConnectionException, ClassNotFoundException;

	List<FinancialRecord> getFinancialRecordsForDate(String recordDate) 
			throws FinancialRecordException, DatabaseConnectionException, ClassNotFoundException;
}
