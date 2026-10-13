package com.hcl.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * EmployeeDirectory Class
 *
 * Implements:
 * 1. HashMap lookup by employee ID.
 * 2. Department frequency count using Map.merge().
 * 3. Alphabetically sorted department report using TreeMap.
 * 4. Natural ordering sorting (Comparable by id).
 * 5. Top-5 salary ranking using a chained Comparator.
 */
public class EmployeeDirectory {

    private final List<Employee> employeeList = new ArrayList<>();
    private final Map<Integer, Employee> employeeMap = new HashMap<>();

    public void addEmployee(Employee emp) {
        employeeList.add(emp);
        employeeMap.put(emp.getId(), emp);
    }

    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(employeeList);
    }

    // =========================================================================
    // 1. HashMap Lookup (O(1) average time complexity)
    // =========================================================================
    public Employee lookupById(int id) {
        return employeeMap.get(id);
    }

    // =========================================================================
    // 2. Frequency Count with Map.merge()
    // =========================================================================
    /**
     * Calculates employee headcount per department using Map.merge().
     * If key is absent, sets value to 1.
     * If key is present, applies Integer::sum (oldVal + 1).
     */
    public Map<String, Integer> getDepartmentHeadcounts() {
        Map<String, Integer> counts = new HashMap<>();
        for (Employee emp : employeeList) {
            counts.merge(emp.getDepartment(), 1, Integer::sum);
        }
        return counts;
    }

    // =========================================================================
    // 3. TreeMap Department Report (Sorted Alphabetically by Department Name)
    // =========================================================================
    /**
     * Groups employees under their respective department using a TreeMap.
     * Guarantees keys (departments) are sorted in natural alphabetical order.
     */
    public Map<String, List<Employee>> getDepartmentReport() {
        Map<String, List<Employee>> report = new TreeMap<>();
        for (Employee emp : employeeList) {
            report.computeIfAbsent(emp.getDepartment(), k -> new ArrayList<>()).add(emp);
        }
        return report;
    }

    // =========================================================================
    // 4. Natural Ordering (Comparable by id)
    // =========================================================================
    /**
     * Returns a new list sorted according to Employee's compareTo implementation (id ascending).
     */
    public List<Employee> getEmployeesSortedById() {
        List<Employee> sorted = new ArrayList<>(employeeList);
        Collections.sort(sorted); // Relies on Employee.compareTo
        return sorted;
    }

    // =========================================================================
    // 5. Top-5 Salary Ranking via Chained Comparator
    // =========================================================================
    /**
     * Sorts employees by:
     * 1. Salary (descending)
     * 2. Name (ascending tie-break)
     * 3. ID (ascending tie-break)
     * and extracts the top 5 highest-earning employees.
     */
    public List<Employee> getTop5BySalary() {
        Comparator<Employee> topSalaryComparator = Comparator
                .comparingDouble(Employee::getSalary).reversed()
                .thenComparing(Employee::getName)
                .thenComparingInt(Employee::getId);

        List<Employee> sorted = new ArrayList<>(employeeList);
        sorted.sort(topSalaryComparator);

        int limit = Math.min(5, sorted.size());
        return sorted.subList(0, limit);
    }
}
