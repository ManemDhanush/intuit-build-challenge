package com.example.assignment2;

import com.example.util.SimpleLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading sales data from CSV files.
 */
public class DataLoader {

    /**
     * Loads sales data from a CSV file in the classpath.
     * Skips the header line and any malformed lines.
     *
     * @param filename the name of the file to load
     * @return a list of Sale objects
     * @throws IOException if the file cannot be found or read
     */
    public List<Sale> loadSalesData(String filename) throws IOException {
        List<Sale> sales = new ArrayList<>();
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            throw new IOException("File not found: " + filename);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            boolean firstLine = true;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                
                if (line.trim().isEmpty()) continue;

                try {
                    // Simple CSV split handling quotes not required for this specific dataset but good practice
                    // For now, assuming standard CSV without internal commas in fields based on problem description
                    // but adding trim() for robustness.
                    String[] parts = line.split(",");
                    
                    if (parts.length == 5) {
                        LocalDate date = LocalDate.parse(parts[0].trim());
                        String product = parts[1].trim();
                        String category = parts[2].trim();
                        BigDecimal amount = new BigDecimal(parts[3].trim());
                        String region = parts[4].trim();
                        sales.add(new Sale(date, product, category, amount, region));
                    } else {
                        SimpleLogger.error("Skipping malformed line " + lineNumber + ": " + line);
                    }
                } catch (DateTimeParseException | NumberFormatException e) {
                    SimpleLogger.error("Error parsing line " + lineNumber + ": " + line + " - " + e.getMessage());
                }
            }
        }
        return sales;
    }
}
