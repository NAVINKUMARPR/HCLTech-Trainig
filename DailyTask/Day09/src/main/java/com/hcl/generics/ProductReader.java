package com.hcl.generics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductReader Class
 *
 * Demonstrates reading products.csv using try-with-resources.
 * The BufferedReader is declared inside the try(...) clause and is automatically closed.
 */
public class ProductReader {

    /**
     * Reads products.csv from the classpath or relative filesystem path using try-with-resources.
     *
     * @return List of parsed Product objects
     */
    public static List<Product> readProducts(String filename) {
        List<Product> products = new ArrayList<>();

        // Try-with-resources using classpath resource stream first
        InputStream is = ProductReader.class.getClassLoader().getResourceAsStream(filename);
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                parseLines(br, products);
                return products;
            } catch (IOException e) {
                System.err.println("Error reading products from classpath: " + e.getMessage());
            }
        }

        // Fallback: Read from local filesystem paths (handles direct javac/IDE execution)
        File fallbackFile = new File(filename);
        if (!fallbackFile.exists()) {
            fallbackFile = new File("src/main/resources/" + filename);
        }

        if (fallbackFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fallbackFile))) {
                parseLines(br, products);
            } catch (IOException e) {
                System.err.println("Error reading products file from disk: " + e.getMessage());
            }
        }

        return products;
    }

    private static void parseLines(BufferedReader br, List<Product> products) throws IOException {
        String line;
        boolean isHeader = true;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (isHeader) {
                isHeader = false; // Skip CSV header row (id,name,category,price,stock)
                continue;
            }

            String[] tokens = line.split(",");
            if (tokens.length >= 5) {
                int id = Integer.parseInt(tokens[0].trim());
                String name = tokens[1].trim();
                String category = tokens[2].trim();
                double price = Double.parseDouble(tokens[3].trim());
                int stock = Integer.parseInt(tokens[4].trim());

                products.add(new Product(id, name, category, price, stock));
            }
        }
    }
}
