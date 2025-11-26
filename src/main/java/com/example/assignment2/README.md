# Assignment 2: Data Analysis Tool

## Overview
This assignment implements a data analysis tool that processes sales records from a CSV file. The goal was to demonstrate modern Java practices, specifically the Streams API for functional data processing, and robust error handling for file I/O.

## Concepts Used
1.  **Java Streams API**: Used for declarative data processing (filtering, mapping, reducing, grouping). This makes the code concise and readable compared to traditional loops.
2.  **Financial Precision**: Used `java.math.BigDecimal` for all monetary values (`Amount`). Floating-point types (`double`) can introduce rounding errors, which is unacceptable for financial data.
3.  **Robust I/O**: The CSV parsing logic is designed to be fault-tolerant. It doesn't crash on bad data; it skips and logs it.
4.  **Immutable Data Models**: The `Sale` class is designed to be a simple data carrier.

## Implementation Approach
1.  **DataLoader**:
    *   Reads the CSV file line by line.
    *   **Robustness**: It manually parses lines to handle edge cases like missing columns or whitespace. It wraps parsing logic in `try-catch` blocks to ensure one bad line doesn't stop the entire load process.
2.  **DataAnalyzer**:
    *   Takes a list of `Sale` objects.
    *   **Analysis**: Uses `Collectors.groupingBy`, `Collectors.reducing`, and `Collectors.counting` to perform aggregations (Total Sales by Category, Average Sales, Top Products).
    *   **Deterministic Sorting**: When finding top products, it uses a secondary sort key (Product Name) to ensure the order is always the same, even if sales amounts are tied.

## Testing Strategy (`DataLoaderTest` & `DataAnalyzerTest`)
We focused on testing both the "Happy Path" and "Unhappy Paths":
*   **Data Loading**:
    *   **Valid File**: Checks that a standard CSV is loaded correctly.
    *   **Bad Data**: A dedicated `bad_data.csv` (with invalid dates, text in number fields, extra columns) is used to prove that the loader skips garbage and keeps valid records.
    *   **Missing File**: Verifies that `IOException` is thrown for non-existent files.
*   **Data Analysis**:
    *   **Correctness**: Verifies math for totals and averages.
    *   **Edge Cases**:
        *   **Null/Empty Inputs**: Ensures the analyzer handles empty lists without crashing (returning 0 or empty maps).
        *   **Negative Limits**: Ensures `getTopSellingProducts` throws exception for invalid arguments.
