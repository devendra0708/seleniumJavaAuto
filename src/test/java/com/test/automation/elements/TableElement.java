package com.test.automation.elements;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.test.automation.base.BaseElement;

public class TableElement extends BaseElement {

    private String thCss = "thead th";
    private String tbodyCss = "tbody";
    private String trCss = "tr";
    private int startRowIndex = 1;

    /**
     * Constructor for TableElement with WebElement
     * @param element WebElement instance
     */
    public TableElement(WebElement element) {
        super(element);
    }

    /**
     * Constructor for TableElement with By locator
     * @param locator By locator for the table
     */
    public TableElement(By locator) {
        super(locator);
    }

    /**
     * Get a header element by its text content
     * @param headerName Text content of the header
     * @return BaseElement representing the header
     */
    public BaseElement getHeaderElementByText(String headerName) {
        int index = getColumnIndex(headerName);
        return new BaseElement(findElement(By.cssSelector(getThCss(index))));
    }

    /**
     * Get a header element by its text content with additional CSS
     * @param headerName Text content of the header
     * @param css Additional CSS selector
     * @return BaseElement representing the header
     */
    public BaseElement getHeaderElementByText(String headerName, String css) {
        int index = getColumnIndex(headerName);
        return new BaseElement(findElement(By.cssSelector(getThCss(index) + css)));
    }

    /**
     * Get a header element by its class name
     * @param className Class name to search for
     * @return BaseElement representing the header
     */
    public BaseElement getHeaderElementByClass(String className) {
        return new BaseElement(findElement(By.className(className)));
    }

    /**
     * Get a cell element by row and column number
     * @param rowNum Row number (1-based)
     * @param colNum Column number (1-based)
     * @return BaseElement representing the cell
     */
    public BaseElement getCellElement(int rowNum, int colNum) {
        return getCellElement(rowNum, colNum, "");
    }

    /**
     * Get a cell element by row and column number with additional CSS
     * @param rowNum Row number (1-based)
     * @param colNum Column number (1-based)
     * @param css Additional CSS selector
     * @return BaseElement representing the cell
     */
    public BaseElement getCellElement(int rowNum, int colNum, String css) {
        return new BaseElement(findElement(By.cssSelector(getTdCss(rowNum, colNum, css))));
    }

    /**
     * Get a cell element by column header text and row number
     * @param rowNum Row number (1-based)
     * @param columnHeaderText Text content of the column header
     * @return BaseElement representing the cell
     */
    public BaseElement getCellElementByColumnHeader(int rowNum, String columnHeaderText) {
        int colNum = getColumnIndex(columnHeaderText);
        return getCellElement(rowNum, colNum);
    }

    /**
     * Get a cell element by column header text and row number with additional CSS
     * @param rowNum Row number (1-based)
     * @param columnHeaderText Text content of the column header
     * @param css Additional CSS selector
     * @return BaseElement representing the cell
     */
    public BaseElement getCellElementByColumnHeader(int rowNum, String columnHeaderText, String css) {
        int colNum = getColumnIndex(columnHeaderText);
        return getCellElement(rowNum, colNum, css);
    }

    /**
     * Get all values in a column by its header text
     * @param columnHeaderText Text content of the column header
     * @return List of cell values in the column
     */
    public List<String> getColumnValuesByColumnHeader(String columnHeaderText) {
        return getColumnValuesByColumnHeader(columnHeaderText, "");
    }

    /**
     * Get all values in a column by its header text with additional CSS
     * @param columnHeaderText Text content of the column header
     * @param childCss Additional CSS selector
     * @return List of cell values in the column
     */
    public List<String> getColumnValuesByColumnHeader(String columnHeaderText, String childCss) {
        int rowCount = getTotalRowCount();
        List<String> allCellElementsValue = new ArrayList<>();

        for (int i = startRowIndex; i <= rowCount; i++) {
            BaseElement cellElement = getCellElementByColumnHeader(i, columnHeaderText, childCss);
            if (cellElement.isDisplayed()) {
                allCellElementsValue.add(cellElement.getText().trim());
            }
        }
        return allCellElementsValue;
    }

    /**
     * Get the index of a column by its header text
     * @param columnHeaderText Text content of the column header
     * @return Column index (1-based)
     */
    public int getColumnIndex(String columnHeaderText) {
        List<WebElement> headers = findElements(By.cssSelector(getThCss()));
        int index = getElementIndexFromHeaderList(headers, columnHeaderText, 1);
        if (index == -1) {
            throw new ColumnHeaderNotFoundException("Column header '" + columnHeaderText + "' is not present in table");
        }
        return index;
    }

    /**
     * Get the row index for a cell value in a specific column
     * @param columnHeaderText Text content of the column header
     * @param cellValue Value to search for
     * @param css Additional CSS selector
     * @return Row index (1-based)
     */
    public int getRowIndex(String columnHeaderText, String cellValue, String css) {
        int columnIndex = getColumnIndex(columnHeaderText);
        List<WebElement> columnValues = findElements(By.cssSelector(getTdCssForColumn(columnIndex, css)));

        int index = getElementIndexFromList(columnValues, cellValue, startRowIndex);
        if (index == -1) {
            throw new ColumnValueNotFoundException(
                    "Value '" + cellValue + "' is not present in column '" + columnHeaderText + "'.");
        }
        return index;
    }

    /**
     * Get the total number of columns in the table
     * @return Number of columns
     */
    public int getTotalColumnsCount() {
        return findElements(By.cssSelector(getThCss())).size();
    }

    /**
     * Get the total number of rows in the table
     * @return Number of rows
     */
    public int getTotalRowCount() {
        return findElements(By.cssSelector(getTrCss())).size();
    }

    /**
     * Check if a cell with given value exists in a column
     * @param columnHeaderText Text content of the column header
     * @param cellValue Value to search for
     * @return true if the cell exists
     */
    public boolean isCellPresent(String columnHeaderText, String cellValue) {
        return isCellPresent(columnHeaderText, cellValue, "");
    }

    /**
     * Check if a cell with given value exists in a column with additional CSS
     * @param columnHeaderText Text content of the column header
     * @param cellValue Value to search for
     * @param css Additional CSS selector
     * @return true if the cell exists
     */
    public boolean isCellPresent(String columnHeaderText, String cellValue, String css) {
        try {
            getRowIndex(columnHeaderText, cellValue, css);
            return true;
        } catch (ColumnValueNotFoundException e) {
            return false;
        }
    }

    // Protected helper methods for CSS selectors
    protected String getThCss() {
        return thCss;
    }

    protected String getThCss(int colNum) {
        return getThCss() + ":nth-child(" + colNum + ")";
    }

    protected String getTBodyCss() {
        return tbodyCss;
    }

    protected String getTrCss() {
        return getTBodyCss() + ">" + trCss;
    }

    protected String getTdCss() {
        return getTrCss() + ">td";
    }

    protected String getTdCss(int rowNum, int colNum) {
        return getTrCss() + ":nth-child(" + rowNum + ")>td:nth-child(" + colNum + ")";
    }

    protected String getTdCss(int rowNum, int colNum, String css) {
        return getTdCss(rowNum, colNum) + css;
    }

    protected String getTdCssForColumn(int colNum, String css) {
        return getTdCss() + ":nth-child(" + colNum + ")" + css;
    }

    // Private helper methods
    private int getElementIndexFromHeaderList(List<WebElement> list, String elementText, int startIndex) {
        int rowIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            scrollIntoView(list.get(i));
            int increment = 1;
            String colSpan = list.get(i).getAttribute("colspan");
            if (colSpan != null) {
                try {
                    increment = Integer.parseInt(colSpan);
                } catch (Exception e) {
                    increment = 1;
                }
            }
            if (list.get(i).getText().trim().equalsIgnoreCase(elementText)) {
                return rowIndex + startIndex;
            }
            rowIndex = rowIndex + increment;
        }
        return -1;
    }

    private int getElementIndexFromList(List<WebElement> list, String elementText, int startIndex) {
        for (int i = 0; i < list.size(); i++) {
            scrollIntoView(list.get(i));
            if (list.get(i).getText().trim().equalsIgnoreCase(elementText)) {
                return i + startIndex;
            }
        }
        return -1;
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    // Custom exceptions
    public static class ColumnHeaderNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ColumnHeaderNotFoundException(String message) {
            super(message);
        }
    }

    public static class ColumnValueNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ColumnValueNotFoundException(String message) {
            super(message);
        }
    }

    // Setter methods for CSS selectors
    public void setTbodyCss(String tbodyCss) {
        this.tbodyCss = tbodyCss;
    }

    public void setTrCss(String trCss) {
        this.trCss = trCss;
    }

    public void setThCss(String thCss) {
        this.thCss = thCss;
    }

    public void setStartRowIndex(int startRowIndex) {
        this.startRowIndex = startRowIndex;
    }
} 