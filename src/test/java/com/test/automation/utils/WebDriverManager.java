package com.test.automation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Manages WebDriver instances using ThreadLocal for thread safety
 */
public class WebDriverManager {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * Initialize a Chrome WebDriver
     */
    public static void initChromeDriver() {
        if (driverThreadLocal.get() != null) {
            quitDriver();
        }
        
        Logger.info("Initializing Chrome WebDriver");
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");  // Avoid DevTools listening issues
        options.addArguments("--disable-extensions");      // Disable extensions to improve stability
        options.addArguments("--no-sandbox");              // Less restrictive sandbox
        options.addArguments("--disable-dev-shm-usage");   // Overcome limited resource problems
        
        // Set headless mode if configured
        if (ConfigReader.getBooleanProperty("headless.mode")) {
            options.addArguments("--headless=new");
            Logger.info("Running in Chrome headless mode");
        }
        
        WebDriver driver = new ChromeDriver(options);
        driverThreadLocal.set(driver);
        Logger.info("Chrome WebDriver initialized");
    }
    
    /**
     * Initialize a Firefox WebDriver
     */
    public static void initFirefoxDriver() {
        if (driverThreadLocal.get() != null) {
            quitDriver();
        }
        
        Logger.info("Initializing Firefox WebDriver");
        io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        // Set headless mode if configured
        if (ConfigReader.getBooleanProperty("headless.mode")) {
            options.addArguments("--headless");
            Logger.info("Running in Firefox headless mode");
        }
        
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        driverThreadLocal.set(driver);
        Logger.info("Firefox WebDriver initialized");
    }
    
    /**
     * Initialize an Edge WebDriver
     */
    public static void initEdgeDriver() {
        if (driverThreadLocal.get() != null) {
            quitDriver();
        }
        
        Logger.info("Initializing Edge WebDriver");
        io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications");
        
        // Set headless mode if configured
        if (ConfigReader.getBooleanProperty("headless.mode")) {
            options.addArguments("--headless=new");
            Logger.info("Running in headless mode");
        }
        
        WebDriver driver = new EdgeDriver(options);
        driver.manage().window().maximize();
        driverThreadLocal.set(driver);
        Logger.info("Edge WebDriver initialized");
    }
    
    /**
     * Initialize a WebDriver based on the browser specified in config
     */
    public static void initDriver() {
        String browser = ConfigReader.getProperty("browser.type").toLowerCase();
        initDriver(browser);
    }
    
    /**
     * Initialize a WebDriver with the specified browser
     * @param browser Browser type (chrome, firefox, edge)
     */
    public static synchronized void initDriver(String browser) {
        // First check if we already have a driver for this thread
        if (driverThreadLocal.get() != null) {
            Logger.info("WebDriver already exists for this thread, quitting existing driver");
            quitDriver();
        }
        
        Logger.info("Initializing WebDriver with browser: " + browser);
        
        switch (browser.toLowerCase()) {
            case "firefox":
                initFirefoxDriver();
                break;
            case "edge":
                initEdgeDriver();
                break;
            case "chrome":
            default:
                initChromeDriver();
                break;
        }
        
        // Verify driver was initialized
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Failed to initialize WebDriver for browser: " + browser);
        }
    }
    
    /**
     * Get the WebDriver instance
     * @return WebDriver instance
     */
    public static synchronized WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        
        if (driver == null) {
            Logger.info("No WebDriver found for current thread, initializing new driver");
            initDriver();
            driver = driverThreadLocal.get();
            
            if (driver == null) {
                Logger.error("WebDriver initialization failed");
                throw new IllegalStateException("WebDriver initialization failed");
            }
        }
        
        try {
            // Check if driver is still responsive
            driver.getWindowHandles();
            Logger.debug("Retrieved active WebDriver instance");
            return driver;
        } catch (Exception e) {
            Logger.warn("WebDriver appears to be stale, reinitializing...", e);
            quitDriver();
            initDriver();
            return driverThreadLocal.get();
        }
    }
    
    /**
     * Quit the WebDriver instance for the current thread
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        
        if (driver != null) {
            Logger.info("Quitting WebDriver");
            driver.quit();
            driverThreadLocal.remove();
        }
    }
    
    /**
     * Navigate to a specific URL
     * @param url The URL to navigate to
     */
    public static void navigateTo(String url) {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                Logger.info("Navigating to URL: " + url);
                
                // Clear cookies before navigating to avoid issues
                driver.manage().deleteAllCookies();
                
                // Navigate to the URL
                driver.get(url);
                
                // Create a WaitUtils instance for this driver
                WaitUtils waitUtils = new WaitUtils(driver);
                
                // Wait for page to load
                waitUtils.waitForPageToLoad();
                
                // Add the explicit base URL check
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl == null || !currentUrl.contains(url)) {
                    Logger.warn("Current URL doesn't match expected. Current: " + 
                                (currentUrl != null ? currentUrl : "null") + 
                                ", Expected to contain: " + url);
                    
                    // Try again with a different approach for more stubborn browsers
                    Logger.info("Retrying navigation with JavaScript");
                    ((JavascriptExecutor) driver).executeScript("window.location.href='" + url + "';");
                    
                    // Wait again
                    waitUtils.waitForPageToLoad();
                }
                
                // Final check
                currentUrl = driver.getCurrentUrl();
                Logger.info("Navigation complete. Current URL: " + currentUrl);
                
            } catch (Exception e) {
                Logger.error("Error navigating to URL: " + url, e);
                throw e;
            }
        } else {
            Logger.error("Cannot navigate: WebDriver is null");
        }
    }
} 