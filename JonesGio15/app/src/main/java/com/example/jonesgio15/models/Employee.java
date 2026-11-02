// Gio Jones
// AD2 - C202606
// Employee.java

package com.example.jonesgio15.models;
import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String firstName;
    private String lastName;
    private int employeeNumber;
    private String hireDate;
    private String employmentStatus;

    // Constructor for creating a new employee object
    public Employee(String firstName, String lastName, int employeeNumber, String hireDate, String employmentStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
        this.hireDate = hireDate;
        this.employmentStatus = employmentStatus;
    }

    // Constructor for loading an existing employee from the database
    public Employee(long id, String firstName, String lastName, int employeeNumber, String hireDate, String employmentStatus) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber;
        this.hireDate = hireDate;
        this.employmentStatus = employmentStatus;
    }

    // Getters & Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(int employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }


    @Override
    public String toString() {
        return firstName + " " + lastName + " (#" + employeeNumber + ")";
    }
}