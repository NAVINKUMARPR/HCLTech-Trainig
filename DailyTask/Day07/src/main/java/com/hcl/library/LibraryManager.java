package com.hcl.library;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * LibraryManager Class
 *
 * Demonstrates:
 * 1. ArrayList catalogue storage.
 * 2. LinkedHashSet for removing duplicate catalogue entries while preserving insertion order.
 * 3. ArrayDeque as a fast, resizable FIFO waitlist.
 * 4. PriorityQueue for faculty-first waitlist scheduling.
 * 5. Iterator.remove() for safely deleting overdue items without ConcurrentModificationException.
 */
public class LibraryManager {

    private List<Book> catalogue = new ArrayList<>();
    private final Deque<BorrowRequest> standardWaitlist = new ArrayDeque<>();
    private final PriorityQueue<BorrowRequest> facultyFirstWaitlist = new PriorityQueue<>();
    private final List<Book> issuedBooks = new ArrayList<>();

    // =========================================================================
    // 1. ArrayList Catalogue & LinkedHashSet De-duplication
    // =========================================================================

    public void addToCatalogue(Book book) {
        catalogue.add(book);
    }

    public List<Book> getCatalogue() {
        return catalogue;
    }

    /**
     * De-duplicates the catalogue using LinkedHashSet.
     * Preserves exact original insertion order while eliminating duplicate ISBNs.
     */
    public List<Book> deduplicateCatalogue() {
        LinkedHashSet<Book> uniqueBooks = new LinkedHashSet<>(catalogue);
        this.catalogue = new ArrayList<>(uniqueBooks);
        return this.catalogue;
    }

    // =========================================================================
    // 2. ArrayDeque Standard FIFO Waitlist
    // =========================================================================

    public void enqueueStandardWaitlist(BorrowRequest request) {
        standardWaitlist.offer(request); // Adds to tail
    }

    public BorrowRequest serveNextStandard() {
        return standardWaitlist.poll(); // Retrieves and removes from head
    }

    public Deque<BorrowRequest> getStandardWaitlist() {
        return standardWaitlist;
    }

    // =========================================================================
    // 3. PriorityQueue Faculty-First Waitlist
    // =========================================================================

    public void enqueuePriorityWaitlist(BorrowRequest request) {
        facultyFirstWaitlist.offer(request);
    }

    public BorrowRequest serveNextPriority() {
        return facultyFirstWaitlist.poll();
    }

    public PriorityQueue<BorrowRequest> getFacultyFirstWaitlist() {
        return facultyFirstWaitlist;
    }

    // =========================================================================
    // 4. Issued Books & Iterator.remove() for Overdue Items
    // =========================================================================

    public void issueBook(Book book) {
        issuedBooks.add(book);
    }

    public List<Book> getIssuedBooks() {
        return issuedBooks;
    }

    /**
     * Purges overdue books using Iterator.remove().
     * Safe from ConcurrentModificationException during collection iteration.
     *
     * @return List of purged overdue books
     */
    public List<Book> purgeOverdueBooks() {
        List<Book> purged = new ArrayList<>();
        Iterator<Book> iterator = issuedBooks.iterator();

        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.isOverdue()) {
                purged.add(book);
                iterator.remove(); // Safely removes current element from underlying collection
            }
        }
        return purged;
    }
}
