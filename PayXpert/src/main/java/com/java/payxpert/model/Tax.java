package com.java.payxpert.model;

public class Tax 
{

	private int taxId;
	private int employeeId;
	private String taxYear;
	private double taxableIncome;
	private double taxAmount;
	private double taxPercentage;
	public int getTaxId() {
		return taxId;
	}
	public void setTaxId(int taxId) {
		this.taxId = taxId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getTaxYear() {
		return taxYear;
	}
	public void setTaxYear(String taxYear) {
		this.taxYear = taxYear;
	}
	public double getTaxableIncome() {
		return taxableIncome;
	}
	public void setTaxableIncome(double taxableIncome) {
		this.taxableIncome = taxableIncome;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public double getTaxPercentage() {
		return taxPercentage;
	}
	public void setTaxPercentage(double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	@Override
	public String toString() {
		return "Tax [taxId=" + taxId + ", employeeId=" + employeeId + ", taxYear=" + taxYear + ", taxableIncome="
				+ taxableIncome + ", taxAmount=" + taxAmount + ", taxPercentage=" + taxPercentage + "]";
	}
	public Tax(int taxId, int employeeId, String taxYear, double taxableIncome, double taxAmount,
			double taxPercentage) {
		super();
		this.taxId = taxId;
		this.employeeId = employeeId;
		this.taxYear = taxYear;
		this.taxableIncome = taxableIncome;
		this.taxAmount = taxAmount;
		this.taxPercentage = taxPercentage;
	}
	public Tax() {
		super();
		// TODO Auto-generated constructor stub
	}

}
