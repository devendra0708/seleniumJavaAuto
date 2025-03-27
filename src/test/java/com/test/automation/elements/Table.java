package com.test.automation.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.test.automation.base.BaseElement;
import com.test.automation.utils.Logger;

/**
 * Table element class that extends BaseElement with table-specific functionality
 */
public class Table extends BaseElement {
    private By rowLocator;
    private By columnHeaderLocator;
    private By cellLocator;
    
    /**
     * Constructor for Table with locator
     * @param locator By locator for this table
     */
    public Table(By locator) {
        super(locator);
        // Default locators for standard HTML tables
        this.rowLocator = By.tagName("tr");
        this.columnHeaderLocator = By.tagName("th");
        this.cellLocator = By.tagName("td");
        Logger.debug("Created Table element with locator: " + locator);
    }
    
    /**
     * Constructor for Table with WebElement
     * @param element WebElement instance
     */
    public Table(WebElement element) {
        super(element);
        // Default locators for standard HTML tables
        this.rowLocator = By.tagName("tr");
        this.columnHeaderLocator = By.tagName("th");
        this.cellLocator = By.tagName("td");
        Logger.debug("Created Table element from existing WebElement");
    }
    
    /**
     * Set custom locators for table elements
     * @param rowLocator Locator for table rows
     * @param headerLocator Locator for column headers
     * @param cellLocator Locator for table cells
     */
    public void setLocators(By rowLocator, By headerLocator, By cellLocator) {
        this.rowLocator = rowLocator;
        this.columnHeaderLocator = headerLocator;
        this.cellLocator = cellLocator;
        Logger.debug("Set custom locators for table");
    }
    
    /**
     * Get the number of rows in the table
     * @return Row count
     */
    public int getRowCount() {
        try {
            waitForVisible();
            List<WebElement> rows = getElement().findElements(rowLocator);
            int rowCount = rows.size();
            Logger.debug("Table has " + rowCount + " rows");
            return rowCount;
        } catch (Exception e) {
            Logger.error("Failed to get row count from table", e);
            throw e;
        }
    }
    
    /**
     * Get the number of columns in the table
     * @return Column count
     */
    public int getColumnCount() {
        try {
            waitForVisible();
            List<WebElement> rows = getElement().findElements(rowLocator);
            
            if (rows.isEmpty()) {
                Logger.debug("Table has no rows");
                return 0;
            }
            
            // Try to get column count from headers first
            List<WebElement> headers = rows.get(0).findElements(columnHeaderLocator);
            if (!headers.isEmpty()) {
                Logger.debug("Table has " + headers.size() + " columns (from headers)");
                return headers.size();
            }
            
            // If no headers, get column count from first data row
            List<WebElement> cells = rows.get(0).findElements(cellLocator);
            Logger.debug("Table has " + cells.size() + " columns (from cells)");
            return cells.size();
        } catch (Exception e) {
            Logger.error("Failed to get column count from table", e);
            throw e;
        }
    }
    
    /**
     * Get the column headers as text
     * @return List of column header texts
     */
    public List<String> getColumnHeaders() {
        try {
            waitForVisible();
            List<WebElement> rows = getElement().findElements(rowLocator);
            List<String> headerTexts = new ArrayList<>();
            
            if (rows.isEmpty()) {
                Logger.debug("Table has no rows");
                return headerTexts;
            }
            
            List<WebElement> headers = rows.get(0).findElements(columnHeaderLocator);
            for (WebElement header : headers) {
                headerTexts.add(header.getText().trim());
            }
            
            return headerTexts;
        } catch (Exception e) {
            Logger.error("Failed to get column headers from table", e);
            throw e;
        }
    }
    
    /**
     * Get the text from a specific cell
     * @param rowIndex Zero-based row index
     * @param colIndex Zero-based column index
     * @return Cell text
     */
    public String getCellData(int rowIndex, int colIndex) {
        try {
            waitForVisible();
            List<WebElement> rows = getElement().findElements(rowLocator);
            
            if (rowIndex >= rows.size()) {
                throw new IndexOutOfBoundsException("Row index " + rowIndex + " is out of bounds. Table has " + rows.size() + " rows.");
            }
            
            WebElement row = rows.get(rowIndex);
            List<WebElement> cells = row.findElements(cellLocator);
            
            if (colIndex >= cells.size()) {
                throw new IndexOutOfBoundsException("Column index " + colIndex + " is out of bounds. Row has " + cells.size() + " cells.");
            }
            
            String cellText = cells.get(colIndex).getText().trim();
            Logger.debug("Cell data at [" + rowIndex + ", " + colIndex + "]: " + cellText);
            return cellText;
        } catch (Exception e) {
            Logger.error("Failed to get cell data from table at [" + rowIndex + ", " + colIndex + "]", e);
            throw e;
        }
    }
    
    /**
     * Get all table data as a list of maps
     * @return List of maps where each map represents a row with column header as key
     */
    public List<Map<String, String>> getTableData() {
        try {
            waitForVisible();
            List<Map<String, String>> tableData = new ArrayList<>();
            List<String> headers = getColumnHeaders();
            List<WebElement> rows = getElement().findElements(rowLocator);
            
            // Skip the header row
            for (int i = 1; i < rows.size(); i++) {
                WebElement row = rows.get(i);
                List<WebElement> cells = row.findElements(cellLocator);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < cells.size() && j < headers.size(); j++) {
                    rowData.put(headers.get(j), cells.get(j).getText().trim());
                }
                
                tableData.add(rowData);
            }
            
            return tableData;
        } catch (Exception e) {
            Logger.error("Failed to get table data from table", e);
            throw e;
        }
    }
    
    /**
     * Find a row by text in a specific column
     * @param text Text to search for
     * @param columnIndex Zero-based column index to search in
     * @return Zero-based row index or -1 if not found
     */
    public int findRowByColumnText(String text, int columnIndex) {
        try {
            waitForVisible();
            List<WebElement> rows = getElement().findElements(rowLocator);
            
            // Skip the header row
            for (int i = 1; i < rows.size(); i++) {
                WebElement row = rows.get(i);
                List<WebElement> cells = row.findElements(cellLocator);
                
                if (columnIndex < cells.size() && cells.get(columnIndex).getText().trim().equals(text)) {
                    Logger.debug("Found text '" + text + "' in column " + columnIndex + " at row " + i);
                    return i;
                }
            }
            
            Logger.debug("Text '" + text + "' not found in column " + columnIndex);
            return -1;
        } catch (Exception e) {
            Logger.error("Failed to find row by column text in table", e);
            throw e;
        }
    }
} 