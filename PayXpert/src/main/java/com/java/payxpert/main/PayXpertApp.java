package com.java.payxpert.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.java.payxpert.dao.*;
import com.java.payxpert.dao.impl.*;
import com.java.payxpert.model.*;

public class PayXpertApp {
    private static Scanner scanner = new Scanner(System.in);
    private static IEmployeeService employeeService = new EmployeeServiceImpl();
    private static IPayrollService payrollService = new PayrollServiceImpl();
    private static ITaxService taxService = new TaxServiceImpl();
    private static IFinancialRecordService financialRecordService = new FinancialRecordServiceImpl();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            try {
                displayMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        handleEmployeeOperations();
                        break;
                    case 2:
                        handlePayrollOperations();
                        break;
                    case 3:
                        handleTaxOperations();
                        break;
                    case 4:
                        handleFinancialRecordOperations();
                        break;
                    case 5:
                        System.out.println("Thank you for using PayXpert. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear the input buffer
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== PayXpert System ===");
        System.out.println("1. Employee Management");
        System.out.println("2. Payroll Management");
        System.out.println("3. Tax Management");
        System.out.println("4. Financial Records");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleEmployeeOperations() {
        // Employee management menu and operations
        System.out.println("\n=== Employee Management ===");
        System.out.println("1. Add Employee");
        System.out.println("2. Update Employee");
        System.out.println("3. Remove Employee");
        System.out.println("4. View Employee");
        System.out.println("5. View All Employees");
        System.out.print("Enter your choice: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    updateEmployee();
                    break;
                case 3:
                    removeEmployee();
                    break;
                case 4:
                    viewEmployee();
                    break;
                case 5:
                    viewAllEmployees();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Similar methods for handlePayrollOperations(), handleTaxOperations(), 
    // and handleFinancialRecordOperations()
    private static void handlePayrollOperations() {
        System.out.println("\n=== Payroll Management ===");
        System.out.println("1. Generate Payroll");
        System.out.println("2. View Payroll by ID");
        System.out.println("3. View Employee Payrolls");
        System.out.println("4. View Payrolls for Period");
        System.out.print("Enter your choice: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    generatePayroll();
                    break;
                case 2:
                    viewPayrollById();
                    break;
                case 3:
                    viewEmployeePayrolls();
                    break;
                case 4:
                    viewPayrollsForPeriod();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleTaxOperations() {
        System.out.println("\n=== Tax Management ===");
        System.out.println("1. Calculate Tax");
        System.out.println("2. View Tax by ID");
        System.out.println("3. View Employee Taxes");
        System.out.println("4. View Taxes for Year");
        System.out.print("Enter your choice: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    calculateTax();
                    break;
                case 2:
                    viewTaxById();
                    break;
                case 3:
                    viewEmployeeTaxes();
                    break;
                case 4:
                    viewTaxesForYear();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleFinancialRecordOperations() {
        System.out.println("\n=== Financial Records ===");
        System.out.println("1. Add Financial Record");
        System.out.println("2. View Record by ID");
        System.out.println("3. View Employee Records");
        System.out.println("4. View Records for Date");
        System.out.print("Enter your choice: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addFinancialRecord();
                    break;
                case 2:
                    viewFinancialRecordById();
                    break;
                case 3:
                    viewEmployeeFinancialRecords();
                    break;
                case 4:
                    viewFinancialRecordsForDate();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void addEmployee() throws ParseException {
        System.out.println("\n=== Add New Employee ===");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        
        System.out.print("Enter Hire Date (yyyy-MM-dd): ");
        String hireDateStr = scanner.nextLine();
        java.util.Date hireDate = dateFormat.parse(hireDateStr);
        
        System.out.print("Enter Job Title: ");
        String jobTitle = scanner.nextLine();
        
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        
        System.out.print("Enter Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Enter Gender (MALE/FEMALE/OTHER): ");
        String genderStr = scanner.nextLine();
        Gender gender = Gender.valueOf(genderStr.toUpperCase());
        
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhoneNumber(phoneNumber);
        employee.setHireDate(hireDate);
        employee.setJobTitle(jobTitle);
        employee.setDepartment(department);
        employee.setSalary(salary);
        employee.setGender(gender);
        
        try {
            boolean success = employeeService.addEmployee(employee);
            if (success) {
                System.out.println("Employee added successfully!");
            } else {
                System.out.println("Failed to add employee.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Additional helper methods for other operations...
 // Employee Operations
    private static void updateEmployee() throws ParseException {
        System.out.println("\n=== Update Employee ===");
        System.out.print("Enter Employee ID to update: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            
            // Get updated information
            System.out.print("Enter new First Name (press Enter to keep current): ");
            String input = scanner.nextLine();
            if (!input.isEmpty()) {
                employee.setFirstName(input);
            }
            
            // Similar prompts for other fields...
            
            boolean success = employeeService.updateEmployee(employee);
            if (success) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("Failed to update employee.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void removeEmployee() {
        System.out.println("\n=== Remove Employee ===");
        System.out.print("Enter Employee ID to remove: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        try {
            boolean success = employeeService.removeEmployee(employeeId);
            if (success) {
                System.out.println("Employee removed successfully!");
            } else {
                System.out.println("Failed to remove employee.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewEmployee() {
        System.out.println("\n=== View Employee ===");
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            System.out.println(employee);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllEmployees() {
        System.out.println("\n=== All Employees ===");
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
 // Payroll Operations
    private static void generatePayroll() {
        System.out.println("\n=== Generate Payroll ===");
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Start Date (yyyy-MM-dd): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter End Date (yyyy-MM-dd): ");
        String endDate = scanner.nextLine();

        try {
            PayRoll payroll = payrollService.generatePayroll(employeeId, startDate, endDate);
            System.out.println("Payroll generated successfully:");
            System.out.println(payroll);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewPayrollById() {
        System.out.println("\n=== View Payroll ===");
        System.out.print("Enter Payroll ID: ");
        int payrollId = scanner.nextInt();
        scanner.nextLine();

        try {
            PayRoll payroll = payrollService.getPayrollById(payrollId);
            System.out.println(payroll);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewEmployeePayrolls() {
        System.out.println("\n=== View Employee Payrolls ===");
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        try {
            List<PayRoll> payrolls = payrollService.getPayrollsForEmployee(employeeId);
            for (PayRoll payroll : payrolls) {
                System.out.println(payroll);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewPayrollsForPeriod() {
        System.out.println("\n=== View Payrolls for Period ===");
        System.out.print("Enter Start Date (yyyy-MM-dd): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter End Date (yyyy-MM-dd): ");
        String endDate = scanner.nextLine();

        try {
            List<PayRoll> payrolls = payrollService.getPayrollsForPeriod(startDate, endDate);
            for (PayRoll payroll : payrolls) {
                System.out.println(payroll);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Tax Operations
    private static void calculateTax() {
        System.out.println("\n=== Calculate Tax ===");
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Tax Year (YYYY): ");
        String taxYear = scanner.nextLine();

        try {
            Tax tax = taxService.calculateTax(employeeId, taxYear);
            System.out.println("Tax calculated successfully:");
            System.out.println(tax);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewTaxById() {
        System.out.println("\n=== View Tax Record ===");
        System.out.print("Enter Tax ID: ");
        int taxId = scanner.nextInt();
        scanner.nextLine();

        try {
            Tax tax = taxService.getTaxById(taxId);
            System.out.println(tax);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewEmployeeTaxes() {
        System.out.println("\n=== View Employee Tax Records ===");
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        try {
            List<Tax> taxes = taxService.getTaxesForEmployee(employeeId);
            for (Tax tax : taxes) {
                System.out.println(tax);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewTaxesForYear() {
        System.out.println("\n=== View Tax Records for Year ===");
        System.out.print("Enter Tax Year (YYYY): ");
        String taxYear = scanner.nextLine();

        try {
            List<Tax> taxes = taxService.getTaxesForYear(taxYear);
            for (Tax tax : taxes) {
                System.out.println(tax);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Financial Record Operations
    private static void addFinancialRecord() throws ParseException {
        System.out.println("\n=== Add Financial Record ===");
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Record Date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        java.util.Date recordDate = dateFormat.parse(dateStr);

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter Record Type: ");
        String recordType = scanner.nextLine();

        FinancialRecord record = new FinancialRecord();
        record.setEmployeeId(employeeId);
        record.setRecordDate(recordDate);
        record.setDescription(description);
        record.setAmount(amount);
        record.setRecordType(recordType);

        try {
            boolean success = financialRecordService.addFinancialRecord(record);
            if (success) {
                System.out.println("Financial record added successfully!");
            } else {
                System.out.println("Failed to add financial record.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewFinancialRecordById() {
        System.out.println("\n=== View Financial Record ===");
        System.out.print("Enter Record ID: ");
        int recordId = scanner.nextInt();
        scanner.nextLine();

        try {
            FinancialRecord record = financialRecordService.getFinancialRecordById(recordId);
            System.out.println(record);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewEmployeeFinancialRecords() {
        System.out.println("\n=== View Employee Financial Records ===");
        System.out.print("Enter Employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        try {
            List<FinancialRecord> records = financialRecordService.getFinancialRecordsForEmployee(employeeId);
            for (FinancialRecord record : records) {
                System.out.println(record);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewFinancialRecordsForDate() {
        System.out.println("\n=== View Financial Records for Date ===");
        System.out.print("Enter Date (yyyy-MM-dd): ");
        String date = scanner.nextLine();

        try {
            List<FinancialRecord> records = financialRecordService.getFinancialRecordsForDate(date);
            for (FinancialRecord record : records) {
                System.out.println(record);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayTax(Tax tax) {
        System.out.println("\nTax ID: " + tax.getTaxId());
        System.out.println("Employee ID: " + tax.getEmployeeId());
        System.out.println("Tax Year: " + tax.getTaxYear());
        System.out.println("Taxable Income: $" + tax.getTaxableIncome());
        System.out.println("Tax Amount: $" + tax.getTaxAmount());
        System.out.println("Tax Percentage: " + tax.getTaxPercentage() + "%");
    }
}
