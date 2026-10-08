# Day 3 task completed


## 📌 Summary
A console-based Java ATM Simulator built as a standard **Apache Maven** project. It showcases control flow structures (`do-while`, `switch`, `break`, `continue`, enhanced-for loop), input validation, and Maven build configurations including **environment profiles (`dev` and `prod`)** with automated resource filtering.

---

## 📁 Project Structure

```text
DailyTask/
└── Day03/
    ├── pom.xml                                    # Maven configuration & profiles
    ├── README.md                                  # Revision guide & instructions
    └── src/
        └── main/
            ├── java/
            │   └── com/
            │       └── hcl/
            │           └── atm/
            │               └── ATMSimulator.java  # Main ATM application
            └── resources/
                └── app.properties                 # Profile-filtered properties
```

---

## 🧠 Key Java & Maven Concepts

| Concept | Explanation | Code Example |
|---|---|---|
| **`do-while` Loop** | Executes the menu body at least once, evaluating the exit condition at the end. | `do { ... } while (choice != 5);` |
| **`switch` Statement** | Routes menu selections cleanly without long `if-else` chains. | `switch (choice) { case 1: ... break; }` |
| **`break` Statement** | Immediately terminates the enclosing loop (e.g., successful PIN login). | `if (pin == CORRECT) { break; }` |
| **`continue` Statement**| Skips remaining code in current iteration and re-runs loop from menu start. | `if (!valid) { continue; }` |
| **Input Validation** | Verifies numeric input, positive amounts, daily limits, and sufficient funds. | `if (amount <= 0) { continue; }` |
| **Enhanced-for Loop** | Cleanly traverses the transaction history list for the mini-statement. | `for (String tx : transactions) { ... }` |
| **Maven Profiles** | Defines environment-specific settings (`dev` vs `prod`) swapped at build time. | `<profiles><profile><id>prod</id>...` |
| **Resource Filtering**| Replaces `${property}` placeholders in `app.properties` during Maven build. | `<filtering>true</filtering>` |

---

## ⚙️ Maven Build Profiles

### 1. `dev` Profile (Default)
- **Environment**: Development (Sandbox)
- **Bank Name**: `HCL Bank (Dev Sandbox)`
- **Daily Withdrawal Limit**: `$100,000.00`

### 2. `prod` Profile
- **Environment**: Production (Live)
- **Bank Name**: `HCL Premier Bank`
- **Daily Withdrawal Limit**: `$25,000.00`

---

## 🛠️ Build & Run Commands

From the `DailyTask/Day03` directory:

### Build with Default Profile (`dev`)
```powershell
mvn clean package
```

### Build with Production Profile (`prod`)
```powershell
mvn clean package -Pprod
```

### Run the Packaged JAR
```powershell
java -jar target/atm-simulator-1.0-SNAPSHOT.jar
```

---

## 🖥️ Sample Console Execution

```text
==================================================
   WELCOME TO HCL BANK (DEV SANDBOX)
   Environment : Development (Sandbox)
   Daily Limit : $100000.00
==================================================

Enter your 4-digit PIN (Attempt 1 of 3): 1234
>>> PIN verified successfully! Access granted.

----------------------------------------
              MAIN MENU                 
----------------------------------------
1. Check Balance
2. Deposit Money
3. Withdraw Money
4. Mini-Statement
5. Exit
Enter your choice (1-5): 1

>>> Current Account Balance: $10000.00

----------------------------------------
              MAIN MENU                 
----------------------------------------
1. Check Balance
2. Deposit Money
3. Withdraw Money
4. Mini-Statement
5. Exit
Enter your choice (1-5): 2

Enter deposit amount: $1500
>>> Successfully deposited: $1500.00
>>> Updated Balance: $11500.00

----------------------------------------
              MAIN MENU                 
----------------------------------------
1. Check Balance
2. Deposit Money
3. Withdraw Money
4. Mini-Statement
5. Exit
Enter your choice (1-5): 3

Enter withdrawal amount: $2000
>>> Successfully withdrawn: $2000.00
>>> Updated Balance: $9500.00

----------------------------------------
              MAIN MENU                 
----------------------------------------
1. Check Balance
2. Deposit Money
3. Withdraw Money
4. Mini-Statement
5. Exit
Enter your choice (1-5): 4

========================================
            MINI-STATEMENT              
========================================
  * Initial Balance : $10000.00
  * Deposited : +$1500.00 | Balance: $11500.00
  * Withdrew  : -$2000.00 | Balance: $9500.00
========================================
Final Available Balance: $9500.00

----------------------------------------
              MAIN MENU                 
----------------------------------------
1. Check Balance
2. Deposit Money
3. Withdraw Money
4. Mini-Statement
5. Exit
Enter your choice (1-5): 5

Thank you for banking with HCL Bank (Dev Sandbox)!
Please take your card. Have a great day!
```

---

## 🚀 Push to GitHub

```powershell
cd d:\HCLTech

# Stage Day 3 files (Maven build output in target/ is automatically ignored)
git add DailyTask/Day03

# Commit
git commit -m "Day 3 - ATM Simulator with Maven Profiles (dev/prod)"

# Push to GitHub
git push origin main
```
