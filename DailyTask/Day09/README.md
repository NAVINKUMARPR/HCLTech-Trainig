# Day 9: Generic Toolkit & Resource Management

## 📌 Summary
A comprehensive toolkit in Java demonstrating advanced **Generics**, the **PECS principle**, and **Resource Management**:
- **Generic Classes**: `Pair<K, V>` holding arbitrary key-value pairs.
- **Bounded Generics**: `Box<T extends Number>` restricted to numeric data types.
- **Generic Methods**: `<T extends Comparable<T>> T findMax(...)` for finding the maximum of any comparable types (numbers, strings, domain entities).
- **Wildcards & PECS**: Producer Extends (`? extends Number`) for reading; Consumer Super (`? super Integer`) for writing.
- **`try-with-resources`**: Clean, leak-free CSV file parsing with `BufferedReader`.
- **`AutoCloseable` LIFO Order**: Proof that resources declared in a `try(...)` block are closed in **reverse order of declaration** (LIFO: Last-In, First-Out).

---

## 📁 Project Structure

```text
DailyTask/
└── Day09/
    ├── pom.xml                                       # Maven configuration (Java 21, exec plugin)
    ├── README.md                                     # Revision guide & concept notes
    └── src/
        └── main/
            ├── resources/
            │   └── products.csv                      # Sample inventory CSV dataset
            └── java/
                └── com/
                    └── hcl/
                        └── generics/
                            ├── Pair.java             # Generic Pair<K, V>
                            ├── Box.java              # Bounded Box<T extends Number>
                            ├── GenericUtils.java     # Generic max() and wildcard methods
                            ├── Product.java          # Model implementing Comparable by price
                            ├── ProductReader.java    # CSV parser using try-with-resources
                            ├── CustomResource.java   # AutoCloseable demonstrating close order
                            └── GenericApp.java       # Main demonstration runner
```

---

## 🧠 Core Concepts for Quick Revision

### 1. Bounded Type Parameters (`extends`)

Restricts the type arguments that can be passed to a generic class or method:

```java
public class Box<T extends Number> { ... }
```
- **Allowed**: `Box<Integer>`, `Box<Double>`, `Box<Float>`, `Box<Long>`.
- **Rejected at Compile Time**: `Box<String>`, `Box<Product>`.
- **Advantage**: Inside `Box`, we can safely invoke methods from the `Number` class (e.g., `item.doubleValue()`) without casting.

---

### 2. Bounded Generic Method: `<T extends Comparable<T>> max()`

Enables finding the maximum across any types that implement `Comparable`:

```java
public static <T extends Comparable<T>> T findMax(T a, T b, T c) {
    T max = a;
    if (b.compareTo(max) > 0) max = b;
    if (c.compareTo(max) > 0) max = c;
    return max;
}
```
- Works uniformly with `Integer`, `String`, `Double`, or custom domain objects like `Product`!

---

### 3. Wildcards & The PECS Rule

> **PECS**: **P**roducer **E**xtends, **C**onsumer **S**uper

| Wildcard Syntax | PECS Role | Allowed Operation | Example in Day 9 |
|---|---|---|---|
| **`? extends Number`** | **Producer** (Source) | **Read-Only**: You can read elements as `Number`, but cannot add elements (except `null`). | `sumOfNumbers(List<? extends Number> list)` |
| **`? super Integer`** | **Consumer** (Destination) | **Write-Friendly**: You can safely add `Integer` objects to the list. | `addIntegers(List<? super Integer> list, ...)` |
| **`?` (Unbounded)** | Unknown Type | Read as `Object`, cannot add elements. | `printList(List<?> list)` |

---

### 4. `try-with-resources` & LIFO Close Order

Introduced in Java 7, `try-with-resources` automatically invokes `.close()` on any class implementing `java.lang.AutoCloseable`:

```java
try (CustomResource r1 = new CustomResource("Resource 1");
     CustomResource r2 = new CustomResource("Resource 2");
     CustomResource r3 = new CustomResource("Resource 3")) {

    // Normal work here...
} // Closed automatically here!
```

#### 🔄 Execution Order:
1. **Acquired (Creation Order)**: `r1` $\rightarrow$ `r2` $\rightarrow$ `r3`
2. **Released (Reverse/LIFO Order)**: `r3` $\rightarrow$ `r2` $\rightarrow$ `r1`
- **Why LIFO?** Downstream resources usually depend on upstream resources (e.g., a `BufferedWriter` depends on a `FileWriter`). Closing in reverse order prevents closing a connection while dependent wrappers are still flushing data!

---

## 🖥️ Sample Run Output

```text
=========================================================
          DAY 9: GENERIC TOOLKIT & RESOURCES DEMO        
=========================================================

>>> SECTION 1: GENERIC Pair<K, V>
Created Pairs:
   * Pair[Alice => 21] | Key: Alice, Value: 21
   * Pair[200 => OK]
   * Pair[Laptop => 1299.99]

---------------------------------------------------------
>>> SECTION 2: BOUNDED Box<T extends Number>
---------------------------------------------------------
   * Box[100 (Integer)] -> doubleValue: 100.0
   * Box[99.45 (Double)] -> doubleValue: 99.45
   * Box[500 (Long)] -> doubleValue: 500.0
   Is intBox (100) > doubleBox (99.45)? true
   [TYPE SAFETY]: Box<String> is rejected at compile-time because String does not extend Number.

---------------------------------------------------------
>>> SECTION 3: GENERIC METHOD <T extends Comparable<T>> max()
---------------------------------------------------------
   * max(45, 120, 89)                => 120
   * max(Banana, Strawberry, Apple)  => Strawberry
   * max(List of Doubles)            => 99.8

---------------------------------------------------------
>>> SECTION 4: WILDCARDS & PECS PRINCIPLE
---------------------------------------------------------
1. Upper-Bounded (? extends Number) - Reading values:
   - Sum of integers: 60.00
   - Sum of doubles : 7.50

2. Lower-Bounded (? super Integer) - Writing values:
   - numberList after addIntegers: [100, 200, 300]
   - objectList after addIntegers: [500, 600]

---------------------------------------------------------
>>> SECTION 5: READ products.csv (try-with-resources)
---------------------------------------------------------
Parsed 5 products from products.csv:

   * Product[#101 | Wireless Ergonomic Mouse         | Electronics  | $  29.99 | Stock:  50]
   * Product[#102 | Mechanical Gaming Keyboard       | Electronics  | $  89.50 | Stock:  30]
   * Product[#103 | Insulated Ceramic Coffee Mug     | Kitchen      | $  14.99 | Stock: 100]
   * Product[#104 | Height Adjustable Standing Desk  | Furniture    | $ 349.00 | Stock:  15]
   * Product[#105 | Active Noise Cancelling Headphones | Electronics  | $ 199.99 | Stock:  25]

   >>> Most Expensive Product (via generic max): Product[#104 | Height Adjustable Standing Desk  | Furniture    | $ 349.00 | Stock:  15]

---------------------------------------------------------
>>> SECTION 6: CUSTOM AutoCloseable & CLOSE ORDER (LIFO)
---------------------------------------------------------
Declaring 3 resources inside try-with-resources:

   [INIT] Acquired/Opened: Resource-1 (DatabaseConnection)
   [INIT] Acquired/Opened: Resource-2 (CloudStorageSession)
   [INIT] Acquired/Opened: Resource-3 (AuditLogStream)

   [INSIDE TRY BLOCK]: Working with active resources...
   [WORK] Processing business logic using Resource-1 (DatabaseConnection)
   [WORK] Processing business logic using Resource-2 (CloudStorageSession)
   [WORK] Processing business logic using Resource-3 (AuditLogStream)
   [INSIDE TRY BLOCK]: Work finished. Exiting try block now...

   [CLOSE] Released/Closed: Resource-3 (AuditLogStream)
   [CLOSE] Released/Closed: Resource-2 (CloudStorageSession)
   [CLOSE] Released/Closed: Resource-1 (DatabaseConnection)

(Notice: Resources were closed in REVERSE order of declaration: LIFO order!)
```

---

## 🛠️ How to Build and Run

### Option 1: Apache Maven (Recommended)
From `DailyTask/Day09`:
```powershell
mvn clean compile exec:java
```

### Option 2: Standard `javac` & `java`
From `DailyTask/Day09`:
```powershell
# Compile all source files into target/classes
javac -d target/classes src/main/java/com/hcl/generics/*.java

# Copy resources into target/classes
copy src\main\resources\products.csv target\classes\

# Run GenericApp
java -cp target/classes com.hcl.generics.GenericApp
```

---

## 🚀 Push Day 9 to GitHub

From the repository root (`d:\HCLTech`):

```powershell
cd d:\HCLTech

# Stage Day 9 files
git add DailyTask/Day09

# Commit changes
git commit -m "feat: complete Day 9 generic toolkit, CSV try-with-resources, and AutoCloseable LIFO task"

# Push to GitHub
git push origin main
```
