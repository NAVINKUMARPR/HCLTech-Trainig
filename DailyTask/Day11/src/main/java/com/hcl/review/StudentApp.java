package com.hcl.review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * StudentApp - Main Demonstration Runner for Day 11
 *
 * Demonstrates:
 * 1. POJO Prompt Comparison: Vague vs Specific Prompt Outputs.
 * 2. StudentService Review: Testing the 5+ resolved code issues.
 */
public class StudentApp {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("         DAY 11: PROMPT & CODE REVIEW DRILL              ");
        System.out.println("=========================================================");

        // =============================================================
        // SECTION 1: VAGUE VS. SPECIFIC POJO DEMONSTRATION
        // =============================================================
        System.out.println("\n>>> SECTION 1: VAGUE PROMPT VS SPECIFIC PROMPT POJO");

        System.out.println("\n[1. Testing VagueStudent (Flawed AI Output)]:");
        VagueStudent vague = new VagueStudent(-99, "", 99.5, new ArrayList<>(List.of("Math")));
        System.out.println("   * VagueStudent instantiated without error despite corrupted data:");
        System.out.printf("     ID: %d | Name: '%s' | GPA: %.1f%n", vague.getId(), vague.getName(), vague.getGpa());
        vague.getCourses().clear(); // Corrupts internal collection directly!
        System.out.println("     Courses after external clear(): " + vague.getCourses());

        System.out.println("\n[2. Testing Production Student (Specific Prompt Output)]:");
        try {
            new Student(-1, "Alice", 3.8, List.of("CS101"));
        } catch (IllegalArgumentException ex) {
            System.out.println("   * Successfully rejected invalid ID (-1): " + ex.getMessage());
        }

        try {
            new Student(101, "", 3.8, List.of("CS101"));
        } catch (IllegalArgumentException ex) {
            System.out.println("   * Successfully rejected blank name: " + ex.getMessage());
        }

        try {
            new Student(101, "Alice", 5.5, List.of("CS101"));
        } catch (IllegalArgumentException ex) {
            System.out.println("   * Successfully rejected out-of-bounds GPA (5.5): " + ex.getMessage());
        }

        Student validStudent = new Student(101, "Alice Smith", 3.85, List.of("Java", "Data Structures"));
        System.out.println("   * Valid Student created: " + validStudent);

        try {
            validStudent.getCourses().add("Hacking");
        } catch (UnsupportedOperationException ex) {
            System.out.println("   * Defensive Copy Protection: Cannot tamper with getCourses() -> " + ex.getClass().getSimpleName());
        }

        // =============================================================
        // SECTION 2: STUDENTSERVICE CODE REVIEW DRILL (5+ FIXES)
        // =============================================================
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 2: StudentService CODE REVIEW AUDIT (5+ FIXES)");
        System.out.println("---------------------------------------------------------");

        StudentService service = new StudentService();

        // Fix 1 & 6: Thread-safe O(1) Registration
        service.registerStudent(validStudent);
        service.registerStudent(new Student(102, "Bob Jones", 3.40, List.of("Algorithms", "Databases")));
        service.registerStudent(new Student(103, "Charlie Brown", 3.90, List.of("Cloud Computing")));

        System.out.printf("   [INFO] Registered %,d students into ConcurrentHashMap repository.%n", service.getStudentCount());

        // Fix 2: Null Validation
        System.out.println("\n--- [Audit Fix 2] Testing Null Student Rejection ---");
        try {
            service.registerStudent(null);
        } catch (NullPointerException ex) {
            System.out.println("   [PASSED] Caught expected NullPointerException: " + ex.getMessage());
        }

        // Fix 3: Duplicate ID Prevention
        System.out.println("\n--- [Audit Fix 3] Testing Duplicate ID Prevention ---");
        try {
            service.registerStudent(new Student(101, "Imposter Alice", 2.00, List.of("Art")));
        } catch (IllegalStateException ex) {
            System.out.println("   [PASSED] Caught expected IllegalStateException: " + ex.getMessage());
        }

        // Fix 4: Encapsulation & Defensive Return
        System.out.println("\n--- [Audit Fix 4] Testing Repository Encapsulation Leakage ---");
        List<Student> exposedList = service.getAllStudents();
        try {
            exposedList.clear(); // Attempt to wipe database
        } catch (UnsupportedOperationException ex) {
            System.out.println("   [PASSED] External wipe blocked! List is unmodifiable: " + ex.getClass().getSimpleName());
        }
        System.out.println("   [VERIFICATION] Repository size remains intact: " + service.getStudentCount());

        // Fix 5: Optional<Student> Return
        System.out.println("\n--- [Audit Fix 5] Testing Optional<Student> Returns ---");
        Optional<Student> studentOpt = service.findStudentById(102);
        studentOpt.ifPresentOrElse(
                s -> System.out.println("   [FOUND] " + s),
                () -> System.out.println("   [NOT FOUND]")
        );

        Optional<Student> missingOpt = service.findStudentById(999);
        missingOpt.ifPresentOrElse(
                s -> System.out.println("   [FOUND] " + s),
                () -> System.out.println("   [SAFE EMPTY OPTIONAL] Handled non-existent ID 999 cleanly without null!")
        );

        System.out.println("\n=========================================================");
        System.out.println("        DAY 11 PROMPT & REVIEW DRILL COMPLETE            ");
        System.out.println("=========================================================");
    }
}
