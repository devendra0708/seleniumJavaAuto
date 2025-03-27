package com.test.automation.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.automation.utils.ConfigReader;
import com.test.automation.utils.Logger;
import com.test.automation.utils.WaitUtils;
import com.test.automation.utils.WebDriverManager;

import java.time.Duration;

/**
 * Base class for all Page Objects in the framework
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String pageName;
    protected WaitUtils waitUtils;
    
    /**
     * Constructor for BasePage
     */
    public BasePage() {
        this.pageName = this.getClass().getSimpleName();
        this.driver = WebDriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
        this.waitUtils = new WaitUtils(driver);
        
        Logger.debug("Initializing " + pageName);
        initialize();
        Logger.debug(pageName + " initialized");
    }
    
    /**
     * Initialize the page
     * Override this method to initialize page elements and perform any necessary setup
     */
    protected void initialize() {
        initLocators();
        initializeElements();
    }

    /**
     * Initialize locators for the page
     * This method must be implemented by each page class to initialize locators
     */
    protected abstract void initLocators();
    
    /**
     * Initialize elements using the locators
     * Override this method in page classes to initialize web elements
     */
    protected void initializeElements() {
        // Default implementation does nothing
        // Override in page classes to initialize elements
    }
    
    /**
     * Check if the page is loaded correctly
     * @return true if page is loaded correctly
     */
    public abstract boolean isPageLoaded();
    
    /**
     * Navigate to a URL
     * @param url URL to navigate to
     */
    protected void navigateTo(String url) {
        try {
            Logger.debug("Navigating to: " + url);
            driver.get(url);
            waitUtils.waitForPageToLoad();
        } catch (Exception e) {
            Logger.error("Failed to navigate to: " + url, e);
            throw e;
        }
    }
    
    /**
     * Get the current URL
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get the page title
     * @return Page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Refresh the page
     */
    protected void refreshPage() {
        try {
            Logger.debug("Refreshing page");
            driver.navigate().refresh();
            waitUtils.waitForPageToLoad();
        } catch (Exception e) {
            Logger.error("Failed to refresh page", e);
            throw e;
        }
    }
    
    /**
     * Execute JavaScript
     * @param script JavaScript to execute
     * @param args Arguments for the script
     * @return Result of the script execution
     */
    protected Object executeJavaScript(String script, Object... args) {
        try {
            return ((JavascriptExecutor) driver).executeScript(script, args);
        } catch (Exception e) {
            Logger.error("Failed to execute JavaScript", e);
            throw e;
        }
    }
    
    /**
     * Take a screenshot
     * @return Screenshot as byte array
     */
    protected byte[] takeScreenshot() {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            Logger.error("Failed to take screenshot", e);
            throw e;
        }
    }
} 