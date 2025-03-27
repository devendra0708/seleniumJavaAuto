package com.test.automation.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import com.test.automation.base.BaseElement;
import com.test.automation.utils.Logger;

/**
 * Input element class that extends BaseElement with input field specific functionality
 */
public class Input extends BaseElement {
    
    /**
     * Constructor for Input element with locator only
     * @param locator By locator to find the element
     */
    public Input(By locator) {
        super(locator);
        Logger.debug("Created Input element with locator: " + locator);
    }
    
    /**
     * Constructor for Input element with existing WebElement
     * @param element WebElement instance
     */
    public Input(WebElement element) {
        super(element);
        Logger.debug("Created Input element from existing WebElement");
    }
    
    /**
     * Type text into the input field with automatic clearing
     * @param text Text to type into the field
     */
    public void type(String text) {
        try {
            Logger.info("Typing '" + text + "' into input field");
            waitForVisible();
            scrollTo();
            clear();
            super.sendKeys(text);
        } catch (Exception e) {
            Logger.error("Failed to type text into input field", e);
            throw e;
        }
    }
    
    /**
     * Type text without clearing previous content
     * @param text Text to append to the field
     */
    public void appendText(String text) {
        try {
            Logger.info("Appending '" + text + "' to input field");
            waitForVisible();
            scrollTo();
            super.sendKeys(text);
        } catch (Exception e) {
            Logger.error("Failed to append text to input field", e);
            throw e;
        }
    }
    
    /**
     * Press Enter key in the input field
     */
    public void pressEnter() {
        try {
            Logger.info("Pressing Enter key in input field");
            waitForVisible();
            super.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            Logger.error("Failed to press Enter key in input field", e);
            throw e;
        }
    }
    
    /**
     * Press Tab key in the input field
     */
    public void pressTab() {
        try {
            Logger.info("Pressing Tab key in input field");
            waitForVisible();
            super.sendKeys(Keys.TAB);
        } catch (Exception e) {
            Logger.error("Failed to press Tab key in input field", e);
            throw e;
        }
    }
    
    /**
     * Enter text into the input field
     * @param text Text to enter
     */
    public void enterText(String text) {
        try {
            Logger.info("Entering text: '" + text + "' into input field");
            WebElement element = getElement();
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            Logger.error("Failed to enter text into input field", e);
            throw e;
        }
    }
    
    /**
     * Clear the input field
     */
    @Override
    public void clear() {
        try {
            Logger.info("Clearing input field");
            waitForVisible();
            super.clear();
        } catch (Exception e) {
            Logger.error("Failed to clear input field", e);
            throw e;
        }
    }
    
    /**
     * Get the current value of the input field
     * @return current value of the input field
     */
    public String getValue() {
        try {
            String value = getAttribute("value");
            Logger.debug("Input field value: " + value);
            return value;
        } catch (Exception e) {
            Logger.error("Failed to get input field value", e);
            throw e;
        }
    }
    
    /**
     * Enter text and press Enter
     * @param text Text to enter
     */
    public void enterTextAndSubmit(String text) {
        try {
            Logger.info("Entering text: '" + text + "' and pressing Enter");
            WebElement element = getElement();
            element.clear();
            element.sendKeys(text);
            element.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            Logger.error("Failed to enter text and submit", e);
            throw e;
        }
    }
    
    /**
     * Check if the input field is read-only
     * @return true if the input field is read-only
     */
    public boolean isReadOnly() {
        try {
            String readOnly = getAttribute("readonly");
            boolean isReadOnly = readOnly != null;
            Logger.debug("Input field is read-only: " + isReadOnly);
            return isReadOnly;
        } catch (Exception e) {
            Logger.error("Failed to check if input field is read-only", e);
            throw e;
        }
    }
    
    /**
     * Type text into the field without clearing it first
     * @param text Text to append to the field
     */
    public void typeWithoutClear(String text) {
        try {
            Logger.info("Typing '" + text + "' into input field without clearing");
            waitForVisible();
            scrollTo();
            super.sendKeys(text);
        } catch (Exception e) {
            Logger.error("Failed to type text into input field without clearing", e);
            throw e;
        }
    }
    
    /**
     * Set the value of the input field using JavaScript
     * Useful for fields that have event listeners that might interfere with normal typing
     * @param value Value to set
     */
    public void setValueJS(String value) {
        try {
            Logger.info("Setting input field value to '" + value + "' using JavaScript");
            waitForVisible();
            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1]", getElement(), value);
        } catch (Exception e) {
            Logger.error("Failed to set input field value using JavaScript", e);
            throw e;
        }
    }
    
    /**
     * Check if the input field is empty
     * @return true if the field is empty
     */
    public boolean isEmpty() {
        try {
            String value = getValue();
            boolean isEmpty = value == null || value.isEmpty();
            Logger.debug("Input field is empty: " + isEmpty);
            return isEmpty;
        } catch (Exception e) {
            Logger.error("Failed to check if input field is empty", e);
            throw e;
        }
    }
} 