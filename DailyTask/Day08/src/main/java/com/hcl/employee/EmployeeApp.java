package com.hcl.employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmployeeApp - Main Demonstration Runner for Day 8
 *
 * Demonstrates:
 * 1. HashMap lookup by ID (O(1)).
 * 2. Frequency counting of department headcounts using Map.merge().
 * 3. Sorted department report using TreeMap.
 * 4. Sorting employees by ID using Comparable.
 * 5. Top-5 salary ranking using a chained Comparator.
 * 6. Missing hashCode() bug demonstration and fix.
 */
public class EmployeeApp {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("          DAY 8: EMPLOYEE DIRECTORY & MAPS DEMO          ");
        System.out.println("=========================================================");

        EmployeeDirectory directory = new EmployeeDirectory();

        // Populate sample employees
        directory.addEmployee(new Employee(105, "Sarah Connor",  "Engineering", 125000.00));
        directory.addEmployee(new Employee(101, "John Doe",      "HR",          65000.00));
        directory.addEmployee(new Employee(108, "Bruce Wayne",   "Executive",   250000.00));
        directory.addEmployee(new Employee(103, "Alice Smith",   "Engineering", 110000.00));
        directory.addEmployee(new Employee(102, "Clark Kent",    "Marketing",   78000.00));
        directory.addEmployee(new Employee(107, "Diana Prince",  "Executive",   210000.00));
        directory.addEmployee(new Employee(104, "Bob Johnson",   "Finance",     92000.00));
        directory.addEmployee(new Employee(106, "Tony Stark",    "Engineering", 250000.00)); // Same salary as Bruce Wayne

        // -------------------------------------------------------------
        // SECTION 1: HashMap Lookup
        // -------------------------------------------------------------
        System.out.println("\n>>> SECTION 1: HashMap FAST LOOKUP (O(1))");
        int searchId = 104;
        Employee found = directory.lookupById(searchId);
        System.out.printf("Searching for Employee ID %d: %s%n", searchId, found != null ? found : "NOT FOUND");

        int missingId = 999;
        Employee notFound = directory.lookupById(missingId);
        System.out.printf("Searching for Employee ID %d: %s%n", missingId, notFound != null ? notFound : "NOT FOUND");

        // -------------------------------------------------------------
        // SECTION 2: Frequency Count using Map.merge()
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 2: DEPARTMENT HEADCOUNT VIA Map.merge()");
        System.out.println("---------------------------------------------------------");
        Map<String, Integer> headcounts = directory.getDepartmentHeadcounts();
        headcounts.forEach((dept, count) -> 
            System.out.printf("   * %-15s : %d employee(s)%n", dept, count));

        // -------------------------------------------------------------
        // SECTION 3: TreeMap Department Report
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 3: SORTED DEPARTMENT REPORT VIA TreeMap");
        System.out.println("---------------------------------------------------------");
        Map<String, List<Employee>> deptReport = directory.getDepartmentReport();
        System.out.println("(Notice: Department names are sorted alphabetically by TreeMap!)\n");
        deptReport.forEach((dept, emps) -> {
            System.out.printf("[%s] Total: %d%n", dept.toUpperCase(), emps.size());
            emps.forEach(emp -> System.out.println("   " + emp));
        });

        // -------------------------------------------------------------
        // SECTION 4: Comparable (Natural Ordering by ID)
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 4: NATURAL ORDERING BY ID (Comparable)");
        System.out.println("---------------------------------------------------------");
        List<Employee> sortedById = directory.getEmployeesSortedById();
        sortedById.forEach(emp -> System.out.println("   " + emp));

        // -------------------------------------------------------------
        // SECTION 5: Top-5 Salary via Chained Comparator
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 5: TOP-5 SALARIES VIA CHAINED COMPARATOR");
        System.out.println("---------------------------------------------------------");
        System.out.println("Sorting Criteria: Salary DESC -> Name ASC -> ID ASC\n");
        List<Employee> top5 = directory.getTop5BySalary();
        int rank = 1;
        for (Employee emp : top5) {
            System.out.printf("   #%d: %s%n", rank++, emp);
        }

        // -------------------------------------------------------------
        // SECTION 6: Missing hashCode() Bug Demo and Fix
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 6: MISSING-hashCode() BUG DEMONSTRATION & FIX");
        System.out.println("---------------------------------------------------------");
        demonstrateMissingHashCodeBug();

        System.out.println("\n=========================================================");
        System.out.println("          DAY 8 DEMONSTRATION EXECUTED SUCCESSFULLY      ");
        System.out.println("=========================================================");
    }

    /**
     * Demonstrates why violating the equals() and hashCode() contract breaks HashMaps.
     */
    private static void demonstrateMissingHashCodeBug() {
        Employee sampleEmp = new Employee(101, "John Doe", "HR", 65000.00);

        // 1. THE BUG: Using BuggyEmployeeKey (equals defined, hashCode missing)
        System.out.println("[PART A] Testing BuggyEmployeeKey (equals overridden, hashCode OMITTED):");
        Map<BuggyEmployeeKey, Employee> buggyMap = new HashMap<>();

        BuggyEmployeeKey keyPut = new BuggyEmployeeKey(101);
        BuggyEmployeeKey keyGet = new BuggyEmployeeKey(101);

        buggyMap.put(keyPut, sampleEmp);

        System.out.printf("   - keyPut : %s%n", keyPut);
        System.out.printf("   - keyGet : %s%n", keyGet);
        System.out.printf("   - keyPut.equals(keyGet)? : %b%n", keyPut.equals(keyGet));
        System.out.printf("   - keyPut.hashCode() == keyGet.hashCode()? : %b (DIFFERENT BUCKETS!)%n",
                keyPut.hashCode() == keyGet.hashCode());

        Employee resultBuggy = buggyMap.get(keyGet);
        System.out.printf("   >>> buggyMap.get(keyGet) Result: %s [FAILED LOOKUP: Expected employee, got null!]%n", 
                resultBuggy);

        // 2. THE FIX: Using FixedEmployeeKey (both equals and hashCode defined)
        System.out.println("\n[PART B] Testing FixedEmployeeKey (both equals and hashCode PROPERLY overridden):");
        Map<FixedEmployeeKey, Employee> fixedMap = new HashMap<>();

        FixedEmployeeKey fixedKeyPut = new FixedEmployeeKey(101);
        FixedEmployeeKey fixedKeyGet = new FixedEmployeeKey(101);

        fixedMap.put(fixedKeyPut, sampleEmp);

        System.out.printf("   - fixedKeyPut : %s%n", fixedKeyPut);
        System.out.printf("   - fixedKeyGet : %s%n", fixedKeyGet);
        System.out.printf("   - fixedKeyPut.equals(fixedKeyGet)? : %b%n", fixedKeyPut.equals(fixedKeyGet));
        System.out.printf("   - fixedKeyPut.hashCode() == fixedKeyGet.hashCode()? : %b (SAME BUCKET!)%n",
                fixedKeyPut.hashCode() == fixedKeyGet.hashCode());

        Employee resultFixed = fixedMap.get(fixedKeyGet);
        System.out.printf("   >>> fixedMap.get(fixedKeyGet) Result: %s [SUCCESSFUL LOOKUP!]%n", 
                resultFixed);
    }
}
