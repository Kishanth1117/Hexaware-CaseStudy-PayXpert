package com.java.payxpert.model;

import java.util.Date;

public class PayRoll 
{

	private int payrollId;
	private int employeeId;
	private Date payPeriodStart;
	private Date payPeriodEnd;
	private double basicSalary;
	private double overtime;
	private double deductions;
	private double netSalary;
	private Date paymentDate;

	public int getPayrollId() {
		return payrollId;
	}
	public void setPayrollId(int payrollId) {
		this.payrollId = payrollId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public Date getPayPeriodStart() {
		return payPeriodStart;
	}
	public void setPayPeriodStart(Date payPeriodStart) {
		this.payPeriodStart = payPeriodStart;
	}
	public Date getPayPeriodEnd() {
		return payPeriodEnd;
	}
	public void setPayPeriodEnd(Date payPeriodEnd) {
		this.payPeriodEnd = payPeriodEnd;
	}
	public double getBasicSalary() {
		return basicSalary;
	}
	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}
	public double getOvertime() {
		return overtime;
	}
	public void setOvertime(double overtime) {
		this.overtime = overtime;
	}
	public double getDeductions() {
		return deductions;
	}
	public void setDeductions(double deductions) {
		this.deductions = deductions;
	}
	public double getNetSalary() {
		return netSalary;
	}
	public void setNetSalary(double netSalary) {
		this.netSalary = netSalary;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Override
	public String toString() {
		return "PayRoll [payrollId=" + payrollId + ", employeeId=" + employeeId + ", payPeriodStart=" + payPeriodStart
				+ ", payPeriodEnd=" + payPeriodEnd + ", basicSalary=" + basicSalary + ", overtime=" + overtime
				+ ", deductions=" + deductions + ", netSalary=" + netSalary + ", paymentDate=" + paymentDate + "]";
	}

	public PayRoll(int payrollId, int employeeId, Date payPeriodStart, Date payPeriodEnd, double basicSalary,
			double overtime, double deductions, double netSalary, Date paymentDate) {
		super();
		this.payrollId = payrollId;
		this.employeeId = employeeId;
		this.payPeriodStart = payPeriodStart;
		this.payPeriodEnd = payPeriodEnd;
		this.basicSalary = basicSalary;
		this.overtime = overtime;
		this.deductions = deductions;
		this.netSalary = netSalary;
		this.paymentDate = paymentDate;
	}

	public PayRoll() {
		super();
		// TODO Auto-generated constructor stub
	}	

}
