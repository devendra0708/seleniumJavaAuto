package com.test.automation.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.test.automation.pages.LoginPage;
import com.test.automation.pages.SalesforceHomePage;
import com.test.automation.utils.ConfigReader;
import com.test.automation.utils.Logger;
import com.test.automation.utils.WebDriverManager;

import java.util.UUID;

/**
 * Standalone tests for Salesforce login functionality
 */
public class SingleLoginTest {
    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        try {
            Logger.info("Setting up WebDriver for login test");
            
            // Initialize WebDriver through WebDriverManager
            WebDriverManager.initDriver();
            
            // Initialize login page
            loginPage = new LoginPage();
            
            Logger.info("Test setup completed successfully");
        } catch (Exception e) {
            Logger.error("Error initializing test", e);
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        Logger.info("Tearing down WebDriver");
        WebDriverManager.quitDriver();
    }

    @Test
    public void testSuccessfulSalesforceLogin() {
        try {
            Logger.step("Testing successful Salesforce login");
            
            // Navigate to Salesforce login
            String url = ConfigReader.getProperty("base.url");
            Logger.info("Navigating to Salesforce: " + url);
            WebDriverManager.navigateTo(url);
            
            // Verify login page loaded correctly
            Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
            
            // Perform login with valid credentials
            loginPage.login(
                ConfigReader.getProperty("test.username"),
                ConfigReader.getProperty("test.password")
            );
            
            // Verify successful login
            Assert.assertTrue(loginPage.isLoginSuccessful(), 
                "Should be logged in to Salesforce successfully");
            
            // Create homepage instance and verify we can navigate
            SalesforceHomePage homePage = new SalesforceHomePage();
            Assert.assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
            
            // Perform logout
            homePage.logout();
            Assert.assertTrue(loginPage.isPageLoaded(), "Should be logged out and back at login page");
            
            Logger.info("Successful login test completed");
        } catch (Exception e) {
            Logger.error("Test failed", e);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testFailedSalesforceLogin() {
        try {
            Logger.step("Testing failed Salesforce login");
            
            // Navigate to Salesforce login
            String url = ConfigReader.getProperty("base.url");
            Logger.info("Navigating to Salesforce: " + url);
            WebDriverManager.navigateTo(url);
            
            // Verify login page loaded correctly
            Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
            
            // Generate random credentials to ensure they're invalid
            String randomUsername = "invalid_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
            String randomPassword = "Invalid_" + UUID.randomUUID().toString().substring(0, 8);
            
            // Perform login with invalid credentials
            loginPage.login(randomUsername, randomPassword);
            
            // Verify error message is displayed
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
                "Error message should be displayed for invalid credentials");
            
            Logger.info("Error message displayed as expected");
            Logger.info("Failed login test completed");
        } catch (Exception e) {
            Logger.error("Test failed", e);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
} 