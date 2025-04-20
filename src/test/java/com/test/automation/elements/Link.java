package com.test.automation.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.test.automation.base.BaseElement;
import com.test.automation.utils.Logger;

/**
 * Link element class that extends BaseElement with link-specific functionality
 */
public class Link extends BaseElement {
    
    /**
     * Constructor for Link element with locator only
     * @param locator By locator to find the element
     */
    public Link(By locator) {
        super(locator);
        Logger.debug("Created Link element with locator: " + locator);
    }
    
    /**
     * Constructor for Link element with existing WebElement
     * @param element WebElement instance
     */
    public Link(WebElement element) {
        super(element);
        Logger.debug("Created Link element from existing WebElement");
    }
    
    /**
     * Click the link with special handling for navigation links
     * Waits for the link to be clickable before performing the action
     */
    @Override
    public void click() {
        try {
            Logger.info("Clicking link");
            waitForClickable();
            scrollTo();
            super.click();
        } catch (Exception e) {
            Logger.error("Failed to click link", e);
            throw e;
        }
    }
    
    /**
     * Hover over the link
     */
    public void hover() {
        try {
            Logger.info("Hovering over link");
            waitForVisible();
            super.hover();
        } catch (Exception e) {
            Logger.error("Failed to hover over link", e);
            throw e;
        }
    }
    
    /**
     * Get the href attribute of the link
     * @return href attribute value
     */
    public String getHref() {
        try {
            String href = getAttribute("href");
            Logger.debug("Link href: " + href);
            return href;
        } catch (Exception e) {
            Logger.error("Failed to get link href", e);
            throw e;
        }
    }
    
    /**
     * Get the text of the link
     * @return Link text
     */
    @Override
    public String getText() {
        try {
            String text = super.getText();
            Logger.debug("Link text: " + text);
            return text;
        } catch (Exception e) {
            Logger.error("Failed to get link text", e);
            throw e;
        }
    }
    
    /**
     * Get the target attribute of the link
     * @return target attribute value
     */
    public String getTarget() {
        String target = getElement().getAttribute("target");
        Logger.debug("Link target: " + target);
        return target;
    }
    
    /**
     * Check if the link opens in a new tab/window
     * @return true if the link opens in a new tab/window
     */
    public boolean opensInNewTab() {
        String target = getTarget();
        boolean opensInNewTab = "_blank".equals(target);
        Logger.debug("Link opens in new tab: " + opensInNewTab);
        return opensInNewTab;
    }
    
    /**
     * Check if the link is valid (has a non-empty href)
     * @return true if the link is valid
     */
    public boolean isValid() {
        try {
            String href = getHref();
            boolean isValid = href != null && !href.trim().isEmpty() && !href.equals("#");
            Logger.debug("Link is valid: " + isValid);
            return isValid;
        } catch (Exception e) {
            Logger.error("Failed to check if link is valid", e);
            throw e;
        }
    }
    
    /**
     * Check if the link is an external link (starts with http or https)
     * @return true if the link is external
     */
    public boolean isExternalLink() {
        try {
            String href = getHref();
            boolean isExternal = href != null && 
                (href.startsWith("http://") || href.startsWith("https://"));
            Logger.debug("Link is external: " + isExternal);
            return isExternal;
        } catch (Exception e) {
            Logger.error("Failed to check if link is external", e);
            throw e;
        }
    }
} 