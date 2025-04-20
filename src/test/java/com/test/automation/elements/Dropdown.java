package com.test.automation.elements;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.test.automation.base.BaseElement;
import com.test.automation.utils.Logger;

/**
 * Represents a dropdown/select element on a web page
 */
public class Dropdown extends BaseElement {
    
    private Select select;
    
    /**
     * Constructor for Dropdown element with locator only
     * @param locator By locator to find the element
     */
    public Dropdown(By locator) {
        super(locator);
        Logger.debug("Created Dropdown element with locator: " + locator);
    }
    
    /**
     * Constructor for Dropdown element with existing WebElement
     * @param element WebElement instance
     */
    public Dropdown(WebElement element) {
        super(element);
        Logger.debug("Created Dropdown element from existing WebElement");
    }
    
    /**
     * Initialize the Select object for dropdown operations
     */
    private void initSelect() {
        if (select == null) {
            select = new Select(getElement());
        }
    }
    
    /**
     * Select an option by visible text
     * @param visibleText Text of the option to select
     */
    public void selectByVisibleText(String visibleText) {
        try {
            Logger.info("Selecting option '" + visibleText + "' in dropdown");
            waitForVisible();
            scrollTo();
            initSelect();
            select.selectByVisibleText(visibleText);
            Logger.debug("Selected option '" + visibleText + "' successfully");
        } catch (Exception e) {
            Logger.error("Failed to select option '" + visibleText + "' in dropdown", e);
            throw e;
        }
    }
    
    /**
     * Select an option by value attribute
     * @param value Value attribute of the option to select
     */
    public void selectByValue(String value) {
        try {
            Logger.info("Selecting option with value '" + value + "' in dropdown");
            waitForVisible();
            scrollTo();
            initSelect();
            select.selectByValue(value);
            Logger.debug("Selected option with value '" + value + "' successfully");
        } catch (Exception e) {
            Logger.error("Failed to select option with value '" + value + "' in dropdown", e);
            throw e;
        }
    }
    
    /**
     * Select an option by index
     * @param index Zero-based index of the option to select
     */
    public void selectByIndex(int index) {
        try {
            Logger.info("Selecting option at index " + index + " in dropdown");
            waitForVisible();
            scrollTo();
            initSelect();
            select.selectByIndex(index);
            Logger.debug("Selected option at index " + index + " successfully");
        } catch (Exception e) {
            Logger.error("Failed to select option at index " + index + " in dropdown", e);
            throw e;
        }
    }
    
    /**
     * Get all available options in the dropdown
     * @return List of option texts
     */
    public List<String> getOptions() {
        try {
            Logger.debug("Getting all options from dropdown");
            waitForVisible();
            initSelect();
            List<WebElement> options = select.getOptions();
            List<String> optionTexts = new ArrayList<>();
            
            for (WebElement option : options) {
                optionTexts.add(option.getText());
            }
            
            return optionTexts;
        } catch (Exception e) {
            Logger.error("Failed to get options from dropdown", e);
            throw e;
        }
    }
    
    /**
     * Get the currently selected option text
     * @return Text of the selected option
     */
    public String getSelectedOption() {
        try {
            Logger.debug("Getting selected option from dropdown");
            waitForVisible();
            initSelect();
            return select.getFirstSelectedOption().getText();
        } catch (Exception e) {
            Logger.error("Failed to get selected option from dropdown", e);
            throw e;
        }
    }
    
    /**
     * Check if a specific option exists in the dropdown
     * @param optionText Option text to look for
     * @return true if option exists, false otherwise
     */
    public boolean hasOption(String optionText) {
        List<String> options = getOptions();
        return options.contains(optionText);
    }
    
    /**
     * Get the selected value
     * @return Value attribute of the selected option
     */
    public String getSelectedValue() {
        try {
            Logger.debug("Getting selected value from dropdown");
            waitForVisible();
            initSelect();
            return select.getFirstSelectedOption().getAttribute("value");
        } catch (Exception e) {
            Logger.error("Failed to get selected value from dropdown", e);
            throw e;
        }
    }
    
    /**
     * Get all available option values
     * @return List of option value attributes
     */
    public List<String> getOptionValues() {
        try {
            Logger.debug("Getting all option values from dropdown");
            waitForVisible();
            initSelect();
            List<WebElement> options = select.getOptions();
            List<String> values = new ArrayList<>();
            
            for (WebElement option : options) {
                values.add(option.getAttribute("value"));
            }
            
            return values;
        } catch (Exception e) {
            Logger.error("Failed to get option values from dropdown", e);
            throw e;
        }
    }
    
    /**
     * Check if this is a multi-select dropdown
     * @return true if multiple options can be selected
     */
    public boolean isMultiple() {
        try {
            waitForVisible();
            initSelect();
            return select.isMultiple();
        } catch (Exception e) {
            Logger.error("Failed to check if dropdown is multiple", e);
            throw e;
        }
    }
    
    /**
     * Deselect all selected options (only works for multi-select dropdowns)
     */
    public void deselectAll() {
        initSelect();
        if (isMultiple()) {
            select.deselectAll();
            Logger.debug("Deselected all options");
        } else {
            Logger.warn("Cannot deselect all options in dropdown as it is not a multi-select dropdown");
        }
    }
    
    /**
     * Get the number of options in the dropdown
     * @return Number of options
     */
    public int getOptionCount() {
        int count = select.getOptions().size();
        Logger.debug("Dropdown has " + count + " options");
        return count;
    }
    
    /**
     * Select an option in a custom dropdown (non-standard select element)
     * @param optionText Text of the option to select
     */
    public void selectCustomDropdownOption(String optionText) {
        handleCustomDropdown(optionText);
    }
    
    /**
     * Handle a custom dropdown (non-standard select element)
     * @param optionText Text of the option to select
     */
    private void handleCustomDropdown(String optionText) {
        try {
            // Click to open the dropdown
            getElement().click();
            Logger.debug("Clicked to open custom dropdown");
            
            // Wait for the dropdown options to be visible
            By optionLocator = By.xpath("//div[contains(@class, 'select-options') or contains(@class, 'dropdown')]//li//*[text()='" + optionText + "' or contains(text(), '" + optionText + "')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
            Logger.debug("Options visible in custom dropdown");
            
            // Click on the option
            driver.findElement(optionLocator).click();
            Logger.debug("Selected option in custom dropdown: '" + optionText + "'");
        } catch (Exception e) {
            Logger.error("Failed to handle custom dropdown", e);
            throw e;
        }
    }
} 