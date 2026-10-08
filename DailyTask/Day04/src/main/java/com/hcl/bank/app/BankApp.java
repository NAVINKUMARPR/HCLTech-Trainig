package com.hcl.bank.app;

import com.hcl.bank.model.BankAccount;
import com.hcl.bank.service.BankService;

import java.util.HashSet;
import java.util.Set;

/**
 * BankApp - Main Demonstration Runner
 * Demonstrates:
 * 1. Chained constructors (this(...))
 * 2. Static counter tracking
 * 3. Deposit & withdrawal validations
 * 4. equals() and hashCode() contract
 * 5. Debugging planted bug in withdraw() using Conditional Breakpoint, Watch, & Hot Code Replace
 */
public class BankApp {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("    DAY 4: BANK ACCOUNT OOP ARCHITECTURE & DEBUGGING DEMO       ");
        System.out.println("=================================================================\n");

        BankService bankService = new BankService();

        // -------------------------------------------------------------
        // 1. Chained Constructors Demo
        // -------------------------------------------------------------
        System.out.println(">>> 1. CHAINED CONSTRUCTORS DEMO");
        System.out.println("Demonstrating 3 constructors chaining via this(...):\n");

        // 1-arg constructor -> chains to 2-arg -> chains to 3-arg (defaults: balance=0.0, type="Savings")
        BankAccount acc1 = bankService.openAccount("Alice Smith");
        System.out.printf("Constructor 1 [1 param]  : %s%n", acc1);

        // 2-arg constructor -> chains to 3-arg (default: type="Savings")
        BankAccount acc2 = bankService.openAccount("Bob Johnson", 1500.0);
        System.out.printf("Constructor 2 [2 params] : %s%n", acc2);

        // 3-arg constructor (master constructor with custom type and balance)
        BankAccount acc3 = bankService.openAccount("Charlie Brown", 5000.0, "Current");
        System.out.printf("Constructor 3 [3 params] : %s%n", acc3);

        System.out.println("\n-------------------------------------------------------------");

        // -------------------------------------------------------------
        // 2. Static Counter Tracking Demo
        // -------------------------------------------------------------
        System.out.println(">>> 2. STATIC FIELD & METHOD DEMO");
        System.out.printf("Total BankAccount instances created across application: %d%n",
                BankAccount.getTotalAccountsCreated());
        System.out.println("Notice how account numbers (ACC1001, ACC1002, ACC1003) were auto-sequenced.");

        System.out.println("\n-------------------------------------------------------------");

        // -------------------------------------------------------------
        // 3. Validated Deposit & Withdrawal Demo
        // -------------------------------------------------------------
        System.out.println(">>> 3. INPUT & BUSINESS RULE VALIDATION DEMO\n");

        System.out.println("[Test A] Valid Deposit:");
        bankService.deposit(acc1.getAccountNumber(), 750.0);

        System.out.println("\n[Test B] Invalid Negative Deposit (should be rejected):");
        bankService.deposit(acc1.getAccountNumber(), -200.0);

        System.out.println("\n[Test C] Valid Withdrawal:");
        bankService.withdraw(acc2.getAccountNumber(), 500.0);

        System.out.println("\n[Test D] Overdraft / Insufficient Funds (should be rejected):");
        bankService.withdraw(acc2.getAccountNumber(), 2000.0);

        System.out.println("\n[Test E] Invalid Negative Withdrawal (should be rejected):");
        bankService.withdraw(acc2.getAccountNumber(), -50.0);

        System.out.println("\n-------------------------------------------------------------");

        // -------------------------------------------------------------
        // 4. equals() and hashCode() Contract Demo
        // -------------------------------------------------------------
        System.out.println(">>> 4. EQUALS() & HASHCODE() CONTRACT DEMO\n");

        // Compare two distinct accounts
        System.out.printf("acc1.equals(acc2)  -> %b (different account numbers)%n", acc1.equals(acc2));

        // Create reference pointing to same logical account number
        BankAccount duplicateAccRef = bankService.findAccount(acc1.getAccountNumber());
        System.out.printf("acc1.equals(accRef) -> %b (same account number: %s)%n",
                acc1.equals(duplicateAccRef), acc1.getAccountNumber());

        // Verify Set uniqueness (HashSet relies on both hashCode and equals)
        Set<BankAccount> accountSet = new HashSet<>();
        accountSet.add(acc1);
        accountSet.add(acc2);
        accountSet.add(acc3);
        accountSet.add(duplicateAccRef); // Duplicate reference

        System.out.printf("HashSet size with duplicate added: %d (Expected: 3)%n", accountSet.size());
        System.out.printf("acc1.hashCode() == duplicateAccRef.hashCode() -> %b%n",
                acc1.hashCode() == duplicateAccRef.hashCode());

        System.out.println("\n-------------------------------------------------------------");

        // -------------------------------------------------------------
        // 5. Inter-Account Transfer Demo (Service Layer)
        // -------------------------------------------------------------
        System.out.println(">>> 5. SERVICE LAYER TRANSFER DEMO\n");
        bankService.transfer(acc3.getAccountNumber(), acc1.getAccountNumber(), 1200.0);

        System.out.println("\n-------------------------------------------------------------");

        // -------------------------------------------------------------
        // 6. Debugging Planted Bug & Hot Code Replace (HCR) Walkthrough
        // -------------------------------------------------------------
        System.out.println(">>> 6. DEBUGGING CASE STUDY: PLANTED BUG & HOT CODE REPLACE\n");
        explainDebuggingScenario(bankService, acc2);

        // Final Accounts Summary
        bankService.printAllAccounts();

        System.out.println("\n=================================================================");
        System.out.println("        DAY 4 DEMONSTRATION EXECUTED SUCCESSFULLY!               ");
        System.out.println("=================================================================");
    }

    /**
     * Interactive console walkthrough describing the debugging case study.
     */
    private static void explainDebuggingScenario(BankService service, BankAccount testAccount) {
        System.out.println("Scenario Overview:");
        System.out.println("------------------");
        System.out.println("1. THE PLANTED BUG:");
        System.out.println("   In BankAccount.java, inside the withdraw(double amount) method:");
        System.out.println("   - Buggy Line : this.balance += amount;  // ACCIDENTALLY CREDITED!");
        System.out.println("   - Effect     : Withdrawing $200 increased the customer balance instead of deducting.");
        System.out.println();
        System.out.println("2. SETTING A CONDITIONAL BREAKPOINT:");
        System.out.println("   - Breakpoint Location: Inside BankAccount.withdraw() at the balance calculation.");
        System.out.println("   - Right-click breakpoint -> Edit Condition -> Add expression:");
        System.out.println("     Condition: amount > 100.0 && accountNumber.equals(\"" + testAccount.getAccountNumber() + "\")");
        System.out.println("   - Benefit  : The debugger only pauses when this specific high-value transaction runs!");
        System.out.println();
        System.out.println("3. USING A WATCH EXPRESSION:");
        System.out.println("   - Added to WATCH panel: `this.balance`, `amount`, `this.balance - amount`");
        System.out.println("   - Observed: Before execution balance was $1000.00; line was about to add $200.00!");
        System.out.println();
        System.out.println("4. HOT CODE REPLACE (HCR) FIX:");
        System.out.println("   - While the JVM was paused at the conditional breakpoint:");
        System.out.println("   - Replaced `this.balance += amount;` with `this.balance -= amount;`");
        System.out.println("   - Saved the file (Ctrl + S). The IDE compiler hot-swapped the class bytecode.");
        System.out.println("   - Resumed execution (F5 / Continue).");
        System.out.println("   - Result: Fixed on-the-fly without killing or restarting the Java process!");
        System.out.println();
        System.out.println("Executing verified withdrawal post-fix now:");
        service.withdraw(testAccount.getAccountNumber(), 200.0);
    }
}
