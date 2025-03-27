package com.test.automation.tests;

import com.test.automation.base.BaseTest;
import com.test.automation.pages.LoginPage;
import com.test.automation.utils.ConfigReader;
import com.test.automation.utils.Logger;
import com.test.automation.utils.WebDriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        try {
            // First, make sure driver is initialized
            Assert.assertNotNull(driver, "WebDriver is null in test method");
            
            // Navigate to the URL first
            String url = ConfigReader.getProperty("base.url");
            Logger.info("Navigating to: " + url);
            WebDriverManager.navigateTo(url);
            
            // Then create the page object
            LoginPage loginPage = new LoginPage();
            
            // Verify the login page loaded
            Assert.assertTrue(loginPage.isPageLoaded(), "Login page did not load");
            
            // Perform login with valid credentials
            loginPage.login(
                ConfigReader.getProperty("test.username"),
                ConfigReader.getProperty("test.password")
            );
            
            // Add assertions here based on your application's behavior
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login was not successful");
        } catch (Exception e) {
            Logger.error("Test failed: " + e.getMessage(), e);
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        try {
            // First, make sure driver is initialized
            Assert.assertNotNull(driver, "WebDriver is null in test method");
            
            // Navigate to the URL first
            String url = ConfigReader.getProperty("base.url");
            Logger.info("Navigating to: " + url);
            WebDriverManager.navigateTo(url);
            
            // Then create the page object
            LoginPage loginPage = new LoginPage();
            
            // Verify the login page loaded
            Assert.assertTrue(loginPage.isPageLoaded(), "Login page did not load");
            
            // Perform login with invalid credentials
            loginPage.login("invalid@email.com", "wrongpassword");
            
            // Verify error message is shown
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
                "Error message should be displayed for invalid credentials");
        } catch (Exception e) {
            Logger.error("Test failed: " + e.getMessage(), e);
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }
} 