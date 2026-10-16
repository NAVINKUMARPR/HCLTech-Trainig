package com.hcl.review;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Production-grade Student POJO
 *
 * Generated from SPECIFIC Prompt:
 * "Create a robust, production-grade Student POJO in Java 21 with:
 *  1. Immutable student ID and validated non-blank name.
 *  2. Strict constructor validation: id > 0, name non-blank, GPA within [0.0, 4.0].
 *  3. Defensive copying for mutable List<String> courses and unmodifiable getter.
 *  4. Correct equals() and hashCode() based on unique student ID.
 *  5. Informative toString() formatting."
 */
public final class Student {

    private final int id;
    private final String name;
    private double gpa;
    private final List<String> courses;

    public Student(int id, String name, double gpa, List<String> courses) {
        // 1. Strict Validation on ID
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be strictly positive: " + id);
        }
        // 2. Strict Validation on Name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or blank.");
        }
        // 3. Strict Validation on GPA
        validateGpa(gpa);

        this.id = id;
        this.name = name.trim();
        this.gpa = gpa;

        // 4. Defensive Copy of Courses Collection
        this.courses = new ArrayList<>(courses != null ? courses : List.of());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getGpa() {
        return gpa;
    }

    /**
     * Validated mutator for updating GPA.
     */
    public void updateGpa(double newGpa) {
        validateGpa(newGpa);
        this.gpa = newGpa;
    }

    /**
     * Returns an unmodifiable view of courses to prevent external state tampering.
     */
    public List<String> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    /**
     * Controlled method to enroll in a new course with validation.
     */
    public void enrollCourse(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or blank.");
        }
        this.courses.add(courseName.trim());
    }

    private static void validateGpa(double gpa) {
        if (Double.isNaN(gpa) || gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0. Given: " + gpa);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return this.id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Student[ID=%-4d | Name=%-14s | GPA=%.2f | Courses=%s]",
                id, name, gpa, courses);
    }
}
