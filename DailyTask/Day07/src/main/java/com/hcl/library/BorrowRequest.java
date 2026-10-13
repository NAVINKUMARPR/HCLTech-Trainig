package com.hcl.library;

/**
 * BorrowRequest Model Class
 * Represents a member waiting in queue for a book.
 * Implements Comparable to give Faculty requests higher priority in PriorityQueue,
 * using requestSequence for FIFO tie-breaking within the same priority tier.
 */
public class BorrowRequest implements Comparable<BorrowRequest> {

    private final String requestId;
    private final String memberName;
    private final String bookTitle;
    private final boolean isFaculty;
    private final int requestSequence;

    public BorrowRequest(String requestId, String memberName, String bookTitle, boolean isFaculty, int requestSequence) {
        this.requestId = requestId;
        this.memberName = memberName;
        this.bookTitle = bookTitle;
        this.isFaculty = isFaculty;
        this.requestSequence = requestSequence;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public boolean isFaculty() {
        return isFaculty;
    }

    public int getRequestSequence() {
        return requestSequence;
    }

    /**
     * Priority rules:
     * 1. Faculty requests take higher priority over students/regular members.
     * 2. Within the same role, earlier requestSequence comes first (FIFO tie-breaker).
     */
    @Override
    public int compareTo(BorrowRequest other) {
        // Faculty (true) comes before non-faculty (false)
        if (this.isFaculty != other.isFaculty) {
            return this.isFaculty ? -1 : 1;
        }
        // FIFO order for members in the same tier
        return Integer.compare(this.requestSequence, other.requestSequence);
    }

    @Override
    public String toString() {
        return String.format("Request[ID=%s, Member='%s' (%s), Book='%s', Seq=%d]",
                requestId, memberName, isFaculty ? "FACULTY" : "STUDENT", bookTitle, requestSequence);
    }
}
