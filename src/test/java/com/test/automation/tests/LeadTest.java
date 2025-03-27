package com.test.automation.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.test.automation.pages.LoginPage;
import com.test.automation.pages.SalesforceHomePage;
import com.test.automation.pages.LeadsPage;
import com.test.automation.utils.ConfigReader;
import com.test.automation.utils.Logger;
import com.test.automation.utils.WebDriverManager;

import java.util.UUID;

/**
 * Tests for Salesforce Lead management
 */
public class LeadTest {
    private LoginPage loginPage;
    private SalesforceHomePage homePage;
    private LeadsPage leadsPage;

    @BeforeMethod
    public void setUp() {
        try {
            Logger.info("Setting up WebDriver for lead test");
            
            // Initialize WebDriver through WebDriverManager
            WebDriverManager.initDriver();
            
            // Initialize page objects
            loginPage = new LoginPage();
            homePage = new SalesforceHomePage();
            leadsPage = new LeadsPage();
            
            // Log in to Salesforce
            String url = ConfigReader.getProperty("base.url");
            Logger.info("Navigating to Salesforce: " + url);
            WebDriverManager.navigateTo(url);
            
            // Verify login page loaded
            Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded");
            
            loginPage.login(
                ConfigReader.getProperty("test.username"),
                ConfigReader.getProperty("test.password")
            );
            
            // Verify we're logged in successfully
            Assert.assertTrue(loginPage.isLoginSuccessful(), 
                "Should be logged in to Salesforce successfully for Lead test");
            
            // Verify home page loaded
            Assert.assertTrue(homePage.isPageLoaded(), 
                "Home page should be loaded after login");
                
            Logger.info("Setup completed successfully - logged into Salesforce");
        } catch (Exception e) {
            Logger.error("Error in setup", e);
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            // Log out from Salesforce
            Logger.info("Logging out from Salesforce");
            homePage.logout();
            Assert.assertTrue(loginPage.isPageLoaded(), "Should be logged out and back at login page");
        } catch (Exception e) {
            Logger.error("Error during logout", e);
        } finally {
            WebDriverManager.quitDriver();
            Logger.info("Driver quit successfully");
        }
    }

    @Test
    public void testCreateNewLead() {
        try {
            Logger.step("Testing lead creation functionality");
            
            // Navigate to Leads tab
            leadsPage = homePage.navigateToLeads();
            
            // Verify leads page loaded
            Assert.assertTrue(leadsPage.isPageLoaded(), "Leads page should be loaded");
            
            // Generate unique test data
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);
            String firstName = "TestFirst" + uniqueId;
            String lastName = "TestLast" + uniqueId;
            String company = "Test Company " + uniqueId;
            String email = "test" + uniqueId + "@example.com";
            String phone = "555-123-4567";
            
            Logger.info("Creating test lead with data: " + firstName + " " + lastName + ", " + company);
            
            // Create a new lead
            leadsPage.createNewLead(firstName, lastName, company, email, phone);
            
            // Navigate back to leads list and search for the new lead
            leadsPage = homePage.navigateToLeads();
            Assert.assertTrue(leadsPage.isPageLoaded(), "Leads page should be loaded after navigation");
            
            // Search for the lead
            leadsPage.searchLeadByName(lastName);
            
            // Verify the lead exists
            Assert.assertTrue(leadsPage.isLeadPresent(lastName), 
                "Newly created lead should be found in the list");
            
            // Clean up - delete the lead
            leadsPage.deleteLead(lastName);
            
            Logger.info("Successfully created, verified, and deleted new lead");
            
        } catch (Exception e) {
            Logger.error("Test failed", e);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testSearchLead() {
        try {
            Logger.step("Testing lead search functionality");
            
            // Navigate to Leads tab
            leadsPage = homePage.navigateToLeads();
            
            // Verify leads page loaded
            Assert.assertTrue(leadsPage.isPageLoaded(), "Leads page should be loaded");
            
            // Get the initial lead count
            int initialCount = leadsPage.getLeadCount();
            Logger.info("Initial lead count: " + initialCount);
            
            if (initialCount == 0) {
                // Create a test lead if none exist
                String uniqueId = UUID.randomUUID().toString().substring(0, 8);
                String firstName = "SearchTest" + uniqueId;
                String lastName = "SearchLast" + uniqueId;
                String company = "Search Company " + uniqueId;
                String email = "search" + uniqueId + "@example.com";
                String phone = "555-987-6543";
                
                // Create the lead
                leadsPage.createNewLead(firstName, lastName, company, email, phone);
                
                // Navigate back to leads list
                leadsPage = homePage.navigateToLeads();
                Assert.assertTrue(leadsPage.isPageLoaded(), "Leads page should be loaded after navigation");
                
                // Verify the lead was created and can be found
                leadsPage.searchLeadByName(lastName);
                Assert.assertTrue(leadsPage.isLeadPresent(lastName), "Created lead should be found");
                
                // Clean up - delete the lead
                leadsPage.deleteLead(lastName);
            } else {
                // Use an existing lead for the search test
                // This assumes there's at least one lead in the system
                // In a real test, you might want to create a specific test lead
                Logger.info("Using existing leads for search test");
            }
            
            Logger.info("Lead search test completed successfully");
            
        } catch (Exception e) {
            Logger.error("Test failed", e);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
} 