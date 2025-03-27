package com.test.automation.pages;

import com.test.automation.base.BasePage;
import com.test.automation.base.BaseElement;
import com.test.automation.elements.Button;
import com.test.automation.elements.Input;
import com.test.automation.utils.Logger;
import com.test.automation.utils.SalesforceUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

/**
 * Page object for Salesforce Leads page
 */
public class LeadsPage extends BasePage {
    // Form locators
    private By newButton;
    private By firstNameField;
    private By lastNameField;
    private By companyField;
    private By emailField;
    private By phoneField;
    private By saveButton;
    
    // Table locators
    private By leadListTable;
    private By leadListRows;
    private By leadSearchBox;
    
    // Element objects
    private Button newBtn;
    private Input firstNameInput;
    private Input lastNameInput;
    private Input companyInput;
    private Input emailInput;
    private Input phoneInput;
    private Button saveBtn;
    private BaseElement leadTable;
    private Input searchInput;
    
    /**
     * Constructor for LeadsPage
     */
    public LeadsPage() {
        super();
        // initLocators is now called from BasePage constructor
    }
    
    /**
     * Initialize locators
     */
    @Override
    protected void initLocators() {
        try {
            // Initialize form locators
            newButton = By.xpath("//div[@title='New']");
            firstNameField = By.xpath("//input[@name='firstName']");
            lastNameField = By.xpath("//input[@name='lastName']");
            companyField = By.xpath("//input[@name='Company']");
            emailField = By.xpath("//input[@name='Email']");
            phoneField = By.xpath("//input[@name='Phone']");
            saveButton = By.xpath("//button[@name='SaveEdit']");
            
            // Initialize table locators
            leadListTable = By.xpath("//table[contains(@class, 'slds-table')]");
            leadListRows = By.xpath("//table[contains(@class, 'slds-table')]//tbody/tr");
            leadSearchBox = By.xpath("//input[contains(@placeholder, 'Search')]");
            
            Logger.debug("LeadsPage locators initialized");
        } catch (Exception e) {
            Logger.error("Failed to initialize locators in LeadsPage", e);
            throw e;
        }
    }
    
    /**
     * Initialize page elements
     * This overrides the base method to initialize our custom elements
     */
    @Override
    protected void initializeElements() {
        try {
            // Verify locators are not null
            Assert.assertNotNull(newButton, "New button locator is null");
            Assert.assertNotNull(firstNameField, "First name field locator is null");
            Assert.assertNotNull(lastNameField, "Last name field locator is null");
            Assert.assertNotNull(companyField, "Company field locator is null");
            Assert.assertNotNull(emailField, "Email field locator is null");
            Assert.assertNotNull(phoneField, "Phone field locator is null");
            Assert.assertNotNull(saveButton, "Save button locator is null");
            Assert.assertNotNull(leadListTable, "Lead list table locator is null");
            Assert.assertNotNull(leadSearchBox, "Lead search box locator is null");
            
            // Initialize custom element objects
            newBtn = new Button(newButton);
            firstNameInput = new Input(firstNameField);
            lastNameInput = new Input(lastNameField);
            companyInput = new Input(companyField);
            emailInput = new Input(emailField);
            phoneInput = new Input(phoneField);
            saveBtn = new Button(saveButton);
            
            leadTable = new BaseElement(leadListTable);
            searchInput = new Input(leadSearchBox);
            
            // Verify elements are not null
            Assert.assertNotNull(newBtn, "New button element is null");
            Assert.assertNotNull(firstNameInput, "First name input element is null");
            Assert.assertNotNull(lastNameInput, "Last name input element is null");
            Assert.assertNotNull(companyInput, "Company input element is null");
            Assert.assertNotNull(emailInput, "Email input element is null");
            Assert.assertNotNull(phoneInput, "Phone input element is null");
            Assert.assertNotNull(saveBtn, "Save button element is null");
            Assert.assertNotNull(leadTable, "Lead table element is null");
            Assert.assertNotNull(searchInput, "Search input element is null");
            
            Logger.debug("Leads page elements initialized successfully");
        } catch (Exception e) {
            Logger.error("Failed to initialize Leads page elements", e);
            throw e;
        }
    }
    
    /**
     * Check if the leads page is loaded correctly
     * @return true if the page is loaded correctly
     */
    @Override
    public boolean isPageLoaded() {
        try {
            boolean newButtonDisplayed = new BaseElement(newButton).isDisplayed();
            boolean leadListDisplayed = waitUtils.waitForElementToDisappear(By.xpath("//div[contains(@class, 'slds-spinner')]"));
            
            boolean isLoaded = newButtonDisplayed && leadListDisplayed;
            
            if (isLoaded) {
                Logger.info("Leads page is loaded");
            } else {
                Logger.warn("Leads page is not fully loaded");
            }
            
            return isLoaded;
        } catch (Exception e) {
            Logger.error("Error checking if Leads page is loaded", e);
            return false;
        }
    }
    
    /**
     * Click the New button to create a new lead
     * @return LeadsPage instance for method chaining
     */
    public LeadsPage clickNewButton() {
        newBtn.click();
        SalesforceUtils.waitForLightningPageLoad(driver);
        Logger.info("Clicked New button on Leads page");
        return this;
    }
    
    /**
     * Create a new lead with the given details
     * @param firstName First name
     * @param lastName Last name
     * @param company Company name
     * @param email Email address
     * @param phone Phone number
     * @return LeadsPage instance for method chaining
     */
    public LeadsPage createNewLead(String firstName, String lastName, String company, String email, String phone) {
        Logger.step("Creating new lead: " + firstName + " " + lastName + " - " + company);
        
        // Navigate to new lead form
        clickNewButton();
        
        // Fill in lead details
        firstNameInput.enterText(firstName);
        lastNameInput.enterText(lastName);
        companyInput.enterText(company);
        emailInput.enterText(email);
        phoneInput.enterText(phone);
        
        // Save the lead
        saveBtn.click();
        SalesforceUtils.waitForLightningPageLoad(driver);
        
        Logger.info("Created new lead: " + firstName + " " + lastName);
        return this;
    }
    
    /**
     * Check if the lead list is loaded
     * @return true if lead list is loaded
     */
    public boolean isLeadListLoaded() {
        return leadTable.isDisplayed();
    }
    
    /**
     * Get the count of leads in the current view
     * @return Number of leads
     */
    public int getLeadCount() {
        List<WebElement> rows = driver.findElements(leadListRows);
        Logger.debug("Found " + rows.size() + " leads in the current view");
        return rows.size();
    }
    
    /**
     * Search for a lead by name
     * @param leadName Name to search for
     * @return LeadsPage instance for method chaining
     */
    public LeadsPage searchLeadByName(String leadName) {
        Logger.step("Searching for lead: " + leadName);
        
        // Get the search box in the Leads list view
        searchInput.enterTextAndSubmit(leadName);
        
        // Wait for the search results
        SalesforceUtils.waitForLightningPageLoad(driver);
        
        Logger.info("Searched for lead: " + leadName);
        return this;
    }
    
    /**
     * Check if a lead with the given name exists in the current view
     * @param leadName Lead name to check for
     * @return true if lead exists
     */
    public boolean isLeadPresent(String leadName) {
        try {
            By leadLocator = By.xpath("//a[contains(text(),'" + leadName + "')]");
            return new BaseElement(leadLocator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Open a lead by name
     * @param leadName Name of the lead to open
     * @return LeadsPage instance for method chaining
     */
    public LeadsPage openLeadByName(String leadName) {
        By leadLocator = By.xpath("//a[contains(@title, '" + leadName + "')]");
        
        // Find and click on the lead with the given name
        Button leadLink = new Button(leadLocator);
        leadLink.click();
        
        // Wait for the lead detail page to load
        SalesforceUtils.waitForLightningPageLoad(driver);
        
        Logger.info("Opened lead: " + leadName);
        return this;
    }
    
    /**
     * Delete a lead by name
     * @param leadName Name of the lead to delete
     * @return LeadsPage instance for method chaining
     */
    public LeadsPage deleteLead(String leadName) {
        // First search for the lead
        searchLeadByName(leadName);
        
        // Then click on the dropdown menu for the lead
        By dropdownButtonLocator = By.xpath("//a[contains(@title, '" + leadName + "')]/ancestor::tr//button[contains(@class, 'rowActions')]");
        Button dropdownButton = new Button(dropdownButtonLocator);
        dropdownButton.click();
        
        // Click the Delete option
        By deleteOptionLocator = By.xpath("//div[contains(@class, 'actionMenu')]//a[@title='Delete']");
        waitUtils.waitForElementVisible(deleteOptionLocator);
        Button deleteOption = new Button(deleteOptionLocator);
        deleteOption.click();
        
        // Confirm deletion in the modal
        By confirmButtonLocator = By.xpath("//button[contains(@title, 'Delete')]");
        waitUtils.waitForElementVisible(confirmButtonLocator);
        Button confirmButton = new Button(confirmButtonLocator);
        confirmButton.click();
        
        // Wait for the page to refresh
        SalesforceUtils.waitForLightningPageLoad(driver);
        
        Logger.info("Deleted lead: " + leadName);
        return this;
    }
} 