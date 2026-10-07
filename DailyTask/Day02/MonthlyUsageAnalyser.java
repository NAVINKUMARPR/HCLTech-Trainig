/**
 * Day 2 Task: Monthly Usage Analyser
 * 
 * Concepts demonstrated:
 * 1. int[] (1-D array)
 * 2. final constants (slab thresholds)
 * 3. Total, average, maximum, and minimum calculations
 * 4. Explicit type casting for average
 * 5. Character grade using nested ternary operator
 * 6. Integer overflow prevention using long
 * 7. 2-D array (int[][]) for 3 houses across 12 months
 * 8. Nested loops for matrix processing
 */
public class MonthlyUsageAnalyser {

    public static void main(String[] args) {

        // 1. Slab Constants using 'final' (Immutable values)
        final int SLAB_1 = 100;
        final int SLAB_2 = 300;
        final int SLAB_3 = 500;

        // Month names array for clean display
        String[] months = {
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        };

        // 2. 1-D Array: 12 months usage
        int[] monthlyUsage = {
            120, 250, 180, 320, 450, 510,
            290, 380, 600, 720, 410, 550
        };

        // --- SECTION 1: 12 MONTH USAGE DISPLAY ---
        System.out.println("===== MONTHLY USAGE ANALYSER =====\n");
        System.out.println("----- 12 MONTH USAGE -----");
        for (int i = 0; i < monthlyUsage.length; i++) {
            System.out.printf("%-10s: %d units%n", months[i], monthlyUsage[i]);
        }
        System.out.println();

        // 3. Statistics Calculation with 'long' to prevent integer overflow
        long totalUsage = 0; // Using long prevents overflow if values grow large
        int maxUsage = monthlyUsage[0];
        int minUsage = monthlyUsage[0];

        for (int i = 0; i < monthlyUsage.length; i++) {
            totalUsage += monthlyUsage[i];

            if (monthlyUsage[i] > maxUsage) {
                maxUsage = monthlyUsage[i];
            }
            if (monthlyUsage[i] < minUsage) {
                minUsage = monthlyUsage[i];
            }
        }

        // 4. Explicit Type Casting for accurate floating-point average
        double averageUsage = (double) totalUsage / monthlyUsage.length;

        // 5. Character Grade using Nested Ternary Operator
        char grade = (averageUsage >= SLAB_3) ? 'A' :
                     (averageUsage >= 400)    ? 'B' :
                     (averageUsage >= SLAB_2) ? 'C' :
                     (averageUsage >= 200)    ? 'D' : 'E';

        // --- SECTION 2: OVERALL ANALYSIS ---
        System.out.println("----- OVERALL ANALYSIS -----");
        System.out.println("Total Usage   : " + totalUsage + " units");
        System.out.printf("Average Usage : %.2f units%n", averageUsage);
        System.out.println("Maximum Usage : " + maxUsage + " units");
        System.out.println("Minimum Usage : " + minUsage + " units");
        System.out.println("Grade         : " + grade);
        System.out.println();

        // 6. Integer Overflow Demonstration
        System.out.println("----- INTEGER OVERFLOW DEMONSTRATION -----");
        int maxInt = Integer.MAX_VALUE; // 2,147,483,647
        int overflowResult = maxInt + 100; // Wraps around to negative in 32-bit int
        long safeResult = (long) maxInt + 100L; // Preserved safely in 64-bit long

        System.out.println("Integer.MAX_VALUE            : " + maxInt);
        System.out.println("int overflow (maxInt + 100)  : " + overflowResult + " (Wraps to negative!)");
        System.out.println("long safe    (maxInt + 100)  : " + safeResult + " (Safe)");
        System.out.println();

        // 7. 2-D Array: Usage for 3 houses across 12 months
        int[][] houseUsage = {
            {120, 250, 180, 320, 450, 510, 290, 380, 600, 720, 410, 550},
            {200, 300, 250, 400, 500, 450, 350, 420, 550, 650, 480, 600},
            {100, 180, 220, 280, 350, 400, 300, 360, 500, 580, 390, 450}
        };

        // --- SECTION 3: HOUSE-WISE ANALYSIS USING NESTED LOOPS ---
        System.out.println("----- HOUSE ANALYSIS -----");
        for (int h = 0; h < houseUsage.length; h++) {
            long houseTotal = 0;
            int houseMax = houseUsage[h][0];
            int houseMin = houseUsage[h][0];

            for (int m = 0; m < houseUsage[h].length; m++) {
                int val = houseUsage[h][m];
                houseTotal += val;

                if (val > houseMax) {
                    houseMax = val;
                }
                if (val < houseMin) {
                    houseMin = val;
                }
            }

            double houseAvg = (double) houseTotal / houseUsage[h].length;
            char houseGrade = (houseAvg >= SLAB_3) ? 'A' :
                              (houseAvg >= 400)    ? 'B' :
                              (houseAvg >= SLAB_2) ? 'C' :
                              (houseAvg >= 200)    ? 'D' : 'E';

            System.out.println("House " + (h + 1) + ":");
            System.out.println("  Total   : " + houseTotal + " units");
            System.out.printf("  Average : %.2f units%n", houseAvg);
            System.out.println("  Maximum : " + houseMax + " units");
            System.out.println("  Minimum : " + houseMin + " units");
            System.out.println("  Grade   : " + houseGrade);
            System.out.println();
        }

        System.out.println("==================================");
    }
}
