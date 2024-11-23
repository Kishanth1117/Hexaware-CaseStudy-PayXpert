package com.java.payxpert.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.java.payxpert.dao.IPayrollService;
import com.java.payxpert.exception.*;
import com.java.payxpert.model.PayRoll;
import com.java.payxpert.util.ConnectionHelper;

public class PayrollServiceImpl implements IPayrollService {

	@Override
	public PayRoll generatePayroll(int employeeId, String startDate, String endDate) 
			throws PayrollGenerationException, DatabaseConnectionException, ClassNotFoundException {
		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO payroll (employee_id, basic_salary, overtime_pay, deductions, net_salary, " +
								"pay_period_start, pay_period_end, payment_date) " +
								"VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {


			double basicSalary = getEmployeeBasicSalary(conn, employeeId) / 12;


			double overtimePay = calculateOvertimePay(employeeId, startDate, endDate);
			double deductions = calculateDeductions(employeeId);


			double netSalary = basicSalary + overtimePay - deductions;


			stmt.setInt(1, employeeId);
			stmt.setDouble(2, basicSalary);
			stmt.setDouble(3, overtimePay);
			stmt.setDouble(4, deductions);
			stmt.setDouble(5, netSalary);
			stmt.setDate(6, java.sql.Date.valueOf(startDate));
			stmt.setDate(7, java.sql.Date.valueOf(endDate));
			stmt.setDate(8, new java.sql.Date(System.currentTimeMillis()));

			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return getPayrollById(rs.getInt(1));
			} else {
				throw new PayrollGenerationException("Failed to generate payroll");
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error generating payroll: " + e.getMessage());
		}
	}

	@Override
	public PayRoll getPayrollById(int payrollId) 
			throws PayrollGenerationException, DatabaseConnectionException, ClassNotFoundException {
		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM payroll WHERE payroll_id = ?")) {

			stmt.setInt(1, payrollId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				PayRoll payroll = new PayRoll();
				payroll.setPayrollId(rs.getInt("payroll_id"));
				payroll.setEmployeeId(rs.getInt("employee_id"));
				payroll.setBasicSalary(rs.getDouble("basic_salary"));
				payroll.setOvertime(rs.getDouble("overtime_pay"));
				payroll.setDeductions(rs.getDouble("deductions"));
				payroll.setNetSalary(rs.getDouble("net_salary"));
				payroll.setPayPeriodStart(rs.getDate("pay_period_start"));
				payroll.setPayPeriodEnd(rs.getDate("pay_period_end"));
				payroll.setPaymentDate(rs.getDate("payment_date"));
				return payroll;
			} else {
				throw new PayrollGenerationException("Payroll not found with ID: " + payrollId);
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error retrieving payroll: " + e.getMessage());
		}
	}

	@Override
	public List<PayRoll> getPayrollsForEmployee(int employeeId) 
			throws PayrollGenerationException, DatabaseConnectionException, ClassNotFoundException {
		List<PayRoll> payrolls = new ArrayList<>();

		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM payroll WHERE employee_id = ?")) {

			stmt.setInt(1, employeeId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				PayRoll payroll = new PayRoll();
				payroll.setPayrollId(rs.getInt("payroll_id"));
				payroll.setEmployeeId(rs.getInt("employee_id"));
				payroll.setBasicSalary(rs.getDouble("basic_salary"));
				payroll.setOvertime(rs.getDouble("overtime_pay"));
				payroll.setDeductions(rs.getDouble("deductions"));
				payroll.setNetSalary(rs.getDouble("net_salary"));
				payroll.setPayPeriodStart(rs.getDate("pay_period_start"));
				payroll.setPayPeriodEnd(rs.getDate("pay_period_end"));
				payroll.setPaymentDate(rs.getDate("payment_date"));
				payrolls.add(payroll);
			}
			return payrolls;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error retrieving payrolls: " + e.getMessage());
		}
	}

	@Override
	public List<PayRoll> getPayrollsForPeriod(String startDate, String endDate) 
			throws PayrollGenerationException, DatabaseConnectionException, ClassNotFoundException {
		List<PayRoll> payrolls = new ArrayList<>();

		try (Connection conn = ConnectionHelper.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM payroll WHERE pay_period_start >= ? AND pay_period_end <= ?")) {

			stmt.setDate(1, java.sql.Date.valueOf(startDate));
			stmt.setDate(2, java.sql.Date.valueOf(endDate));
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				PayRoll payroll = new PayRoll();
				payroll.setPayrollId(rs.getInt("payroll_id"));
				payroll.setEmployeeId(rs.getInt("employee_id"));
				payroll.setBasicSalary(rs.getDouble("basic_salary"));
				payroll.setOvertime(rs.getDouble("overtime_pay"));
				payroll.setDeductions(rs.getDouble("deductions"));
				payroll.setNetSalary(rs.getDouble("net_salary"));
				payroll.setPayPeriodStart(rs.getDate("pay_period_start"));
				payroll.setPayPeriodEnd(rs.getDate("pay_period_end"));
				payroll.setPaymentDate(rs.getDate("payment_date"));
				payrolls.add(payroll);
			}
			return payrolls;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Error retrieving payrolls: " + e.getMessage());
		}
	}

	private double getEmployeeBasicSalary(Connection conn, int employeeId) throws SQLException {
		try (PreparedStatement stmt = conn.prepareStatement(
				"SELECT salary FROM employees WHERE employee_id = ?")) {
			stmt.setInt(1, employeeId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getDouble("salary");
			}
			return 0.0;
		}
	}

	private double calculateOvertimePay(int employeeId, String startDate, String endDate) throws SQLException, ClassNotFoundException {
		try (Connection conn = ConnectionHelper.getConnection()) {

			double monthlySalary = getEmployeeBasicSalary(conn, employeeId) / 12;

			double hourlyRate = monthlySalary / (22 * 8);

			String sql = "SELECT COALESCE(SUM(overtime_hours), 0) as total_overtime " +
					"FROM attendance " +
					"WHERE employee_id = ? " +
					"AND attendance_date BETWEEN ? AND ?";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, employeeId);
				ps.setDate(2, java.sql.Date.valueOf(startDate));
				ps.setDate(3, java.sql.Date.valueOf(endDate));

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					double overtimeHours = rs.getDouble("total_overtime");
					return Math.round(overtimeHours * hourlyRate * 1.5 * 100.0) / 100.0; 
				}
			}
		}
		return 0.0;
	}

	private double calculateDeductions(int employeeId) {
		try (Connection conn = ConnectionHelper.getConnection()) {
			double monthlySalary = getEmployeeBasicSalary(conn, employeeId) / 12;
			double totalDeductions = 0.0;


			totalDeductions += monthlySalary * 0.10;


			String sql = "SELECT deduction_type, amount " +
					"FROM employee_deductions " +
					"WHERE employee_id = ? " +
					"AND is_active = true";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, employeeId);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					String type = rs.getString("deduction_type");
					double amount = rs.getDouble("amount");

					if (type.equals("PENSION")) {
						totalDeductions += monthlySalary * (amount / 100.0);
					} else {
						totalDeductions += amount;
					}
				}
			}
			return totalDeductions;
		} catch (Exception e) {
			System.err.println("Error calculating deductions: " + e.getMessage());
		}
		return 0.0;
	}
}