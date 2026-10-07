package com.hcl.atm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Day 3 Task: ATM Simulator
 * 
 * Concepts Demonstrated:
 * 1. do-while loop for interactive menu
 * 2. switch statement for menu routing
 * 3. 3 PIN authentication attempts with break
 * 4. continue on invalid entries / failed validations
 * 5. Input validation (positive amounts, funds check, daily limits)
 * 6. Enhanced-for loop (for-each) for mini-statement display
 * 7. Maven build & resource filtering (dev/prod profiles)
 */
public class ATMSimulator {

    private static final int CORRECT_PIN = 1234;
    private static double balance = 10000.00;
    private static final List<String> transactions = new ArrayList<>();

    // Configured via app.properties (filtered by Maven Profiles: dev/prod)
    private static String environment = "Development (Sandbox)";
    private static String bankName = "HCL Bank (Dev Sandbox)";
    private static double dailyLimit = 100000.0;

    public static void main(String[] args) {
        loadProperties();

        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("   WELCOME TO " + bankName.toUpperCase());
        System.out.println("   Environment : " + environment);
        System.out.println("   Daily Limit : $" + String.format("%.2f", dailyLimit));
        System.out.println("==================================================\n");

        // Initial transaction record
        transactions.add(String.format("Initial Balance : $%.2f", balance));

        // ----------------------------------------------------
        // CONCEPT 1: 3 PIN Attempts with 'break'
        // ----------------------------------------------------
        boolean isAuthenticated = false;
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.print("Enter your 4-digit PIN (Attempt " + attempt + " of " + maxAttempts + "): ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! PIN must be a numeric 4-digit code.\n");
                if (scanner.hasNext()) scanner.next(); // Clear invalid token
                continue;
            }

            int enteredPin = scanner.nextInt();
            if (enteredPin == CORRECT_PIN) {
                isAuthenticated = true;
                System.out.println(">>> PIN verified successfully! Access granted.\n");
                break; // Break early from PIN attempts on success
            } else {
                int remaining = maxAttempts - attempt;
                if (remaining > 0) {
                    System.out.println("Incorrect PIN! Attempts remaining: " + remaining + "\n");
                } else {
                    System.out.println(">>> Alert: 3 incorrect attempts. Card locked for security!");
                    System.out.println("Please visit your nearest bank branch to unlock your card.");
                    return; // Terminate execution
                }
            }
        }

        if (!isAuthenticated) {
            return;
        }

        // ----------------------------------------------------
        // CONCEPT 2: do-while Loop Menu + switch Statement
        // ----------------------------------------------------
        int choice;
        do {
            System.out.println("----------------------------------------");
            System.out.println("              MAIN MENU                 ");
            System.out.println("----------------------------------------");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Mini-Statement");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            // Input Validation: Check if input is a valid integer
            if (!scanner.hasNextInt()) {
                System.out.println("Error: Invalid choice! Please enter a number between 1 and 5.\n");
                if (scanner.hasNext()) scanner.next(); // Clear invalid input
                choice = -1;
                continue; // CONCEPT 3: 'continue' skips rest of loop and reprompts menu
            }

            choice = scanner.nextInt();

            // Menu selection routing via switch statement
            switch (choice) {
                case 1:
                    // Check Balance
                    System.out.println("\n>>> Current Account Balance: $" + String.format("%.2f", balance) + "\n");
                    break;

                case 2:
                    // Deposit Money with input validation
                    System.out.print("\nEnter deposit amount: $");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Error: Deposit amount must be numeric!\n");
                        if (scanner.hasNext()) scanner.next();
                        continue; // Return to menu
                    }
                    double deposit = scanner.nextDouble();

                    // Validation: Positive amount
                    if (deposit <= 0) {
                        System.out.println("Error: Deposit amount must be greater than $0.00!\n");
                        continue;
                    }

                    balance += deposit;
                    String depRecord = String.format("Deposited : +$%.2f | Balance: $%.2f", deposit, balance);
                    transactions.add(depRecord);
                    System.out.println(">>> Successfully deposited: $" + String.format("%.2f", deposit));
                    System.out.println(">>> Updated Balance: $" + String.format("%.2f", balance) + "\n");
                    break;

                case 3:
                    // Withdraw Money with input validation
                    System.out.print("\nEnter withdrawal amount: $");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Error: Withdrawal amount must be numeric!\n");
                        if (scanner.hasNext()) scanner.next();
                        continue;
                    }
                    double withdrawal = scanner.nextDouble();

                    // Validation 1: Positive amount
                    if (withdrawal <= 0) {
                        System.out.println("Error: Withdrawal amount must be greater than $0.00!\n");
                        continue;
                    }

                    // Validation 2: Daily Limit check
                    if (withdrawal > dailyLimit) {
                        System.out.printf("Error: Amount exceeds daily limit of $%.2f!%n%n", dailyLimit);
                        continue;
                    }

                    // Validation 3: Sufficient funds check
                    if (withdrawal > balance) {
                        System.out.printf("Error: Insufficient funds! Current balance is $%.2f%n%n", balance);
                        continue;
                    }

                    balance -= withdrawal;
                    String withRecord = String.format("Withdrew  : -$%.2f | Balance: $%.2f", withdrawal, balance);
                    transactions.add(withRecord);
                    System.out.println(">>> Successfully withdrawn: $" + String.format("%.2f", withdrawal));
                    System.out.println(">>> Updated Balance: $" + String.format("%.2f", balance) + "\n");
                    break;

                case 4:
                    // CONCEPT 4: Enhanced-for loop (for-each) for Mini-Statement
                    System.out.println("\n========================================");
                    System.out.println("            MINI-STATEMENT              ");
                    System.out.println("========================================");
                    for (String tx : transactions) {
                        System.out.println("  * " + tx);
                    }
                    System.out.println("========================================");
                    System.out.printf("Final Available Balance: $%.2f%n%n", balance);
                    break;

                case 5:
                    // Exit
                    System.out.println("\nThank you for banking with " + bankName + "!");
                    System.out.println("Please take your card. Have a great day!\n");
                    break;

                default:
                    // Out-of-range option
                    System.out.println("Error: Invalid option (" + choice + "). Choose between 1 and 5.\n");
                    break;
            }

        } while (choice != 5);

        scanner.close();
    }

    /**
     * Loads environment properties filtered by Maven build profiles.
     */
    private static void loadProperties() {
        try (InputStream is = ATMSimulator.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                environment = props.getProperty("app.environment", environment);
                bankName = props.getProperty("app.bank.name", bankName);
                String limitStr = props.getProperty("app.daily.limit");
                if (limitStr != null && !limitStr.isEmpty() && !limitStr.startsWith("${")) {
                    dailyLimit = Double.parseDouble(limitStr);
                }
            }
        } catch (Exception e) {
            // Sensible defaults retained if properties cannot be loaded
        }
    }
}
