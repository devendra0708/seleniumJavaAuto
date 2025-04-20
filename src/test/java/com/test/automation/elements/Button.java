package com.test.automation.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.test.automation.base.BaseElement;
import com.test.automation.utils.Logger;

/**
 * Button element class that extends BaseElement with button-specific functionality
 */
public class Button extends BaseElement {
    
    /**
     * Constructor for Button element with locator and name
     * @param locator By locator to find the element
     * @param name Descriptive name for logging
     */
    public Button(By locator) {
        super(locator);
        Logger.debug("Created Button element with locator: " + locator);
    }
    
    /**
     * Constructor for Button element with existing WebElement
     * @param element WebElement instance
     */
    public Button(WebElement element) {
        super(element);
        Logger.debug("Created Button element from existing WebElement");
    }
    
    /**
     * Click the button with special handling for AJAX-enabled buttons
     * Waits for the button to be clickable before performing the action
     */
    @Override
    public void click() {
        try {
            Logger.info("Clicking button");
            waitForClickable();
            scrollTo();
            super.click();
        } catch (Exception e) {
            Logger.error("Failed to click button", e);
            throw e;
        }
    }
    
    /**
     * Click the button using JavaScript
     * Useful for buttons that are not responding to normal clicks
     */
    @Override
    public void jsClick() {
        try {
            Logger.info("Clicking button using JavaScript");
            waitForVisible();
            super.jsClick();
        } catch (Exception e) {
            Logger.error("Failed to click button using JavaScript", e);
            throw e;
        }
    }
    
    /**
     * Check if the button is enabled
     * @return true if the button is enabled
     */
    @Override
    public boolean isEnabled() {
        try {
            boolean enabled = super.isEnabled();
            Logger.debug("Button is enabled: " + enabled);
            return enabled;
        } catch (Exception e) {
            Logger.error("Failed to check if button is enabled", e);
            throw e;
        }
    }
    
    /**
     * Check if the button has the specified class
     * @param className Class name to check for
     * @return true if the button has the specified class
     */
    public boolean hasClass(String className) {
        return super.hasClass(className);
    }
} 