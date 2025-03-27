package com.test.automation.pages;

import com.test.automation.base.BasePage;
import com.test.automation.elements.Button;
import com.test.automation.elements.Input;
import com.test.automation.utils.Logger;
import com.test.automation.utils.SalesforceUtils;
import com.test.automation.utils.WaitUtils;
import com.test.automation.base.BaseElement;

import org.openqa.selenium.By;
import org.testng.Assert;

/**
 * Page object for Salesforce home page
 */
public class SalesforceHomePage extends BasePage {
    // Navigation tabs
    private By homeTab;
    private By leadsTab;
    private By contactsTab;
    private By accountsTab;
    private By opportunitiesTab;
    
    // User menu elements
    private By userMenuButton;
    private By logoutMenuItem;
    private By setupMenuItem;
    
    // Search
    private By globalSearchBox;
    private By searchButton;
    
    // Element objects
    private Button homeButton;
    private Button leadsButton;
    private Button contactsButton;
    private Button accountsButton;
    private Button opportunitiesButton;
    private Button userMenuBtn;
    private Button logoutButton;
    private Button setupButton;
    private Input searchInput;
    private Button searchBtn;
    
    /**
     * Constructor for SalesforceHomePage
     */
    public SalesforceHomePage() {
        super();
        // initLocators is now called from BasePage constructor
    }
    
    /**
     * Initialize locators
     */
    @Override
    protected void initLocators() {
        try {
            // Initialize navigation tab locators
            homeTab = By.xpath("//span[contains(@class, 'slds-truncate') and text()='Home']");
            leadsTab = By.xpath("//span[contains(@class, 'slds-truncate') and text()='Leads']");
            contactsTab = By.xpath("//span[contains(@class, 'slds-truncate') and text()='Contacts']");
            accountsTab = By.xpath("//span[contains(@class, 'slds-truncate') and text()='Accounts']");
            opportunitiesTab = By.xpath("//span[contains(@class, 'slds-truncate') and text()='Opportunities']");
            
            // Initialize user menu locators
            userMenuButton = By.className("userProfileCard");
            logoutMenuItem = By.xpath("//a[contains(@class, 'logout')]");
            setupMenuItem = By.xpath("//span[text()='Setup']");
            
            // Initialize search locators
            globalSearchBox = By.xpath("//input[contains(@placeholder, 'Search')]");
            searchButton = By.xpath("//button[contains(@class, 'searchButton')]");
            
            Logger.debug("SalesforceHomePage locators initialized");
        } catch (Exception e) {
            Logger.error("Failed to initialize locators in SalesforceHomePage", e);
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
            Assert.assertNotNull(homeTab, "Home tab locator is null");
            Assert.assertNotNull(leadsTab, "Leads tab locator is null");
            Assert.assertNotNull(contactsTab, "Contacts tab locator is null");
            Assert.assertNotNull(accountsTab, "Accounts tab locator is null");
            Assert.assertNotNull(opportunitiesTab, "Opportunities tab locator is null");
            Assert.assertNotNull(userMenuButton, "User menu button locator is null");
            Assert.assertNotNull(logoutMenuItem, "Logout menu item locator is null");
            Assert.assertNotNull(setupMenuItem, "Setup menu item locator is null");
            Assert.assertNotNull(globalSearchBox, "Global search box locator is null");
            Assert.assertNotNull(searchButton, "Search button locator is null");
            
            // Initialize custom element objects
            homeButton = new Button(homeTab);
            leadsButton = new Button(leadsTab);
            contactsButton = new Button(contactsTab);
            accountsButton = new Button(accountsTab);
            opportunitiesButton = new Button(opportunitiesTab);
            
            userMenuBtn = new Button(userMenuButton);
            logoutButton = new Button(logoutMenuItem);
            setupButton = new Button(setupMenuItem);
            
            searchInput = new Input(globalSearchBox);
            searchBtn = new Button(searchButton);
            
            // Verify elements are not null
            Assert.assertNotNull(homeButton, "Home button element is null");
            Assert.assertNotNull(leadsButton, "Leads button element is null");
            Assert.assertNotNull(contactsButton, "Contacts button element is null");
            Assert.assertNotNull(accountsButton, "Accounts button element is null");
            Assert.assertNotNull(opportunitiesButton, "Opportunities button element is null");
            Assert.assertNotNull(userMenuBtn, "User menu button element is null");
            Assert.assertNotNull(logoutButton, "Logout button element is null");
            Assert.assertNotNull(setupButton, "Setup button element is null");
            Assert.assertNotNull(searchInput, "Search input element is null");
            Assert.assertNotNull(searchBtn, "Search button element is null");
            
            Logger.debug("Salesforce home page elements initialized successfully");
        } catch (Exception e) {
            Logger.error("Failed to initialize Salesforce home page elements", e);
            throw e;
        }
    }
    
    /**
     * Check if the home page is loaded correctly
     * @return true if the page is loaded correctly
     */
    @Override
    public boolean isPageLoaded() {
        try {
            boolean homeTabDisplayed = new BaseElement(homeTab).isDisplayed();
            boolean userMenuDisplayed = new BaseElement(userMenuButton).isDisplayed();
            
            boolean isLoaded = homeTabDisplayed && userMenuDisplayed;
            
            if (isLoaded) {
                Logger.info("Salesforce Home page is loaded");
            } else {
                Logger.warn("Salesforce Home page is not fully loaded");
            }
            
            return isLoaded;
        } catch (Exception e) {
            Logger.error("Error checking if Salesforce Home page is loaded", e);
            return false;
        }
    }
    
    /**
     * Check if the home page is loaded
     * @return true if home page is loaded
     */
    public boolean isHomePageLoaded() {
        return isPageLoaded();
    }
    
    /**
     * Navigate to the Leads tab
     * @return LeadsPage instance
     */
    public LeadsPage navigateToLeads() {
        leadsButton.click();
        waitUtils.waitForPageToLoad();
        SalesforceUtils.waitForLightningPageLoad(driver);
        Logger.info("Navigated to Leads tab");
        return new LeadsPage();
    }
    
    /**
     * Navigate to the Contacts tab
     * @return SalesforceHomePage instance for method chaining
     */
    public SalesforceHomePage navigateToContacts() {
        contactsButton.click();
        waitUtils.waitForPageToLoad();
        SalesforceUtils.waitForLightningPageLoad(driver);
        Logger.info("Navigated to Contacts tab");
        return this;
    }
    
    /**
     * Navigate to the Accounts tab
     * @return SalesforceHomePage instance for method chaining
     */
    public SalesforceHomePage navigateToAccounts() {
        accountsButton.click();
        waitUtils.waitForPageToLoad();
        SalesforceUtils.waitForLightningPageLoad(driver);
        Logger.info("Navigated to Accounts tab");
        return this;
    }
    
    /**
     * Navigate to the Opportunities tab
     * @return SalesforceHomePage instance for method chaining
     */
    public SalesforceHomePage navigateToOpportunities() {
        opportunitiesButton.click();
        waitUtils.waitForPageToLoad();
        SalesforceUtils.waitForLightningPageLoad(driver);
        Logger.info("Navigated to Opportunities tab");
        return this;
    }
    
    /**
     * Open the user menu
     * @return SalesforceHomePage instance for method chaining
     */
    public SalesforceHomePage openUserMenu() {
        userMenuBtn.click();
        // Brief wait for the menu to appear
        WaitUtils.sleep(500);
        Logger.info("Opened user menu");
        return this;
    }
    
    /**
     * Logout from Salesforce
     * @return LoginPage instance
     */
    public LoginPage logout() {
        openUserMenu();
        logoutButton.click();
        waitUtils.waitForPageToLoad();
        Logger.info("Logged out from Salesforce");
        return new LoginPage();
    }
    
    /**
     * Navigate to Setup
     * @return SalesforceHomePage instance for method chaining
     */
    public SalesforceHomePage navigateToSetup() {
        openUserMenu();
        setupButton.click();
        waitUtils.waitForPageToLoad();
        SalesforceUtils.waitForLightningPageLoad(driver);
        Logger.info("Navigated to Setup");
        return this;
    }
    
    /**
     * Perform a global search
     * @param searchTerm Term to search for
     * @return SalesforceHomePage instance for method chaining
     */
    public SalesforceHomePage search(String searchTerm) {
        searchInput.enterText(searchTerm);
        searchBtn.click();
        waitUtils.waitForPageToLoad();
        SalesforceUtils.waitForLightningPageLoad(driver);
        Logger.info("Performed global search for: " + searchTerm);
        return this;
    }
    
    /**
     * Navigate to the Home tab
     * @return SalesforceHomePage instance for method chaining
     */
    public SalesforceHomePage navigateToHome() {
        homeButton.click();
        waitUtils.waitForPageToLoad();
        SalesforceUtils.waitForLightningPageLoad(driver);
        Logger.info("Navigated to Home tab");
        return this;
    }
    
    /**
     * Take a screenshot of the Salesforce home page
     * @return byte array of the screenshot
     */
    public byte[] captureScreenshot() {
        Logger.info("Taking screenshot of Salesforce home page");
        return takeScreenshot();
    }
} 