package com.hcl.employee;

/**
 * Employee Model Class
 * Represents an employee in the directory.
 * Implements Comparable to define natural ordering sorted by employee id.
 */
public class Employee implements Comparable<Employee> {

    private final int id;
    private final String name;
    private final String department;
    private final double salary;

    public Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    /**
     * Natural Ordering: Sorted ascending by employee ID.
     */
    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return String.format("Employee[ID=%-4d | Name=%-14s | Dept=%-12s | Salary=$%,10.2f]",
                id, name, department, salary);
    }
}
