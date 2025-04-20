package com.test.automation.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.test.automation.base.BaseElement;
import com.test.automation.utils.Logger;

/**
 * Represents a checkbox element in the UI
 */
public class Checkbox extends BaseElement {
    
    /**
     * Constructor for Checkbox with locator
     * @param locator By locator for this checkbox
     */
    public Checkbox(By locator) {
        super(locator);
        Logger.debug("Created Checkbox element with locator: " + locator);
    }
    
    /**
     * Constructor for Checkbox with WebElement
     * @param element WebElement instance
     */
    public Checkbox(WebElement element) {
        super(element);
        Logger.debug("Created Checkbox element from existing WebElement");
    }
    
    /**
     * Check if the checkbox is selected
     * @return true if checkbox is selected/checked
     */
    public boolean isChecked() {
        boolean checked = getElement().isSelected();
        Logger.debug("Checkbox is checked: " + checked);
        return checked;
    }
    
    /**
     * Check the checkbox if it's not already checked
     */
    public void check() {
        try {
            Logger.info("Checking checkbox");
            waitForClickable();
            
            if (!isSelected()) {
                scrollTo();
                click();
                Logger.debug("Checkbox is now checked");
            } else {
                Logger.debug("Checkbox was already checked");
            }
        } catch (Exception e) {
            Logger.error("Failed to check checkbox", e);
            throw e;
        }
    }
    
    /**
     * Uncheck the checkbox if it's checked
     */
    public void uncheck() {
        try {
            Logger.info("Unchecking checkbox");
            waitForClickable();
            
            if (isSelected()) {
                scrollTo();
                click();
                Logger.debug("Checkbox is now unchecked");
            } else {
                Logger.debug("Checkbox was already unchecked");
            }
        } catch (Exception e) {
            Logger.error("Failed to uncheck checkbox", e);
            throw e;
        }
    }
    
    /**
     * Set the checkbox to the specified state
     * @param check true to check, false to uncheck
     */
    public void setChecked(boolean check) {
        if (check) {
            check();
        } else {
            uncheck();
        }
    }
    
    /**
     * Toggle the checkbox state
     */
    public void toggle() {
        try {
            Logger.info("Toggling checkbox");
            waitForClickable();
            scrollTo();
            click();
            Logger.debug("Checkbox state toggled");
        } catch (Exception e) {
            Logger.error("Failed to toggle checkbox", e);
            throw e;
        }
    }
    
    /**
     * Check the checkbox using JavaScript
     */
    public void jsCheck() {
        WebElement checkbox = getElement();
        if (!checkbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            Logger.debug("JavaScript checked");
        }
    }
    
    /**
     * Set the state of a Salesforce checkbox
     * @param check true to check, false to uncheck
     */
    public void setSalesforceCheckbox(boolean check) {
        try {
            WebElement checkbox = getElement();
            boolean isChecked = checkbox.isSelected();
            
            if (check != isChecked) {
                checkbox.click();
                Logger.debug((check ? "Checked" : "Unchecked") + " Salesforce checkbox");
            } else {
                Logger.debug("Salesforce checkbox already in desired state (checked: " + check + ")");
            }
        } catch (Exception e) {
            // Try with JavaScript as a fallback
            Logger.warn("Using JavaScript fallback for Salesforce checkbox");
            WebElement checkbox = getElement();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            Logger.debug("JavaScript " + (check ? "checked" : "unchecked") + " Salesforce checkbox");
        }
    }
} 