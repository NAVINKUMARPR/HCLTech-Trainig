package com.hcl.library;

import java.util.Objects;

/**
 * Book Model Class
 * Represents a library book with ISBN, title, author, and overdue status.
 * Implements equals & hashCode on ISBN for Set-based deduplication.
 */
public class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private boolean isOverdue;

    public Book(String isbn, String title, String author) {
        this(isbn, title, author, false);
    }

    public Book(String isbn, String title, String author, boolean isOverdue) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isOverdue = isOverdue;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return String.format("Book[ISBN=%s, Title='%s', Author='%s', Overdue=%s]",
                isbn, title, author, isOverdue);
    }
}
