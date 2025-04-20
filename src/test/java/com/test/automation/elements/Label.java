package com.test.automation.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.test.automation.base.BaseElement;
import com.test.automation.utils.Logger;

/**
 * Label element class that extends BaseElement with label-specific functionality
 */
public class Label extends BaseElement {
    
    /**
     * Constructor for Label element with locator only
     * @param locator By locator to find the element
     */
    public Label(By locator) {
        super(locator);
        Logger.debug("Created Label element with locator: " + locator);
    }
    
    /**
     * Constructor for Label element with existing WebElement
     * @param element WebElement instance
     */
    public Label(WebElement element) {
        super(element);
        Logger.debug("Created Label element from existing WebElement");
    }
    
    /**
     * Get the text content of the label with special handling
     * @return Text content of the label
     */
    @Override
    public String getText() {
        try {
            Logger.debug("Getting text from label");
            waitForVisible();
            String text = super.getText();
            Logger.debug("Label text: " + text);
            return text;
        } catch (Exception e) {
            Logger.error("Failed to get text from label", e);
            throw e;
        }
    }
    
    /**
     * Check if label matches expected text
     * @param expectedText Text to compare against
     * @return true if the label text matches
     */
    public boolean textMatches(String expectedText) {
        try {
            String actualText = getText();
            boolean matches = actualText.equals(expectedText);
            Logger.debug("Label text match check: '" + actualText + "' vs '" + 
                         expectedText + "' - " + matches);
            return matches;
        } catch (Exception e) {
            Logger.error("Failed to check if label text matches: '" + expectedText + "'", e);
            throw e;
        }
    }
    
    /**
     * Check if label contains expected text
     * @param expectedText Text to check for
     * @return true if the label text contains the expected text
     */
    public boolean textContains(String expectedText) {
        try {
            String actualText = getText();
            boolean contains = actualText.contains(expectedText);
            Logger.debug("Label text contains check: '" + actualText + "' contains '" + 
                         expectedText + "' - " + contains);
            return contains;
        } catch (Exception e) {
            Logger.error("Failed to check if label text contains: '" + expectedText + "'", e);
            throw e;
        }
    }
    
    /**
     * Get the 'for' attribute of the label
     * @return Value of the 'for' attribute
     */
    public String getForAttribute() {
        try {
            Logger.debug("Getting 'for' attribute from label");
            String forAttr = getAttribute("for");
            Logger.debug("Label 'for' attribute: " + forAttr);
            return forAttr;
        } catch (Exception e) {
            Logger.error("Failed to get 'for' attribute from label", e);
            throw e;
        }
    }
    
    /**
     * Check if the label is associated with a specific element ID
     * @param elementId ID of the element to check
     * @return true if the label is associated with the element
     */
    public boolean isAssociatedWithElement(String elementId) {
        try {
            Logger.debug("Checking if label is associated with element: " + elementId);
            String forAttr = getForAttribute();
            boolean isAssociated = forAttr != null && forAttr.equals(elementId);
            Logger.debug("Label is associated with element " + elementId + ": " + isAssociated);
            return isAssociated;
        } catch (Exception e) {
            Logger.error("Failed to check if label is associated with element: " + elementId, e);
            throw e;
        }
    }
    
    /**
     * Get the associated input element
     * @return BaseElement representing the associated input
     */
    public BaseElement getAssociatedElement() {
        try {
            Logger.debug("Getting associated element for label");
            String forAttr = getForAttribute();
            if (forAttr == null || forAttr.isEmpty()) {
                Logger.debug("Label has no 'for' attribute");
                return null;
            }
            
            WebElement element = driver.findElement(By.id(forAttr));
            Logger.debug("Found associated element with ID: " + forAttr);
            return new BaseElement(element);
        } catch (Exception e) {
            Logger.error("Failed to get associated element for label", e);
            throw e;
        }
    }
} 