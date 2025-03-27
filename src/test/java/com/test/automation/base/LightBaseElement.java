package com.test.automation.base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.automation.utils.ConfigReader;
import com.test.automation.utils.Logger;
import com.test.automation.utils.WebDriverManager;

import java.time.Duration;
import java.util.List;

/**
 * Lightweight wrapper for WebElement with essential functionality
 * A simplified version of BaseElement that focuses on core functionality
 */
public class LightBaseElement {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected By locator;
    protected WebElement element = null;
    
    /**
     * Constructor with locator
     */
    public LightBaseElement(By locator) {
        this.driver = WebDriverManager.getDriver();
        this.locator = locator;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
            ConfigReader.getIntProperty("explicit.wait")));
    }
    
    /**
     * Constructor with WebElement
     */
    public LightBaseElement(WebElement element) {
        this.driver = WebDriverManager.getDriver();
        this.element = element;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
            ConfigReader.getIntProperty("explicit.wait")));
    }
    
    /**
     * Get the underlying WebElement
     */
    public WebElement getElement() {
        if (element == null && locator != null) {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        }
        return element;
    }
    
    /**
     * Click the element
     */
    public void click() {
        waitForClickable();
        scrollTo();
        getElement().click();
    }
    
    /**
     * Type text into the element
     */
    public void type(String text) {
        waitForVisible();
        getElement().clear();
        getElement().sendKeys(text);
    }
    
    /**
     * Get text from the element
     */
    public String getText() {
        return getElement().getText();
    }
    
    /**
     * Check if the element is displayed
     */
    public boolean isDisplayed() {
        try {
            return getElement().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if the element is enabled
     */
    public boolean isEnabled() {
        try {
            return getElement().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for the element to be visible
     */
    public void waitForVisible() {
        if (locator != null) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } else {
            wait.until(ExpectedConditions.visibilityOf(getElement()));
        }
    }
    
    /**
     * Wait for the element to be clickable
     */
    public void waitForClickable() {
        if (locator != null) {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(getElement()));
        }
    }
    
    /**
     * Scroll to the element
     */
    public void scrollTo() {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'center'});",
            getElement());
    }
    
    /**
     * Get an attribute value
     */
    public String getAttribute(String attribute) {
        return getElement().getAttribute(attribute);
    }
    
    /**
     * Clear the element
     */
    public void clear() {
        getElement().clear();
    }
    
    /**
     * Send keys to the element
     */
    public void sendKeys(CharSequence... keysToSend) {
        getElement().sendKeys(keysToSend);
    }
    
    /**
     * Check if the element is selected
     */
    public boolean isSelected() {
        return getElement().isSelected();
    }
    
    /**
     * Click using JavaScript (useful when regular click doesn't work)
     */
    public void jsClick() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getElement());
    }
    
    /**
     * Hover over the element
     */
    public void hover() {
        Actions actions = new Actions(driver);
        actions.moveToElement(getElement()).perform();
    }
    
    /**
     * Find a child element
     */
    public WebElement findElement(By by) {
        return getElement().findElement(by);
    }
    
    /**
     * Find child elements
     */
    public List<WebElement> findElements(By by) {
        return getElement().findElements(by);
    }
} 