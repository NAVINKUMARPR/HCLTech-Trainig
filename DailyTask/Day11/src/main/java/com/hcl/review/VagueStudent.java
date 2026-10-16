package com.hcl.review;

import java.util.List;

/**
 * VagueStudent POJO
 *
 * Generated from VAGUE Prompt:
 * "Create a Student class in Java"
 *
 * Characteristics / Flaws:
 * 1. No input validation (allows id <= 0, empty names, negative/infinite GPAs).
 * 2. Mutable state without defensive copying (exposes internal courses list).
 * 3. Missing equals() and hashCode() contracts (breaks HashSet and HashMap).
 * 4. Missing toString() override (prints default memory address).
 */
public class VagueStudent {

    private int id;
    private String name;
    private double gpa;
    private List<String> courses;

    // Default no-arg constructor allows uninitialized inconsistent object state
    public VagueStudent() {
    }

    public VagueStudent(int id, String name, double gpa, List<String> courses) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.courses = courses; // Flaw: Direct reference assigned without defensive copy!
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id; // Flaw: No check for positive ID
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name; // Flaw: Allows null or empty string
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa; // Flaw: Allows invalid GPA range (e.g., -2.5 or 99.0)
    }

    public List<String> getCourses() {
        return courses; // Flaw: Exposes mutable internal collection directly
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    // NOTE: equals() and hashCode() are completely omitted!
}
