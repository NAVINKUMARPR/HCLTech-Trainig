package com.hcl.library;

import java.util.List;

/**
 * LibraryApp - Main Demonstration Runner for Day 7
 *
 * Demonstrates:
 * 1. ArrayList catalogue population and LinkedHashSet deduplication (maintaining order).
 * 2. ArrayDeque standard FIFO waitlist.
 * 3. PriorityQueue faculty-first priority waitlist.
 * 4. Safe overdue book removal using Iterator.remove().
 * 5. ArrayList vs LinkedList performance benchmarking on head inserts and get(i).
 */
public class LibraryApp {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("         DAY 7: LIBRARY WAITLIST & COLLECTIONS DEMO       ");
        System.out.println("=========================================================");

        LibraryManager manager = new LibraryManager();

        // -------------------------------------------------------------
        // SECTION 1: ArrayList Catalogue & LinkedHashSet De-duplication
        // -------------------------------------------------------------
        System.out.println("\n>>> SECTION 1: CATALOGUE & LinkedHashSet DE-DUPLICATION");

        manager.addToCatalogue(new Book("978-0132350884", "Clean Code", "Robert C. Martin"));
        manager.addToCatalogue(new Book("978-0134685991", "Effective Java", "Joshua Bloch"));
        manager.addToCatalogue(new Book("978-0132350884", "Clean Code (Duplicate)", "Robert C. Martin")); // Duplicate ISBN
        manager.addToCatalogue(new Book("978-0596009205", "Head First Design Patterns", "Eric Freeman"));
        manager.addToCatalogue(new Book("978-0134685991", "Effective Java (Duplicate)", "Joshua Bloch")); // Duplicate ISBN

        System.out.println("Catalogue before de-duplication (size: " + manager.getCatalogue().size() + "):");
        for (Book b : manager.getCatalogue()) {
            System.out.println("   * " + b);
        }

        List<Book> uniqueCatalogue = manager.deduplicateCatalogue();
        System.out.println("\nCatalogue after LinkedHashSet de-duplication (size: " + uniqueCatalogue.size() + "):");
        System.out.println("(Notice: Duplicate ISBNs removed while exact insertion order is preserved!)");
        for (Book b : uniqueCatalogue) {
            System.out.println("   * " + b);
        }

        // -------------------------------------------------------------
        // SECTION 2: ArrayDeque Standard FIFO Waitlist
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 2: ArrayDeque STANDARD FIFO WAITLIST");
        System.out.println("---------------------------------------------------------");

        manager.enqueueStandardWaitlist(new BorrowRequest("REQ-101", "Alice", "Clean Code", false, 1));
        manager.enqueueStandardWaitlist(new BorrowRequest("REQ-102", "Bob", "Clean Code", false, 2));
        manager.enqueueStandardWaitlist(new BorrowRequest("REQ-103", "Charlie", "Clean Code", false, 3));

        System.out.println("Serving requests in FIFO order via ArrayDeque.poll():");
        while (!manager.getStandardWaitlist().isEmpty()) {
            BorrowRequest req = manager.serveNextStandard();
            System.out.println("   [SERVED] " + req);
        }

        // -------------------------------------------------------------
        // SECTION 3: PriorityQueue Faculty-First Waitlist
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 3: PriorityQueue FACULTY-FIRST WAITLIST");
        System.out.println("---------------------------------------------------------");

        // Students arrive first (sequences 1 and 2)
        manager.enqueuePriorityWaitlist(new BorrowRequest("REQ-201", "Student Dan", "Effective Java", false, 1));
        manager.enqueuePriorityWaitlist(new BorrowRequest("REQ-202", "Student Eve", "Effective Java", false, 2));

        // Faculty members arrive later (sequences 3 and 4)
        manager.enqueuePriorityWaitlist(new BorrowRequest("REQ-203", "Prof. Alan", "Effective Java", true, 3));
        manager.enqueuePriorityWaitlist(new BorrowRequest("REQ-204", "Dr. Grace", "Effective Java", true, 4));

        // Another student arrives later
        manager.enqueuePriorityWaitlist(new BorrowRequest("REQ-205", "Student Frank", "Effective Java", false, 5));

        System.out.println("Serving requests from PriorityQueue:");
        System.out.println("(Notice: Faculty members jump to the front of the queue, followed by students FIFO!)");
        while (!manager.getFacultyFirstWaitlist().isEmpty()) {
            BorrowRequest req = manager.serveNextPriority();
            System.out.println("   [SERVED PRIORITY] " + req);
        }

        // -------------------------------------------------------------
        // SECTION 4: Iterator.remove() for Overdue Items
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 4: SAFE REMOVAL OF OVERDUE ITEMS (Iterator.remove)");
        System.out.println("---------------------------------------------------------");

        manager.issueBook(new Book("978-101", "Java Concurrency in Practice", "Brian Goetz", false));
        manager.issueBook(new Book("978-102", "Refactoring", "Martin Fowler", true));          // Overdue
        manager.issueBook(new Book("978-103", "Design Patterns", "Gang of Four", false));
        manager.issueBook(new Book("978-104", "The Pragmatic Programmer", "Andy Hunt", true)); // Overdue

        System.out.println("Currently issued books before purge (count: " + manager.getIssuedBooks().size() + "):");
        for (Book b : manager.getIssuedBooks()) {
            System.out.println("   * " + b);
        }

        List<Book> purged = manager.purgeOverdueBooks();
        System.out.println("\nPurged overdue books using Iterator.remove() (count: " + purged.size() + "):");
        for (Book b : purged) {
            System.out.println("   [PURGED] " + b);
        }

        System.out.println("\nRemaining active issued books (count: " + manager.getIssuedBooks().size() + "):");
        for (Book b : manager.getIssuedBooks()) {
            System.out.println("   * " + b);
        }

        // -------------------------------------------------------------
        // SECTION 5: Benchmark: ArrayList vs. LinkedList
        // -------------------------------------------------------------
        ListBenchmark.runBenchmark();

        System.out.println("\n=========================================================");
        System.out.println("          DAY 7 DEMONSTRATION EXECUTED SUCCESSFULLY      ");
        System.out.println("=========================================================");
    }
}
