package com.test.automation.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.automation.utils.ConfigReader;
import com.test.automation.utils.WaitUtils;
import com.test.automation.utils.WebDriverManager;

import java.time.Duration;

/**
 * Lightweight base class for all Page Objects
 * A simplified version of BasePage that focuses on core functionality
 */
public abstract class LightBasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String pageName;
    protected WaitUtils waitUtils;
    
    /**
     * Constructor for LightBasePage
     */
    public LightBasePage() {
        // Set the page name to the class name
        this.pageName = this.getClass().getSimpleName();
        
        // Get the WebDriver instance
        this.driver = WebDriverManager.getDriver();
        
        // Set up wait utilities
        int waitTimeout = ConfigReader.getIntProperty("explicit.wait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeout));
        this.waitUtils = new WaitUtils(driver);
        
        // Initialize the page
        initialize();
    }
    
    /**
     * Initialize the page
     * This method can be overridden to customize the initialization sequence
     */
    protected void initialize() {
        // Initialize locators
        initLocators();
    }
    
    /**
     * Initialize locators
     * This method must be implemented by each page class
     */
    protected abstract void initLocators();
    
    /**
     * Check if the page is loaded
     * This method must be implemented by each page class
     */
    public abstract boolean isPageLoaded();
    
    /**
     * Navigate to a URL
     */
    protected void navigateTo(String url) {
        driver.get(url);
        waitUtils.waitForPageToLoad();
    }
    
    /**
     * Get the current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get the page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Execute JavaScript
     */
    protected Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }
    
    /**
     * Take a screenshot
     */
    protected byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    
    /**
     * Refresh the page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
        waitUtils.waitForPageToLoad();
    }
    
    /**
     * Navigate back
     */
    protected void navigateBack() {
        driver.navigate().back();
        waitUtils.waitForPageToLoad();
    }
} 