package com.test.automation.pages;

import com.test.automation.base.BasePage;
import com.test.automation.base.BaseElement;
import com.test.automation.utils.Logger;

import org.openqa.selenium.By;

/**
 * Page object for the Salesforce login page
 */
public class LoginPage extends BasePage {
    
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
     * Initialize the login page
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
            return new BaseElement(usernameField).isDisplayed() &&
                   new BaseElement(passwordField).isDisplayed() &&
                   new BaseElement(loginButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Enter username
     */
    public LoginPage enterUsername(String username) {
        new BaseElement(usernameField).type(username);
        return this;
    }
    
    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        new BaseElement(passwordField).type(password);
        return this;
    }
    
    /**
     * Click login button
     */
    public LoginPage clickLoginButton() {
        new BaseElement(loginButton).click();
        return this;
    }
    
    /**
     * Check remember me checkbox
     */
    public LoginPage checkRememberMe() {
        BaseElement checkbox = new BaseElement(rememberMeCheckbox);
        if (!checkbox.getElement().isSelected()) {
            checkbox.click();
        }
        return this;
    }
    
    /**
     * Click forgot password link
     */
    public LoginPage clickForgotPassword() {
        new BaseElement(forgotPasswordLink).click();
        return this;
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return new BaseElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return new BaseElement(errorMessage).getText();
    }
    
    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        try {
            waitUtils.waitForPageToLoad();
            
            // Check for Lightning UI elements
            boolean homeTabVisible = new BaseElement(homeTab).isDisplayed();
            boolean userMenuVisible = new BaseElement(userMenuButton).isDisplayed();
            
            return homeTabVisible || userMenuVisible;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Login with username and password
     */
    public LoginPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return this;
    }
    
    /**
     * Login with remember me checked
     */
    public LoginPage loginWithRememberMe(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        checkRememberMe();
        clickLoginButton();
        return this;
    }
    
    /**
     * Navigate to login page
     */
    public LoginPage navigateToLoginPage() {
        String loginUrl = "https://login.salesforce.com";
        driver.get(loginUrl);
        waitUtils.waitForPageToLoad();
        return this;
    }
} 