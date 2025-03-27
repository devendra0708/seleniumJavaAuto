package com.test.automation.pages;

import com.test.automation.base.LightBasePage;
import com.test.automation.base.LightBaseElement;

import org.openqa.selenium.By;

/**
 * Lightweight implementation of the Salesforce login page
 * Uses the simplified base classes for a more streamlined approach
 */
public class LightLoginPage extends LightBasePage {
    
    // Locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("Login");
    private By errorMessage = By.id("error");
    private By rememberMeCheckbox = By.id("rememberUn");
    private By forgotPasswordLink = By.id("forgot_password_link");
    
    // Success indicators
    private By homeTab = By.xpath("//span[contains(@class, 'slds-truncate') and text()='Home']");
    private By userMenuButton = By.className("userProfileCard");
    
    /**
     * Initialize locators
     */
    @Override
    protected void initLocators() {
        // Locators are already initialized as fields
    }
    
    /**
     * Check if the login page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return new LightBaseElement(usernameField).isDisplayed() &&
                   new LightBaseElement(passwordField).isDisplayed() &&
                   new LightBaseElement(loginButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Enter username
     */
    public LightLoginPage enterUsername(String username) {
        new LightBaseElement(usernameField).type(username);
        return this;
    }
    
    /**
     * Enter password
     */
    public LightLoginPage enterPassword(String password) {
        new LightBaseElement(passwordField).type(password);
        return this;
    }
    
    /**
     * Click login button
     */
    public LightLoginPage clickLoginButton() {
        new LightBaseElement(loginButton).click();
        return this;
    }
    
    /**
     * Check remember me checkbox
     */
    public LightLoginPage checkRememberMe() {
        LightBaseElement checkbox = new LightBaseElement(rememberMeCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }
    
    /**
     * Click forgot password link
     */
    public LightLoginPage clickForgotPassword() {
        new LightBaseElement(forgotPasswordLink).click();
        return this;
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return new LightBaseElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return new LightBaseElement(errorMessage).getText();
    }
    
    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        try {
            waitUtils.waitForPageToLoad();
            
            // Check for Lightning UI elements
            boolean homeTabVisible = new LightBaseElement(homeTab).isDisplayed();
            boolean userMenuVisible = new LightBaseElement(userMenuButton).isDisplayed();
            
            return homeTabVisible || userMenuVisible;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Login with username and password
     */
    public LightLoginPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return this;
    }
    
    /**
     * Navigate to login page
     */
    public LightLoginPage navigateToLoginPage() {
        navigateTo("https://login.salesforce.com");
        return this;
    }
} 