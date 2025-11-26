package com.example.assignment2;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a single sale record.
 */
public class Sale {
    private LocalDate date;
    private String product;
    private String category;
    private BigDecimal amount;
    private String region;

    public Sale(LocalDate date, String product, String category, BigDecimal amount, String region) {
        this.date = date;
        this.product = product;
        this.category = category;
        this.amount = amount;
        this.region = region;
    }

    public LocalDate getDate() { return date; }
    public String getProduct() { return product; }
    public String getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public String getRegion() { return region; }

    @Override
    public String toString() {
        return "Sale{" +
                "date=" + date +
                ", product='" + product + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", region='" + region + '\'' +
                '}';
    }
}
