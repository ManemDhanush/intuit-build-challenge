package com.example.assignment2;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataLoaderTest {

    @Test
    void testLoadSalesData_ValidFile() throws IOException {
        DataLoader loader = new DataLoader();
        List<Sale> sales = loader.loadSalesData("sales_data.csv");
        assertFalse(sales.isEmpty(), "Should load sales data");
        assertEquals(70, sales.size(), "Should load 70 records");
    }

    @Test
    void testLoadSalesData_FileNotFound() {
        DataLoader loader = new DataLoader();
        assertThrows(IOException.class, () -> loader.loadSalesData("non_existent.csv"));
    }

    @Test
    void testLoadSalesData_BadData() throws IOException {
        DataLoader loader = new DataLoader();
        // bad_data.csv has 7 lines total:
        // 1 header
        // 3 valid lines (1, 6, 7)
        // 3 invalid lines (2: date, 3: amount, 5: missing col, 6: extra col - wait, let's count carefully)
        // Line 1: Header
        // Line 2: Valid
        // Line 3: Invalid Date
        // Line 4: Invalid Amount
        // Line 5: Missing Region (split length 4)
        // Line 6: Empty/Comma only
        // Line 7: Extra Column (split length 6)
        // Line 8: Valid
        
        // My file content:
        // 1: Header
        // 2: Valid
        // 3: Invalid Date
        // 4: Invalid Amount
        // 5: Missing Region (4 cols)
        // 6: Empty/Comma
        // 7: Extra Column
        // 8: Valid
        
        // So expected valid records: 2
        
        List<Sale> sales = loader.loadSalesData("bad_data.csv");
        assertEquals(2, sales.size(), "Should load only valid records from bad_data.csv");
    }
}
