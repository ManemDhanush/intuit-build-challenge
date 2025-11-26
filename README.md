# Java Coding Assignments

This repository contains robust, production-ready solutions for two Java coding assignments: a **Producer-Consumer** simulation and a **Data Analysis** tool.

## Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher

## Quick Start

1.  **Build the Project**:
    This command compiles the code and runs all unit tests.
    ```bash
    mvn clean package
    ```

2.  **Run Assignment 1 (Producer-Consumer)**:
    ```bash
    mvn exec:java -Dexec.mainClass="com.example.assignment1.Assignment1Main"
    ```

3.  **Run Assignment 2 (Data Analysis)**:
    ```bash
    mvn exec:java -Dexec.mainClass="com.example.assignment2.Assignment2Main"
    ```

---

## Assignment 1: Producer-Consumer Pattern

A multi-threaded simulation demonstrating thread safety and synchronization.

### Key Features
- **BoundedBuffer**: A thread-safe queue implemented using `wait()` and `notifyAll()`.
- **Graceful Shutdown**: Implements a "poison pill" mechanism to ensure all threads exit cleanly without deadlocks.
- **Decoupling**: Uses `SourceContainer` and `DestinationContainer` interfaces to separate data logic from threading logic.
- **Robustness**: Handles interrupts and prevents operations on a shutdown buffer.

### Edge Cases Handled
- **Invalid Capacity**: Prevents creating buffers with zero or negative size.
- **Immediate Shutdown**: Handles shutdown requests even before any data is produced.
- **Interruption**: Threads waiting on full/empty buffers handle interrupts correctly.

---

## Assignment 2: Data Analysis

A data processing tool that analyzes sales records from a CSV file using Java Streams.

### Key Features
- **Java Streams**: Utilizes functional programming for concise and readable data transformations.
- **Financial Precision**: Uses `BigDecimal` for all monetary calculations to avoid floating-point errors.
- **Robust CSV Parsing**: The `DataLoader` is resilient to malformed data, skipping invalid lines (bad dates, non-numeric amounts) while logging errors instead of crashing.
- **Deterministic Results**: Sorting logic handles ties consistently (e.g., sorting by name if sales are equal).

### Edge Cases Handled
- **Bad Data**: Successfully processes files with mixed valid/invalid lines, missing columns, or extra columns.
- **Null/Empty Inputs**: `DataAnalyzer` guards against null inputs and handles empty datasets gracefully.
- **Input Validation**: Validates parameters (e.g., negative limits for top products).

---

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── com/example/assignment1/  # Producer-Consumer classes
│   │   ├── com/example/assignment2/  # Data Analysis classes
│   │   └── com/example/util/         # Shared utilities (SimpleLogger)
│   └── resources/
│       └── sales_data.csv            # Dataset (70+ records)
└── test/
    ├── java/                         # Unit tests (JUnit 5)
    └── resources/                    # Test data (bad_data.csv)
```

## Logging

A custom `SimpleLogger` utility is used across the project to provide thread-safe, timestamped logs for better debugging and visibility.
