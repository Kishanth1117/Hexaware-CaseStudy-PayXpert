package com.java.payxpert.dao;

import java.util.List;
import com.java.payxpert.model.Employee;
import com.java.payxpert.exception.*;

public interface IEmployeeService {
	Employee getEmployeeById(int employeeId) 
			throws EmployeeNotFoundException, DatabaseConnectionException, ClassNotFoundException;

	List<Employee> getAllEmployees() 
			throws DatabaseConnectionException, ClassNotFoundException;

	boolean addEmployee(Employee employee) 
			throws InvalidInputException, DatabaseConnectionException, ClassNotFoundException;

	boolean updateEmployee(Employee employee) 
			throws InvalidInputException, DatabaseConnectionException, EmployeeNotFoundException, ClassNotFoundException;

	boolean removeEmployee(int employeeId) 
			throws DatabaseConnectionException, EmployeeNotFoundException, ClassNotFoundException;
}

