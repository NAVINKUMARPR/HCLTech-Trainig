# Day 7: Library Waitlist System (Java Collections Framework)

## 📌 Summary
A comprehensive implementation of a Library Waitlist & Catalog Management System demonstrating the core data structures of the **Java Collections Framework (JCF)**:
- **`ArrayList` Catalogue**: Dynamic resizable array storing catalogued books.
- **`LinkedHashSet` De-duplication**: Strips duplicate books (by ISBN) while preserving exact original insertion order.
- **`ArrayDeque` Waitlist**: High-performance, resizable FIFO double-ended queue for general student waitlists.
- **`PriorityQueue` Faculty-First Waitlist**: Min-heap priority queue prioritizing Faculty members before students, with FIFO tie-breaking.
- **`Iterator.remove()` Purge**: Safe removal of overdue borrowed books during traversal without triggering `ConcurrentModificationException`.
- **Performance Benchmark**: Empirical timing comparing `ArrayList` vs. `LinkedList` for head insertions (`add(0, e)`) and random access (`get(i)`).

---

## 📁 Project Structure

```text
DailyTask/
└── Day07/
    ├── pom.xml                                   # Maven configuration (Java 21, exec plugin)
    ├── README.md                                 # Quick revision notes & benchmark analysis
    └── src/
        └── main/
            └── java/
                └── com/
                    └── hcl/
                        └── library/
                            ├── Book.java          # Model with equals/hashCode on ISBN
                            ├── BorrowRequest.java # Model implementing Comparable for PriorityQueue
                            ├── LibraryManager.java# Core collection operations & waitlist routing
                            ├── ListBenchmark.java # Performance benchmark: ArrayList vs LinkedList
                            └── LibraryApp.java    # Application runner & demonstration
```

---

## 🧠 Core Collections Concepts for Revision

### 1. JCF Collection Comparison Table

| Collection | Underlying Data Structure | Ordering | Duplicates? | Best Use Case |
|---|---|---|---|---|
| **`ArrayList`** | Resizable `Object[]` array | Insertion Order | Allowed | Fast random access, reading and appending at the end. |
| **`LinkedList`** | Doubly-linked list nodes | Insertion Order | Allowed | Frequent head/tail additions/removals; sequential iteration. |
| **`LinkedHashSet`** | Hash table + doubly-linked list | Insertion Order | **No** (Unique) | De-duplicating collections while preserving exact insertion sequence. |
| **`ArrayDeque`** | Resizable circular array | FIFO / LIFO | Allowed | General Queues and Stacks; faster and less memory than `LinkedList` or legacy `Stack`. |
| **`PriorityQueue`** | Binary Min-Heap (Array-based) | Priority Order | Allowed | Processing elements according to priority (`Comparable` / `Comparator`). |

---

### 2. Why `Iterator.remove()` Instead of a `for-each` Loop?

```java
// ❌ WRONG: Throws ConcurrentModificationException
for (Book b : issuedBooks) {
    if (b.isOverdue()) {
        issuedBooks.remove(b); // Modifies collection while iterator is active!
    }
}

// ✅ CORRECT: Safe removal via Iterator
Iterator<Book> it = issuedBooks.iterator();
while (it.hasNext()) {
    Book b = it.next();
    if (b.isOverdue()) {
        it.remove(); // Safely updates internal modCount & cursor
    }
}
```
- **Explanation**: Java collections use internal field `modCount`. In a `for-each` loop, calling `collection.remove()` changes `modCount`, causing the hidden iterator to detect mismatch and immediately throw a fail-fast **`ConcurrentModificationException`**.
- Calling **`iterator.remove()`** updates both the underlying list and the iterator’s expected modification state synchronously.

---

### 3. `Comparable` in `PriorityQueue` (Faculty-First)

```java
@Override
public int compareTo(BorrowRequest other) {
    // 1. Role priority: Faculty (true) comes before Student (false)
    if (this.isFaculty != other.isFaculty) {
        return this.isFaculty ? -1 : 1;
    }
    // 2. Tie-breaker: Earlier request sequence arrives first (FIFO)
    return Integer.compare(this.requestSequence, other.requestSequence);
}
```

---

## ⏱️ Benchmark: `ArrayList` vs. `LinkedList`

Empirical results from [ListBenchmark.java](file:///d:/HCLTech/DailyTask/Day07/src/main/java/com/hcl/library/ListBenchmark.java):

```text
[TEST 1] Inserting 50,000 elements at Head (index 0):
   - ArrayList.add(0, e)  :   127.47 ms (O(n) - element shifting required)
   - LinkedList.add(0, e) :    10.25 ms (O(1) - constant-time node link)
   >>> Winner for Head Inserts: LinkedList (12.4x faster)

[TEST 2] Random get(i) across 10,000 lookups from 50,000 elements:
   - ArrayList.get(i)     :     1.44 ms (O(1) - direct memory offset calculation)
   - LinkedList.get(i)    :   245.11 ms (O(n) - sequential node pointer hopping)
   >>> Winner for Random Access: ArrayList (170.4x faster)
```

### 🔬 Why the Massive Difference?
1. **Head Insertion (`add(0, element)`)**:
   - `ArrayList` must shift every existing element one index to the right in memory ($O(n)$ work per insert $\implies O(n^2)$ total).
   - `LinkedList` simply allocates a new `Node` and updates `head.prev` pointer ($O(1)$ constant time).
2. **Random Access (`get(i)`)**:
   - `ArrayList` calculates index memory address directly in hardware: `base_address + (i * element_size)` ($O(1)$ instant lookup).
   - `LinkedList` has no index arithmetic; it must traverse node-by-node from the head/tail until reaching index `i` ($O(n)$ pointer hopping).

---

## 🛠️ How to Build and Run

### Option 1: Apache Maven (Recommended)
From `DailyTask/Day07`:
```powershell
mvn clean compile exec:java
```

### Option 2: Standard `javac` & `java`
From `DailyTask/Day07`:
```powershell
# Compile all classes
javac -d target/classes src/main/java/com/hcl/library/*.java

# Run LibraryApp
java -cp target/classes com.hcl.library.LibraryApp
```

---

## 🚀 Push Day 7 to GitHub

From the repository root (`d:\HCLTech`):

```powershell
cd d:\HCLTech

# Stage Day 7 files
git add DailyTask/Day07

# Commit
git commit -m "feat: complete Day 7 library waitlist collections and benchmark task"

# Push
git push origin main
```
