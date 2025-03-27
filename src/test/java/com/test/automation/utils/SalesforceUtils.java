package com.test.automation.utils;

import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SalesforceUtils {
    
    /**
     * Switches to Lightning Experience if classic view is detected
     * @param driver WebDriver instance
     * @return true if switched or already in Lightning, false if failed
     */
    public static boolean switchToLightningIfNeeded(WebDriver driver) {
        try {
            // Check if we're already in Lightning
            if (isLightningExperience(driver)) {
                return true;
            }
            
            // Try to switch to Lightning
            By switchToLightningLink = By.linkText("Switch to Lightning Experience");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(switchToLightningLink));
            element.click();
            
            // Wait for Lightning to load
            wait.until((d) -> isLightningExperience(d));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if the current UI is Lightning Experience
     * @param driver WebDriver instance
     * @return true if Lightning UI is detected
     */
    public static boolean isLightningExperience(WebDriver driver) {
        try {
            return driver.findElements(By.xpath("//*[contains(@class, 'slds-')]")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Waits for Lightning page to load completely
     * @param driver WebDriver instance
     */
    public static void waitForLightningPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(
            ConfigReader.getIntProperty("page.load.timeout")));
            
        // Wait for the Lightning spinner to disappear
        By spinnerLocator = By.xpath("//div[contains(@class, 'slds-spinner')]");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinnerLocator));
        
        // Additional wait for page stability
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Switches to a new tab/window in Salesforce
     * @param driver WebDriver instance
     */
    public static void switchToNewTab(WebDriver driver) {
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        
        for (String handle : handles) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
    
    /**
     * Executes JavaScript to click an element when regular click might not work
     * @param driver WebDriver instance
     * @param element WebElement to click
     */
    public static void jsClick(WebDriver driver, WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }
} 