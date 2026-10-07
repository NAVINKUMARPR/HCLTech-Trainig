public class PlatformInfo {
    public static void main(String[] args) {
        // 1. Java Version & Operating System
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");

        // 2. Runtime Information (Processors & Heap Memory)
        Runtime runtime = Runtime.getRuntime();
        int processors = runtime.availableProcessors();
        long maxMemory = runtime.maxMemory();   // Maximum heap memory in bytes
        long freeMemory = runtime.freeMemory(); // Free heap memory in bytes

        // Display Platform Information
        System.out.println("========================================");
        System.out.println("         Platform & JVM Info            ");
        System.out.println("========================================");
        System.out.println("Java Version       : " + javaVersion);
        System.out.println("OS Name            : " + osName);
        System.out.println("Available Processors: " + processors);
        System.out.println("Max Heap Memory    : " + (maxMemory / (1024 * 1024)) + " MB (" + maxMemory + " bytes)");
        System.out.println("Free Heap Memory   : " + (freeMemory / (1024 * 1024)) + " MB (" + freeMemory + " bytes)");
        System.out.println("========================================");
    }
}
