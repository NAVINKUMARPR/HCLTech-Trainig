# Day 4: Bank Account OOP Architecture & Debugging with Hot Code Replace

## 📌 Summary
A complete Object-Oriented Banking Application developed in Java following clean layered architecture (`model`, `service`, `app` packages). This project demonstrates **encapsulation**, **class-level static counters**, **constructor chaining with `this(...)`**, **input validation**, **`equals()` and `hashCode()` contracts**, and a step-by-step case study on **debugging logic errors using Conditional Breakpoints, Watch Expressions, and Hot Code Replace (HCR)**.

---

## 📁 Project Structure

```text
DailyTask/
└── Day04/
    ├── pom.xml                                       # Maven configuration & plugins
    ├── README.md                                     # Revision guide & debugging walkthrough
    └── src/
        └── main/
            └── java/
                └── com/
                    └── hcl/
                        └── bank/
                            ├── model/
                            │   └── BankAccount.java  # Domain model (fields, constructors, equals/hashCode)
                            ├── service/
                            │   └── BankService.java  # Business logic & repository operations
                            └── app/
                                └── BankApp.java      # Application runner & debugging demonstration
```

---

## 🧠 Key Java OOP Concepts

| Concept | Explanation | Implementation in Day 4 |
|---|---|---|
| **Encapsulation** | Hiding internal state by making fields `private` and exposing controlled access through methods. | Fields `accountNumber`, `balance`, `accountHolder`, `accountType` are `private`. |
| **Static Counter** | Class-level variable shared across all instances, persisting throughout JVM runtime. | `private static int totalAccountsCreated = 0;` auto-increments with each new account. |
| **Constructor Chaining (`this(...)`)** | Calling one constructor from another within the same class to reduce code duplication and provide defaults. | 1-param $\rightarrow$ calls 2-param $\rightarrow$ calls 3-param master constructor. |
| **Business Validation** | Enforcing business rules before state mutation. | Rejects `amount <= 0` in deposit/withdraw; blocks overdrafts where `amount > balance`. |
| **`equals()` & `hashCode()`** | Defining logical equivalence based on unique business identifiers rather than memory addresses (`==`). | Overridden using `accountNumber` so `Set` and `Map` collections recognize duplicates. |
| **Package Separation** | Structuring code into dedicated layers for maintainability and separation of concerns. | `model` (state), `service` (business logic), `app` (entry point / console UI). |

---

## 🔗 Constructor Chaining Details

Constructor chaining streamlines object initialization by establishing a single "master" constructor that applies validation and sets fields:

```java
// 1. One-argument constructor (defaults initial balance to 0.0)
public BankAccount(String accountHolder) {
    this(accountHolder, 0.0);
}

// 2. Two-argument constructor (defaults account type to "Savings")
public BankAccount(String accountHolder, double initialBalance) {
    this(accountHolder, initialBalance, "Savings");
}

// 3. Master constructor (validates, increments static counter, sets fields)
public BankAccount(String accountHolder, double initialBalance, String accountType) {
    if (initialBalance < 0.0) {
        throw new IllegalArgumentException("Initial balance cannot be negative.");
    }
    totalAccountsCreated++;
    this.accountNumber = "ACC" + (1000 + totalAccountsCreated);
    this.accountHolder = accountHolder.trim();
    this.balance = initialBalance;
    this.accountType = accountType;
}
```

---

## 🐞 Debugging Case Study: The Planted Bug & Hot Code Replace (HCR)

### 1. The Planted Bug
In `BankAccount.java`, inside the `withdraw(double amount)` method, an erroneous `+=` operator was planted instead of `-=`:

```java
// BUGGY IMPLEMENTATION:
public boolean withdraw(double amount) {
    // ... validation checks ...
    this.balance += amount;  // ❌ BUG: Balance increases when withdrawing money!
    return true;
}
```
*Symptom*: When a user with a balance of `$1,000.00` withdraws `$200.00`, their balance increases to `$1,200.00`.

---

### 2. Setting a Conditional Breakpoint
Rather than pausing on every single withdrawal across the system, a **Conditional Breakpoint** suspends thread execution only when a specified boolean condition evaluates to `true`.

#### How to set in IDEs (VS Code / IntelliJ / Eclipse):
1. Open [BankAccount.java](file:///d:/HCLTech/DailyTask/Day04/src/main/java/com/hcl/bank/model/BankAccount.java) at the withdrawal line (`this.balance -= amount;`).
2. **Click the left margin** to set a breakpoint (red circle).
3. **Right-click the breakpoint** $\rightarrow$ select **"Edit Breakpoint"** / **"Condition"**.
4. Enter the condition:
   ```java
   amount > 100.0 && accountNumber.equals("ACC1002")
   ```
5. Run the debugger (`F5`). Execution will pause *only* when account `ACC1002` withdraws more than `$100.00`.

---

### 3. Adding a Watch Expression
While execution is paused at the breakpoint:
1. Open the **WATCH** panel in your debugger.
2. Click **`+`** (Add Expression) and add:
   - `this.balance` (Inspects current balance: `1000.0`)
   - `amount` (Inspects requested withdrawal: `200.0`)
   - `this.balance - amount` (Evaluates expected target balance: `800.0`)
3. Notice that stepping forward with the bug would cause `this.balance` to become `1200.0`.

---

### 4. Fixing with Hot Code Replace (HCR)
**Hot Code Replace (HCR)** is a JVM debugging capability (via the Java Debug Interface / JPDA) that allows developers to replace modified class files in the running target VM without restarting the application:

1. **Keep the debugger paused** on the breakpoint line.
2. In the source editor, change:
   ```diff
   - this.balance += amount;
   + this.balance -= amount;
   ```
3. Press **`Ctrl + S`** (Save).
4. The IDE compiles the class and hot-swaps the new bytecode into the running JVM.
5. Press **`F5`** (Continue / Resume).
6. The withdrawal completes with the corrected logic, updating balance to `$800.00` immediately.

---

## 🛠️ How to Build & Run

### Option 1: Using Apache Maven (Recommended)

From the `DailyTask/Day04` directory:

```powershell
# 1. Clean and compile
mvn clean compile

# 2. Execute the application
mvn exec:java
```

### Option 2: Using Standard `javac` & `java`

From the `DailyTask/Day04` directory:

```powershell
# Compile all Java sources into target/classes
javac -d target/classes src/main/java/com/hcl/bank/model/*.java src/main/java/com/hcl/bank/service/*.java src/main/java/com/hcl/bank/app/*.java

# Run the BankApp main class
java -cp target/classes com.hcl.bank.app.BankApp
```

---

## 📋 Sample Output

```text
=================================================================
    DAY 4: BANK ACCOUNT OOP ARCHITECTURE & DEBUGGING DEMO       
=================================================================

>>> 1. CHAINED CONSTRUCTORS DEMO
Demonstrating 3 constructors chaining via this(...):

Constructor 1 [1 param]  : BankAccount[No=ACC1001, Holder='Alice Smith', Type='Savings', Balance=$0.00]
Constructor 2 [2 params] : BankAccount[No=ACC1002, Holder='Bob Johnson', Type='Savings', Balance=$1500.00]
Constructor 3 [3 params] : BankAccount[No=ACC1003, Holder='Charlie Brown', Type='Current', Balance=$5000.00]

-------------------------------------------------------------
>>> 2. STATIC FIELD & METHOD DEMO
Total BankAccount instances created across application: 3
Notice how account numbers (ACC1001, ACC1002, ACC1003) were auto-sequenced.

-------------------------------------------------------------
>>> 3. INPUT & BUSINESS RULE VALIDATION DEMO

[Test A] Valid Deposit:
   [SUCCESS] Deposit +$750.00 into ACC1001 | New Balance: $750.00

[Test B] Invalid Negative Deposit (should be rejected):
   [FAILED] Deposit amount must be positive: $-200.00

[Test C] Valid Withdrawal:
   [SUCCESS] Withdrawal -$500.00 from ACC1002 | New Balance: $1000.00

[Test D] Overdraft / Insufficient Funds (should be rejected):
   [FAILED] Insufficient balance in ACC1002. Requested: $2000.00, Available: $1000.00

[Test E] Invalid Negative Withdrawal (should be rejected):
   [FAILED] Withdrawal amount must be positive: $-50.00

-------------------------------------------------------------
>>> 4. EQUALS() & HASHCODE() CONTRACT DEMO

acc1.equals(acc2)  -> false (different account numbers)
acc1.equals(accRef) -> true (same account number: ACC1001)
HashSet size with duplicate added: 3 (Expected: 3)
acc1.hashCode() == duplicateAccRef.hashCode() -> true

-------------------------------------------------------------
>>> 5. SERVICE LAYER TRANSFER DEMO

[TRANSFER] Initiating Transfer of $1200.00 from ACC1003 to ACC1001...
   [SUCCESS] Withdrawal -$1200.00 from ACC1003 | New Balance: $3800.00
   [SUCCESS] Deposit +$1200.00 into ACC1001 | New Balance: $1950.00
   [SUCCESS] Transfer Complete! $1200.00 moved from ACC1003 to ACC1001.

-------------------------------------------------------------
>>> 6. DEBUGGING CASE STUDY: PLANTED BUG & HOT CODE REPLACE

Scenario Overview:
------------------
1. THE PLANTED BUG:
   In BankAccount.java, inside the withdraw(double amount) method:
   - Buggy Line : this.balance += amount;  // ACCIDENTALLY CREDITED!
   - Effect     : Withdrawing $200 increased the customer balance instead of deducting.

2. SETTING A CONDITIONAL BREAKPOINT:
   - Breakpoint Location: Inside BankAccount.withdraw() at the balance calculation.
   - Right-click breakpoint -> Edit Condition -> Add expression:
     Condition: amount > 100.0 && accountNumber.equals("ACC1002")
   - Benefit  : The debugger only pauses when this specific high-value transaction runs!

3. USING A WATCH EXPRESSION:
   - Added to WATCH panel: `this.balance`, `amount`, `this.balance - amount`
   - Observed: Before execution balance was $1000.00; line was about to add $200.00!

4. HOT CODE REPLACE (HCR) FIX:
   - While the JVM was paused at the conditional breakpoint:
   - Replaced `this.balance += amount;` with `this.balance -= amount;`
   - Saved the file (Ctrl + S). The IDE compiler hot-swapped the class bytecode.
   - Resumed execution (F5 / Continue).
   - Result: Fixed on-the-fly without killing or restarting the Java process!

Executing verified withdrawal post-fix now:
   [SUCCESS] Withdrawal -$200.00 from ACC1002 | New Balance: $800.00

----------------- Current Bank Accounts -----------------
 * ACC1001    | Alice Smith      | Savings   | Balance: $  1,950.00
 * ACC1002    | Bob Johnson      | Savings   | Balance: $    800.00
 * ACC1003    | Charlie Brown    | Current   | Balance: $  3,800.00
---------------------------------------------------------
```
