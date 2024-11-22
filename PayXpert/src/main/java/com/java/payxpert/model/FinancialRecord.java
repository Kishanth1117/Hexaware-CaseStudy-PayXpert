package com.java.payxpert.model;

import java.util.Date;

public class FinancialRecord 
{

	private int recordId;
	private int employeeId;
	private Date recordDate;
	private String description;
	private double amount;
	private String recordType;

	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	@Override
	public String toString() {
		return "FinancialRecord [recordId=" + recordId + ", employeeId=" + employeeId + ", recordDate=" + recordDate
				+ ", description=" + description + ", amount=" + amount + ", recordType=" + recordType + "]";
	}

	public FinancialRecord(int recordId, int employeeId, Date recordDate, String description, double amount,
			String recordType) {
		super();
		this.recordId = recordId;
		this.employeeId = employeeId;
		this.recordDate = recordDate;
		this.description = description;
		this.amount = amount;
		this.recordType = recordType;
	}
	public FinancialRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

}


