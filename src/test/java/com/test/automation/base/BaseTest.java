package com.test.automation.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.test.automation.utils.ConfigReader;
import com.test.automation.utils.Logger;
import com.test.automation.utils.WebDriverManager;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        initializeDriver(browser);
        configureDriver();
        Logger.info("WebDriver initialized with browser: " + browser);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            WebDriverManager.quitDriver();
            Logger.info("WebDriver quit successfully");
        }
    }

    private void initializeDriver(String browser) {
        try {
            WebDriverManager.initDriver(browser);
            driver = WebDriverManager.getDriver();
        } catch (Exception e) {
            Logger.error("Error initializing driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void configureDriver() {
        if (driver != null) {
            // Set timeouts
            int pageLoadTimeout = ConfigReader.getIntProperty("page.load.timeout");
            int implicitWait = ConfigReader.getIntProperty("implicit.wait");
            
            // Configure timeouts
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            
            // Maximize window
            driver.manage().window().maximize();
            
            Logger.info("Driver configured with timeouts - pageLoad: " + pageLoadTimeout + "s, implicit: " + implicitWait + "s");
        }
    }
} 