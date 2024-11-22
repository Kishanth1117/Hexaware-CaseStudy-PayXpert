package com.java.payxpert.dao;

import java.util.List;
import com.java.payxpert.model.PayRoll;
import com.java.payxpert.exception.*;

public interface IPayrollService {
	PayRoll generatePayroll(int employeeId, String startDate, String endDate) 
			throws PayrollGenerationException, DatabaseConnectionException, ClassNotFoundException;

	PayRoll getPayrollById(int payrollId) 
			throws PayrollGenerationException, DatabaseConnectionException, ClassNotFoundException;

	List<PayRoll> getPayrollsForEmployee(int employeeId) 
			throws PayrollGenerationException, DatabaseConnectionException, ClassNotFoundException;

	List<PayRoll> getPayrollsForPeriod(String startDate, String endDate) 
			throws PayrollGenerationException, DatabaseConnectionException, ClassNotFoundException;
}

