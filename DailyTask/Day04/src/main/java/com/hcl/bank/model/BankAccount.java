package com.hcl.bank.model;

import java.util.Objects;

/**
 * BankAccount Model Class
 * Represents a bank account with encapsulated fields, static counter,
 * chained constructors, validated transactions, and equals/hashCode overrides.
 */
public class BankAccount {

    // ==========================================
    // 1. Static Field (Class-level counter)
    // ==========================================
    private static int totalAccountsCreated = 0;

    // ==========================================
    // 2. Private Fields (Encapsulation)
    // ==========================================
    private final String accountNumber;
    private String accountHolder;
    private double balance;
    private String accountType;

    // ==========================================
    // 3. Chained Constructors (using this(...))
    // ==========================================

    /**
     * Constructor 1: Takes only account holder name.
     * Chains to Constructor 2 with a default balance of 0.0.
     *
     * @param accountHolder Name of the account holder
     */
    public BankAccount(String accountHolder) {
        this(accountHolder, 0.0);
    }

    /**
     * Constructor 2: Takes account holder name and initial balance.
     * Chains to Constructor 3 with a default account type "Savings".
     *
     * @param accountHolder  Name of the account holder
     * @param initialBalance Initial deposit amount
     */
    public BankAccount(String accountHolder, double initialBalance) {
        this(accountHolder, initialBalance, "Savings");
    }

    /**
     * Constructor 3 (Master Constructor):
     * Validates initial balance, increments static counter,
     * auto-generates a unique account number, and initializes all fields.
     *
     * @param accountHolder  Name of the account holder
     * @param initialBalance Initial deposit amount (must be >= 0)
     * @param accountType    Type of account (e.g., Savings, Current)
     */
    public BankAccount(String accountHolder, double initialBalance, String accountType) {
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty.");
        }
        if (initialBalance < 0.0) {
            throw new IllegalArgumentException("Initial balance cannot be negative: $" + initialBalance);
        }

        // Increment static counter upon every successful creation
        totalAccountsCreated++;

        // Auto-generate account number: e.g., ACC1001, ACC1002, ...
        this.accountNumber = "ACC" + (1000 + totalAccountsCreated);
        this.accountHolder = accountHolder.trim();
        this.balance = initialBalance;
        this.accountType = (accountType != null && !accountType.trim().isEmpty()) ? accountType.trim() : "Savings";
    }

    // ==========================================
    // 4. Validated Transaction Methods
    // ==========================================

    /**
     * Validated Deposit Method.
     * Ensures deposit amount is strictly positive before modifying balance.
     *
     * @param amount Amount to deposit
     * @return true if successful, false otherwise
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.printf("   [FAILED] Deposit amount must be positive: $%.2f%n", amount);
            return false;
        }
        this.balance += amount;
        System.out.printf("   [SUCCESS] Deposit +$%.2f into %s | New Balance: $%.2f%n", 
                amount, this.accountNumber, this.balance);
        return true;
    }

    /**
     * Validated Withdraw Method.
     * Ensures withdrawal amount is positive and does not exceed current balance.
     *
     * DEBUGGING NOTE:
     * - Planted Bug was: `this.balance += amount;` (or inverted check),
     *   which erroneously increased the balance during withdrawal!
     * - Fixed using Hot Code Replace: `this.balance -= amount;`
     *
     * @param amount Amount to withdraw
     * @return true if withdrawal succeeded, false otherwise
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.printf("   [FAILED] Withdrawal amount must be positive: $%.2f%n", amount);
            return false;
        }
        if (amount > this.balance) {
            System.out.printf("   [FAILED] Insufficient balance in %s. Requested: $%.2f, Available: $%.2f%n",
                    this.accountNumber, amount, this.balance);
            return false;
        }

        // =========================================================================
        // [BUG FIXED VIA HOT CODE REPLACE]
        // Previously: this.balance += amount; (Planted Bug: credited instead of debited)
        // Corrected:  this.balance -= amount; (Correct debit logic)
        // =========================================================================
        this.balance -= amount;

        System.out.printf("   [SUCCESS] Withdrawal -$%.2f from %s | New Balance: $%.2f%n",
                amount, this.accountNumber, this.balance);
        return true;
    }

    // ==========================================
    // 5. equals, hashCode, and toString
    // ==========================================

    /**
     * Two bank accounts are considered equal if and only if they share
     * the exact same unique account number.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(this.accountNumber, that.accountNumber);
    }

    /**
     * Consistent with equals: uses accountNumber to generate hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return String.format("BankAccount[No=%s, Holder='%s', Type='%s', Balance=$%.2f]",
                accountNumber, accountHolder, accountType, balance);
    }

    // ==========================================
    // 6. Getters & Setters
    // ==========================================

    public static int getTotalAccountsCreated() {
        return totalAccountsCreated;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        if (accountHolder != null && !accountHolder.trim().isEmpty()) {
            this.accountHolder = accountHolder.trim();
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        if (accountType != null && !accountType.trim().isEmpty()) {
            this.accountType = accountType.trim();
        }
    }
}
