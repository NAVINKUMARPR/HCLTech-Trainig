package com.hcl.review;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Production-grade StudentService
 *
 * Incorporates fixes for 5+ critical issues identified during code review of naive AI generation:
 * - Fix 1: Thread-safe repository using ConcurrentHashMap (fixes race conditions in ArrayList).
 * - Fix 2: Strict input validation on null arguments (fixes silent NullPointerExceptions).
 * - Fix 3: Duplicate student ID prevention (fixes accidental data clobbering).
 * - Fix 4: Encapsulation protection returning unmodifiable snapshots (fixes state leakage).
 * - Fix 5: Optional<Student> return type (fixes null returns and caller NPEs).
 * - Fix 6: O(1) indexed lookup instead of O(n) linear loop traversal.
 */
public class StudentService {

    // [FIX 1 & 6]: ConcurrentHashMap replaces non-thread-safe ArrayList, providing O(1) thread-safe access
    private final Map<Integer, Student> studentRepository = new ConcurrentHashMap<>();

    /**
     * Registers a new student.
     *
     * @param student the student to register
     * @throws NullPointerException if student is null [FIX 2]
     * @throws IllegalStateException if a student with the same ID already exists [FIX 3]
     */
    public void registerStudent(Student student) {
        // [FIX 2]: Validate non-null input immediately
        Objects.requireNonNull(student, "Student parameter cannot be null.");

        // [FIX 3]: Prevent silent record clobbering / duplicate IDs
        Student existing = studentRepository.putIfAbsent(student.getId(), student);
        if (existing != null) {
            throw new IllegalStateException("Student registration rejected: ID " + student.getId() + " already exists!");
        }
    }

    /**
     * Finds a student by ID.
     *
     * @param id student identifier
     * @return Optional containing the student if present, empty Optional otherwise [FIX 5]
     */
    public Optional<Student> findStudentById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }
        // [FIX 5 & 6]: Returns Optional and performs O(1) lookup
        return Optional.ofNullable(studentRepository.get(id));
    }

    /**
     * Returns an unmodifiable snapshot of all registered students.
     *
     * @return unmodifiable list of students [FIX 4]
     */
    public List<Student> getAllStudents() {
        // [FIX 4]: Defensive copy wrapped in unmodifiableList prevents callers from clearing or mutating repository
        return Collections.unmodifiableList(new ArrayList<>(studentRepository.values()));
    }

    /**
     * Updates student GPA with validation.
     */
    public boolean updateStudentGpa(int id, double newGpa) {
        Student student = studentRepository.get(id);
        if (student == null) {
            return false;
        }
        student.updateGpa(newGpa);
        return true;
    }

    /**
     * Removes a student by ID.
     */
    public boolean removeStudent(int id) {
        return studentRepository.remove(id) != null;
    }

    /**
     * Returns total registered student count.
     */
    public int getStudentCount() {
        return studentRepository.size();
    }
}
