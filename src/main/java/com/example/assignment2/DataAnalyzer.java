package com.example.assignment2;

import com.example.util.SimpleLogger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Analyzes sales data using Java Streams.
 */
public class DataAnalyzer {

    private final List<Sale> sales;

    /**
     * Constructs a DataAnalyzer with the given list of sales.
     *
     * @param sales the list of sales to analyze
     */
    public DataAnalyzer(List<Sale> sales) {
        if (sales == null) {
            throw new IllegalArgumentException("Sales list cannot be null");
        }
        this.sales = sales;
    }

    /**
     * Calculates the total sales amount for each category.
     *
     * @return a map where the key is the category and the value is the total sales amount
     */
    public Map<String, BigDecimal> getTotalSalesByCategory() {
        SimpleLogger.log("Grouping sales by category...");
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Sale::getAmount, BigDecimal::add)
                ));
    }

    public BigDecimal getAverageSalesAmount() {
        if (sales.isEmpty()) return BigDecimal.ZERO;
        
        SimpleLogger.log("Calculating average sales...");
        BigDecimal total = sales.stream()
                .map(Sale::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        return total.divide(BigDecimal.valueOf(sales.size()), 2, RoundingMode.HALF_UP);
    }

    public List<String> getTopSellingProducts(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit must be non-negative");
        }
        if (limit == 0) {
            return List.of();
        }
        
        SimpleLogger.log("Identifying top " + limit + " products...");

        Map<String, BigDecimal> productSales = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getProduct,
                        Collectors.reducing(BigDecimal.ZERO, Sale::getAmount, BigDecimal::add)
                ));

        return productSales.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey())) // Deterministic tie-breaking
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getSalesCountByRegion() {
        SimpleLogger.log("Counting sales by region...");
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getRegion,
                        Collectors.counting()
                ));
    }
}
