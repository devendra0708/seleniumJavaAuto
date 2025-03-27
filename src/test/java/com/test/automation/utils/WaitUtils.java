package com.test.automation.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class for handling various wait operations
 */
public class WaitUtils {
    private WebDriver driver;
    private WebDriverWait wait;
    private int defaultTimeoutSeconds;
    
    /**
     * Constructor for WaitUtils
     * @param driver WebDriver instance
     */
    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeoutSeconds = ConfigReader.getIntProperty("explicit.wait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeoutSeconds));
        Logger.debug("WaitUtils initialized with default timeout: " + defaultTimeoutSeconds + " seconds");
    }
    
    /**
     * Constructor for WaitUtils with custom timeout
     * @param driver WebDriver instance
     * @param timeoutInSeconds Custom timeout in seconds
     */
    public WaitUtils(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.defaultTimeoutSeconds = timeoutInSeconds;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        Logger.debug("WaitUtils initialized with custom timeout: " + timeoutInSeconds + " seconds");
    }
    
    /**
     * Wait for page to load completely
     */
    public void waitForPageToLoad() {
        try {
            // Wait for document.readyState to be 'complete'
            wait.until((ExpectedCondition<Boolean>) driver -> {
                String readyState = ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState").toString();
                return "complete".equals(readyState);
            });
            
            // Wait for jQuery to be loaded and active requests to complete
            waitForAjaxComplete();
            
            Logger.debug("Page loaded completely");
        } catch (Exception e) {
            Logger.warn("Waiting for page load timed out or failed: " + e.getMessage());
        }
    }
    
    /**
     * Wait for AJAX calls to complete
     */
    public void waitForAjaxComplete() {
        try {
            ExpectedCondition<Boolean> ajaxLoadCondition = driver -> {
                return ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0").equals(true);
            };
            wait.until(ajaxLoadCondition);
            Logger.debug("AJAX calls completed");
        } catch (Exception e) {
            Logger.warn("Timeout waiting for AJAX calls to complete or jQuery is not defined", e);
        }
    }
    
    /**
     * Wait for Angular to finish rendering
     */
    public void waitForAngular() {
        try {
            // For Angular 1.x
            String angularScript = "return (window.angular !== undefined) && "
                    + "(angular.element(document).injector() !== undefined) && "
                    + "(angular.element(document).injector().get('$http').pendingRequests.length === 0)";
            
            // For Angular 2+
            String angular2Script = "return (window.getAllAngularTestabilities) && "
                    + "window.getAllAngularTestabilities().every(testability => testability.isStable())";
            
            ExpectedCondition<Boolean> angularLoad = driver -> {
                try {
                    return Boolean.TRUE.equals(((JavascriptExecutor) driver).executeScript(angularScript))
                            || Boolean.TRUE.equals(((JavascriptExecutor) driver).executeScript(angular2Script));
                } catch (Exception e) {
                    // Angular not found or script error
                    return true;
                }
            };
            
            wait.until(angularLoad);
            Logger.debug("Angular finished rendering");
        } catch (Exception e) {
            Logger.warn("Timeout waiting for Angular to finish rendering", e);
        }
    }
    
    /**
     * Wait for an element to be visible
     * @param locator By locator for the element
     * @return WebElement when found
     */
    public WebElement waitForElementVisible(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Logger.debug("Element is visible: " + locator);
            return element;
        } catch (TimeoutException e) {
            Logger.error("Element not visible within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Wait for an element to be clickable
     * @param locator By locator for the element
     * @return WebElement when found
     */
    public WebElement waitForElementClickable(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            Logger.debug("Element is clickable: " + locator);
            return element;
        } catch (TimeoutException e) {
            Logger.error("Element not clickable within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Wait for an element to be present in DOM
     * @param locator By locator for the element
     * @return WebElement when found
     */
    public WebElement waitForElementPresent(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            Logger.debug("Element is present: " + locator);
            return element;
        } catch (TimeoutException e) {
            Logger.error("Element not present within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Wait for an element to disappear
     * @param locator By locator for the element
     * @return true if element disappears within timeout
     */
    public boolean waitForElementToDisappear(By locator) {
        try {
            boolean result = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            Logger.debug("Element disappeared: " + locator);
            return result;
        } catch (TimeoutException e) {
            Logger.error("Element did not disappear within timeout: " + locator, e);
            return false;
        }
    }
    
    /**
     * Wait for an element's text to contain expected text
     * @param locator By locator for the element
     * @param text Expected text
     * @return WebElement when condition is met
     */
    public WebElement waitForTextToBePresentInElement(By locator, String text) {
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            WebElement element = driver.findElement(locator);
            Logger.debug("Element contains text '" + text + "': " + locator);
            return element;
        } catch (TimeoutException e) {
            Logger.error("Element does not contain text '" + text + "' within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Wait with fluent wait pattern
     * Useful for unstable elements with configurable polling
     * @param locator By locator for the element
     * @param timeoutInSeconds Timeout in seconds
     * @param pollingIntervalInMillis Polling interval in milliseconds
     * @return WebElement when found
     */
    public WebElement waitFluentlyForElement(By locator, int timeoutInSeconds, long pollingIntervalInMillis) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalInMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        
        try {
            WebElement element = fluentWait.until(driver -> driver.findElement(locator));
            Logger.debug("Element found with fluent wait: " + locator);
            return element;
        } catch (TimeoutException e) {
            Logger.error("Element not found with fluent wait within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Wait for page title to contain specific text
     * @param titleText Expected text in title
     * @return true if title contains the text within timeout
     */
    public boolean waitForTitleToContain(String titleText) {
        try {
            boolean result = wait.until(ExpectedConditions.titleContains(titleText));
            Logger.debug("Page title contains '" + titleText + "'");
            return result;
        } catch (TimeoutException e) {
            Logger.error("Page title does not contain '" + titleText + "' within timeout", e);
            return false;
        }
    }
    
    /**
     * Wait for a URL to contain specific text
     * @param urlText Expected text in URL
     * @return true if URL contains the text within timeout
     */
    public boolean waitForUrlToContain(String urlText) {
        try {
            boolean result = wait.until(ExpectedConditions.urlContains(urlText));
            Logger.debug("URL contains '" + urlText + "'");
            return result;
        } catch (TimeoutException e) {
            Logger.error("URL does not contain '" + urlText + "' within timeout", e);
            return false;
        }
    }
    
    /**
     * Wait for all elements matching a locator to be visible
     * @param locator By locator for the elements
     * @return List of WebElements when all are visible
     */
    public java.util.List<WebElement> waitForAllElementsVisible(By locator) {
        try {
            java.util.List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            Logger.debug("All elements visible: " + locator + " (count: " + elements.size() + ")");
            return elements;
        } catch (TimeoutException e) {
            Logger.error("Not all elements visible within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Wait for a specific URL to load
     * @param expectedUrl The expected URL (or part of it)
     * @return true if successful, false otherwise
     */
    public boolean waitForUrlToLoad(String expectedUrl) {
        try {
            // First wait for the page to load
            waitForPageToLoad();
            
            // Then wait for the URL to contain the expected value
            boolean result = wait.until((ExpectedCondition<Boolean>) driver -> 
                    driver.getCurrentUrl() != null && driver.getCurrentUrl().contains(expectedUrl));
            
            if (result) {
                Logger.debug("URL containing '" + expectedUrl + "' loaded successfully");
            } else {
                Logger.warn("URL did not load as expected. Current URL: " + driver.getCurrentUrl());
            }
            
            return result;
        } catch (TimeoutException e) {
            Logger.warn("Timeout waiting for URL to load: " + expectedUrl);
            return false;
        } catch (Exception e) {
            Logger.error("Error waiting for URL to load: " + expectedUrl, e);
            return false;
        }
    }
    
    /**
     * Static sleep method (use sparingly, prefer explicit waits)
     * @param milliseconds Time to sleep in milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
            Logger.debug("Slept for " + milliseconds + " milliseconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.warn("Sleep interrupted", e);
        }
    }
} 