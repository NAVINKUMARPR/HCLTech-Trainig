# Day 2: Monthly Usage Analyser

## 📌 Summary
A simple Java console program that analyses electricity/utility usage data over 12 months for single and multiple households.

---

## 📁 Files in Day 2
- `MonthlyUsageAnalyser.java` - Java source code
- `MonthlyUsageAnalyser.class` - Compiled bytecode
- `README.md` - Revision guide and Git instructions

---

## 🧠 Key Concepts for Revision

| Concept | Explanation | Example |
|---|---|---|
| **`int[]`** | Fixed-size collection of integer elements stored contiguously in memory. | `int[] usage = {120, 250, 180};` |
| **`final` Constants** | Variables that cannot be modified after assignment. Written in uppercase. | `final int SLAB_1 = 100;` |
| **Type Casting** | Explicitly converts types so integer division doesn't drop decimal digits. | `(double) total / count` |
| **Ternary Operator** | Inline shorthand for `if-else` (`condition ? valueIfTrue : valueIfFalse`). | `(avg >= 500) ? 'A' : 'B'` |
| **`long` & Overflow** | `int` max is ~2.14 billion. Adding more wraps to negative. `long` prevents this. | `long total = 0;` |
| **2-D Array (`int[][]`)** | An array of arrays (matrix) where rows = houses and columns = months. | `int[][] houses = { {..}, {..} };` |
| **Nested Loops** | An outer loop to iterate houses and an inner loop to iterate months. | `for (int h...) { for (int m...) }` |

---

## 🛠️ CLI Commands (No IDE)

```powershell
# Navigate to folder
cd d:\HCLTech\DailyTask\Day02

# Compile
javac MonthlyUsageAnalyser.java

# Run
java MonthlyUsageAnalyser
```

---

## 🖥️ Expected Output

```text
===== MONTHLY USAGE ANALYSER =====

----- 12 MONTH USAGE -----
January   : 120 units
February  : 250 units
...
December  : 550 units

----- OVERALL ANALYSIS -----
Total Usage   : 4780 units
Average Usage : 398.33 units
Maximum Usage : 720 units
Minimum Usage : 120 units
Grade         : C

----- INTEGER OVERFLOW DEMONSTRATION -----
Integer.MAX_VALUE            : 2147483647
int overflow (maxInt + 100)  : -2147483549 (Wraps to negative!)
long safe    (maxInt + 100)  : 2147483747 (Safe)

----- HOUSE ANALYSIS -----
House 1: Total: 4780 | Avg: 398.33 | Max: 720 | Min: 120 | Grade: C
House 2: Total: 5150 | Avg: 429.17 | Max: 650 | Min: 200 | Grade: B
House 3: Total: 4110 | Avg: 342.50 | Max: 580 | Min: 100 | Grade: C
```

---

## 🚀 Push to GitHub

You can commit and push directly from the repository root:

```powershell
cd d:\HCLTech

# Stage Day 2 files
git add DailyTask/Day02

# Commit
git commit -m "Day 2 - Monthly Usage Analyser"

# Push to your GitHub repository
git push origin main
```
