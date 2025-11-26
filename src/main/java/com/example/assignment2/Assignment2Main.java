package com.example.assignment2;

import com.example.util.SimpleLogger;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Assignment2Main {
    public static void main(String[] args) {
        SimpleLogger.log("Starting Sales Data Analysis...");
        DataLoader loader = new DataLoader();
        try {
            SimpleLogger.log("Loading data from sales_data.csv...");
            List<Sale> sales = loader.loadSalesData("sales_data.csv");
            SimpleLogger.log("Loaded " + sales.size() + " sales records.");
            
            DataAnalyzer analyzer = new DataAnalyzer(sales);

            SimpleLogger.log("1. Calculating Total Sales by Category...");
            Map<String, BigDecimal> salesByCategory = analyzer.getTotalSalesByCategory();
            salesByCategory.forEach((category, total) -> 
                SimpleLogger.log(String.format("   %s: $%.2f", category, total)));

            SimpleLogger.log("2. Calculating Average Sales Amount...");
            SimpleLogger.log(String.format("   Average: $%.2f", analyzer.getAverageSalesAmount()));

            SimpleLogger.log("3. Finding Top 3 Selling Products...");
            List<String> topProducts = analyzer.getTopSellingProducts(3);
            topProducts.forEach(product -> SimpleLogger.log("   - " + product));

            SimpleLogger.log("4. Counting Sales by Region...");
            Map<String, Long> salesByRegion = analyzer.getSalesCountByRegion();
            salesByRegion.forEach((region, count) -> 
                SimpleLogger.log(String.format("   %s: %d", region, count)));
                
            SimpleLogger.log("Analysis Complete.");

        } catch (IOException e) {
            SimpleLogger.error("Failed to load data: " + e.getMessage());
        }
    }
}
