package com.hcl.bank.service;

import com.hcl.bank.model.BankAccount;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BankService Class
 * Encapsulates banking operations and account management business logic.
 */
public class BankService {

    private final Map<String, BankAccount> accountRepository = new LinkedHashMap<>();

    /**
     * Registers a new account using 1-argument constructor.
     */
    public BankAccount openAccount(String holderName) {
        BankAccount account = new BankAccount(holderName);
        accountRepository.put(account.getAccountNumber(), account);
        return account;
    }

    /**
     * Registers a new account using 2-argument constructor.
     */
    public BankAccount openAccount(String holderName, double initialBalance) {
        BankAccount account = new BankAccount(holderName, initialBalance);
        accountRepository.put(account.getAccountNumber(), account);
        return account;
    }

    /**
     * Registers a new account using 3-argument constructor.
     */
    public BankAccount openAccount(String holderName, double initialBalance, String accountType) {
        BankAccount account = new BankAccount(holderName, initialBalance, accountType);
        accountRepository.put(account.getAccountNumber(), account);
        return account;
    }

    /**
     * Finds an account by account number.
     */
    public BankAccount findAccount(String accountNumber) {
        return accountRepository.get(accountNumber);
    }

    /**
     * Performs a validated deposit into the specified account.
     */
    public boolean deposit(String accountNumber, double amount) {
        BankAccount account = findAccount(accountNumber);
        if (account == null) {
            System.out.printf("   [FAILED] Account %s not found.%n", accountNumber);
            return false;
        }
        return account.deposit(amount);
    }

    /**
     * Performs a validated withdrawal from the specified account.
     */
    public boolean withdraw(String accountNumber, double amount) {
        BankAccount account = findAccount(accountNumber);
        if (account == null) {
            System.out.printf("   [FAILED] Account %s not found.%n", accountNumber);
            return false;
        }
        return account.withdraw(amount);
    }

    /**
     * Transfers money between two accounts with validation.
     */
    public boolean transfer(String fromAccNum, String toAccNum, double amount) {
        System.out.printf("[TRANSFER] Initiating Transfer of $%.2f from %s to %s...%n", amount, fromAccNum, toAccNum);
        BankAccount fromAcc = findAccount(fromAccNum);
        BankAccount toAcc = findAccount(toAccNum);

        if (fromAcc == null) {
            System.out.printf("   [FAILED] Source account %s not found.%n", fromAccNum);
            return false;
        }
        if (toAcc == null) {
            System.out.printf("   [FAILED] Destination account %s not found.%n", toAccNum);
            return false;
        }
        if (fromAcc.equals(toAcc)) {
            System.out.println("   [FAILED] Cannot transfer funds to the same account.");
            return false;
        }

        // Withdraw from source, then deposit into destination if successful
        if (fromAcc.withdraw(amount)) {
            toAcc.deposit(amount);
            System.out.printf("   [SUCCESS] Transfer Complete! $%.2f moved from %s to %s.%n", 
                    amount, fromAccNum, toAccNum);
            return true;
        } else {
            System.out.println("   [FAILED] Transfer aborted due to withdrawal failure.");
            return false;
        }
    }

    /**
     * Returns an unmodifiable view of all active accounts.
     */
    public Collection<BankAccount> getAllAccounts() {
        return Collections.unmodifiableCollection(accountRepository.values());
    }

    /**
     * Displays summary of all registered accounts.
     */
    public void printAllAccounts() {
        System.out.println("\n----------------- Current Bank Accounts -----------------");
        if (accountRepository.isEmpty()) {
            System.out.println("No accounts registered yet.");
        } else {
            for (BankAccount acc : accountRepository.values()) {
                System.out.printf(" * %-10s | %-16s | %-9s | Balance: $%,10.2f%n",
                        acc.getAccountNumber(),
                        acc.getAccountHolder(),
                        acc.getAccountType(),
                        acc.getBalance());
            }
        }
        System.out.println("---------------------------------------------------------");
    }
}
