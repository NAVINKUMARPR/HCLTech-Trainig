package com.hcl.generics;

/**
 * CustomResource Class
 *
 * Implements java.lang.AutoCloseable to demonstrate resource lifecycle and close order
 * inside try-with-resources statements.
 */
public class CustomResource implements AutoCloseable {

    private final String resourceName;

    public CustomResource(String resourceName) {
        this.resourceName = resourceName;
        System.out.println("   [INIT] Acquired/Opened: " + resourceName);
    }

    public void doWork() {
        System.out.println("   [WORK] Processing business logic using " + resourceName);
    }

    /**
     * Called automatically by try-with-resources.
     * Note: Resources are closed in REVERSE order of their declaration (LIFO order).
     */
    @Override
    public void close() {
        System.out.println("   [CLOSE] Released/Closed: " + resourceName);
    }
}
