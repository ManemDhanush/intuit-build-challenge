package com.example.assignment2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class DataAnalyzerTest {

    private DataAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        List<Sale> sales = Arrays.asList(
            new Sale(LocalDate.now(), "A", "Cat1", new BigDecimal("100.00"), "North"),
            new Sale(LocalDate.now(), "B", "Cat1", new BigDecimal("200.00"), "South"),
            new Sale(LocalDate.now(), "C", "Cat2", new BigDecimal("300.00"), "North"),
            new Sale(LocalDate.now(), "A", "Cat1", new BigDecimal("50.00"), "East"),
            new Sale(LocalDate.now(), "D", "Cat3", new BigDecimal("300.00"), "West") // Tie with C
        );
        analyzer = new DataAnalyzer(sales);
    }

    @Test
    void testGetTotalSalesByCategory() {
        Map<String, BigDecimal> result = analyzer.getTotalSalesByCategory();
        assertEquals(new BigDecimal("350.00"), result.get("Cat1"));
        assertEquals(new BigDecimal("300.00"), result.get("Cat2"));
    }

    @Test
    void testGetAverageSalesAmount() {
        assertEquals(new BigDecimal("190.00"), analyzer.getAverageSalesAmount());
    }

    @Test
    void testGetTopSellingProducts() {
        // C: 300, D: 300, B: 200, A: 150
        // Tie-breaking: C vs D. Alphabetical C comes before D.
        List<String> top = analyzer.getTopSellingProducts(4);
        assertEquals("C", top.get(0)); 
        assertEquals("D", top.get(1)); 
        assertEquals("B", top.get(2)); 
        assertEquals("A", top.get(3));
    }
    
    @Test
    void testGetTopSellingProductsLimit() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getTopSellingProducts(-1));
        assertTrue(analyzer.getTopSellingProducts(0).isEmpty());
    }

    @Test
    void testGetSalesCountByRegion() {
        Map<String, Long> result = analyzer.getSalesCountByRegion();
        assertEquals(2L, result.get("North"));
        assertEquals(1L, result.get("South"));
        assertEquals(1L, result.get("East"));
    }

    @Test
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new DataAnalyzer(null));
    }

    @Test
    void testEmptyInput() {
        DataAnalyzer emptyAnalyzer = new DataAnalyzer(List.of());
        assertEquals(BigDecimal.ZERO, emptyAnalyzer.getAverageSalesAmount());
        assertTrue(emptyAnalyzer.getTotalSalesByCategory().isEmpty());
        assertTrue(emptyAnalyzer.getTopSellingProducts(5).isEmpty());
    }
}
