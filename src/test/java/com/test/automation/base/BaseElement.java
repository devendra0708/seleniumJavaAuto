package com.test.automation.base;

import java.time.Duration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.automation.utils.ConfigReader;
import com.test.automation.utils.Logger;
import com.test.automation.utils.WebDriverManager;

/**
 * Base class for all UI element types in the framework
 * Implements WebElement to allow it to be used anywhere a WebElement is required
 */
public class BaseElement implements WebElement {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected By locator;
    protected WebElement element = null;
    protected Integer explicitWait;
    
    /**
     * Constructor for BaseElement with locator only
     * @param locator By locator for this element
     */
    public BaseElement(By locator) {
        this.driver = WebDriverManager.getDriver();
        this.locator = locator;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
            ConfigReader.getIntProperty("explicit.wait")));
        Logger.debug("Created element with locator: " + locator);
    }
    
    /**
     * Constructor for BaseElement with WebElement only
     * @param element WebElement instance
     */
    public BaseElement(WebElement element) {
        this.driver = WebDriverManager.getDriver();
        this.element = element;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
            ConfigReader.getIntProperty("explicit.wait")));
        Logger.debug("Created element from existing WebElement");
    }
    
    /**
     * Get the WebElement instance
     * This will find the element if it hasn't been found yet
     * @return WebElement instance
     */
    public WebElement getElement() {
        try {
            if (element == null) {
                if (locator != null) {
                    element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                }
            }
            return element;
        } catch (Exception e) {
            String locatorStr = locator != null ? locator.toString() : "null";
            Logger.error("Failed to get element with locator: " + locatorStr, e);
            throw e;
        }
    }
    
    /**
     * Type text into the element
     * @param text Text to type
     */
    public void type(String text) {
        try {
            Logger.debug("Typing text: " + text);
            waitForVisible();
            clear();
            sendKeys(text);
        } catch (Exception e) {
            Logger.error("Failed to type text: " + text, e);
            throw e;
        }
    }
    
    /**
     * Get the value of an input element
     * @return Value of the element
     */
    public String getValue() {
        return getAttribute("value");
    }
    
    /**
     * Set the value of an input element
     * @param value Value to set
     */
    public void setValue(String value) {
        try {
            Logger.debug("Setting value: " + value);
            waitForVisible();
            clear();
            sendKeys(value);
        } catch (Exception e) {
            Logger.error("Failed to set value: " + value, e);
            throw e;
        }
    }
    
    /**
     * Set the value of an input element using Angular's ng-model
     * @param value Value to set
     */
    public void setValueUsingAngular(String value) {
        try {
            Logger.debug("Setting value using Angular: " + value);
            waitForVisible();
            String script = "arguments[0].value=arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input'));" +
                            "arguments[0].dispatchEvent(new Event('change'));";
            ((JavascriptExecutor) driver).executeScript(script, getElement(), value);
        } catch (Exception e) {
            Logger.error("Failed to set Angular value: " + value, e);
            throw e;
        }
    }
    
    /**
     * Get the parent element of this element
     * @return BaseElement representing the parent element
     */
    public BaseElement getParentElement() {
        try {
            Logger.debug("Getting parent element");
            WebElement parent = getElement().findElement(By.xpath(".."));
            return new BaseElement(parent);
        } catch (Exception e) {
            Logger.error("Failed to get parent element", e);
            throw e;
        }
    }
    
    /**
     * Get the next sibling element matching the given tag
     * @param tagXpath XPath for the sibling tag to find
     * @return BaseElement representing the sibling
     */
    public BaseElement getNextSibling(String tagXpath) {
        try {
            Logger.debug("Getting next sibling");
            WebElement sibling = getElement().findElement(
                By.xpath("following-sibling::" + tagXpath));
            return new BaseElement(sibling);
        } catch (Exception e) {
            Logger.error("Failed to get next sibling", e);
            throw e;
        }
    }
    
    /**
     * Check if the element has a specific class
     * @param className Class name to check for
     * @return true if the element has the class
     */
    public boolean hasClass(String className) {
        String classes = getAttribute("class");
        return classes != null && classes.contains(className);
    }
    
    /**
     * Check if the element is active (has focus)
     * @return true if the element is active
     */
    public boolean isElementActive() {
        WebElement activeElement = driver.switchTo().activeElement();
        return getElement().equals(activeElement);
    }
    
    /**
     * Check if the element is disabled
     * @return true if the element is disabled
     */
    public boolean isElementDisabled() {
        String disabled = getAttribute("disabled");
        String ariaDisabled = getAttribute("aria-disabled");
        return disabled != null || "true".equals(ariaDisabled);
    }
    
    /**
     * Check if a child element is present
     * @param selector By selector for the child element
     * @return true if the child element is present
     */
    public boolean isChildElementPresent(By selector) {
        try {
            getElement().findElement(selector);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Change the style of the element to display:none
     */
    public void changeStyleToDisplayHidden() {
        try {
            Logger.debug("Changing style to display:none");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.display='none'", getElement());
        } catch (Exception e) {
            Logger.error("Failed to change style to hidden", e);
            throw e;
        }
    }
    
    /**
     * Change the style of the element to display:block
     */
    public void changeStyleToDisplay() {
        try {
            Logger.debug("Changing style to display:block");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.display='block'", getElement());
        } catch (Exception e) {
            Logger.error("Failed to change style to display", e);
            throw e;
        }
    }
    
    /**
     * Remove an event handler from the element
     * @param event Event name (e.g., "click", "change")
     */
    public void removeEventHandler(String event) {
        try {
            Logger.debug("Removing event handler: " + event);
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].on" + event + " = null", getElement());
        } catch (Exception e) {
            Logger.error("Failed to remove event handler: " + event, e);
            throw e;
        }
    }
    
    /**
     * Prevent default behavior for an event
     * @param event Event name (e.g., "click", "submit")
     */
    public void preventDefault(String event) {
        try {
            Logger.debug("Preventing default for event: " + event);
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].addEventListener('" + event + "', function(e) { e.preventDefault(); })",
                getElement());
        } catch (Exception e) {
            Logger.error("Failed to prevent default for event: " + event, e);
            throw e;
        }
    }
    
    /**
     * Get the locator used to find this element
     * @return By locator
     */
    public By getLocator() {
        return locator;
    }
    
    /**
     * Set a custom explicit wait timeout for this element
     * @param explicitWait Timeout in seconds
     */
    public void setExplicitWait(Integer explicitWait) {
        this.explicitWait = explicitWait;
        if (explicitWait != null) {
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        }
    }
    
    // WebElement interface implementation methods
    
    /**
     * Check if the element is displayed
     * @return true if the element is displayed
     */
    @Override
    public boolean isDisplayed() {
        try {
            return getElement().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if the element is enabled
     * @return true if the element is enabled
     */
    @Override
    public boolean isEnabled() {
        try {
            return getElement().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for the element to be visible
     */
    public void waitForVisible() {
        try {
            Logger.debug("Waiting for element to be visible");
            if (locator != null) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } else {
                wait.until(ExpectedConditions.visibilityOf(getElement()));
            }
        } catch (Exception e) {
            Logger.error("Element not visible after wait", e);
            throw e;
        }
    }
    
    /**
     * Wait for the element to be clickable
     */
    public void waitForClickable() {
        try {
            Logger.debug("Waiting for element to be clickable");
            if (locator != null) {
                wait.until(ExpectedConditions.elementToBeClickable(locator));
            } else {
                wait.until(ExpectedConditions.elementToBeClickable(getElement()));
            }
        } catch (Exception e) {
            Logger.error("Element not clickable after wait", e);
            throw e;
        }
    }
    
    /**
     * Scroll to the element
     */
    public void scrollTo() {
        try {
            Logger.debug("Scrolling to element");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'center'});",
                getElement());
            
            // Small pause to allow the scroll to complete
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            Logger.error("Failed to scroll to element", e);
            throw e;
        }
    }
    
    /**
     * Scroll the element into view
     */
    public void scrollIntoView() {
        try {
            Logger.debug("Scrolling element into view");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", getElement());
            
            // Small pause to allow the scroll to complete
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            Logger.error("Failed to scroll element into view", e);
            throw e;
        }
    }
    
    /**
     * Hover over the element
     */
    public void hover() {
        try {
            Logger.debug("Hovering over element");
            Actions actions = new Actions(driver);
            actions.moveToElement(getElement()).perform();
        } catch (Exception e) {
            Logger.error("Failed to hover over element", e);
            throw e;
        }
    }
    
    /**
     * Hover over the element with offset
     * @param offsetX X offset from the center
     * @param offsetY Y offset from the center
     */
    public void hover(int offsetX, int offsetY) {
        try {
            Logger.debug("Hovering over element with offset: " + offsetX + ", " + offsetY);
            Actions actions = new Actions(driver);
            actions.moveToElement(getElement(), offsetX, offsetY).perform();
        } catch (Exception e) {
            Logger.error("Failed to hover over element with offset", e);
            throw e;
        }
    }
    
    /**
     * Hover over the element using JavaScript
     */
    public void hoverUsingJavaScript() {
        try {
            Logger.debug("Hovering over element using JavaScript");
            String script = 
                "var element = arguments[0];" +
                "var mouseoverEvent = new MouseEvent('mouseover', {" +
                "  'view': window," +
                "  'bubbles': true," +
                "  'cancelable': true" +
                "});" +
                "element.dispatchEvent(mouseoverEvent);";
            
            ((JavascriptExecutor) driver).executeScript(script, getElement());
        } catch (Exception e) {
            Logger.error("Failed to hover using JavaScript", e);
            throw e;
        }
    }
    
    /**
     * Get the text of the element
     * @return Text of the element
     */
    @Override
    public String getText() {
        try {
            return getElement().getText();
        } catch (Exception e) {
            Logger.error("Failed to get text", e);
            throw e;
        }
    }
    
    /**
     * Get an attribute of the element
     * @param attribute Attribute name
     * @return Attribute value
     */
    @Override
    public String getAttribute(String attribute) {
        try {
            return getElement().getAttribute(attribute);
        } catch (Exception e) {
            Logger.error("Failed to get attribute: " + attribute, e);
            throw e;
        }
    }
    
    /**
     * Get a CSS property of the element
     * @param propertyName CSS property name
     * @return CSS property value
     */
    @Override
    public String getCssValue(String propertyName) {
        try {
            return getElement().getCssValue(propertyName);
        } catch (Exception e) {
            Logger.error("Failed to get CSS value: " + propertyName, e);
            throw e;
        }
    }
    
    /**
     * Get the font family of the element
     * @return Font family
     */
    public String getFontFamily() {
        try {
            return getCssValue("font-family");
        } catch (Exception e) {
            Logger.error("Failed to get font family", e);
            throw e;
        }
    }
    
    /**
     * Get the font size of the element
     * @return Font size
     */
    public String getFontSize() {
        try {
            return getCssValue("font-size");
        } catch (Exception e) {
            Logger.error("Failed to get font size", e);
            throw e;
        }
    }
    
    /**
     * Clear the element
     */
    @Override
    public void clear() {
        try {
            Logger.debug("Clearing element");
            getElement().clear();
        } catch (Exception e) {
            Logger.error("Failed to clear element", e);
            throw e;
        }
    }
    
    /**
     * Click the element
     */
    @Override
    public void click() {
        try {
            Logger.debug("Clicking element");
            waitForClickable();
            scrollTo();
            getElement().click();
        } catch (Exception e) {
            Logger.error("Failed to click element", e);
            throw e;
        }
    }
    
    /**
     * Click the element without scrolling
     */
    public void clickWithoutScroll() {
        try {
            Logger.debug("Clicking element without scroll");
            waitForClickable();
            getElement().click();
        } catch (Exception e) {
            Logger.error("Failed to click element without scroll", e);
            throw e;
        }
    }
    
    /**
     * Click the element using JavaScript
     */
    public void jsClick() {
        try {
            Logger.debug("Clicking element using JavaScript");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getElement());
        } catch (Exception e) {
            Logger.error("Failed to click element using JavaScript", e);
            throw e;
        }
    }
    
    /**
     * Click the element using Angular's ng-click
     */
    public void clickUsingAngular() {
        try {
            Logger.debug("Clicking element using Angular");
            String script = "angular.element(arguments[0]).triggerHandler('click')";
            ((JavascriptExecutor) driver).executeScript(script, getElement());
        } catch (Exception e) {
            Logger.error("Failed to click element using Angular", e);
            throw e;
        }
    }
    
    /**
     * Double-click the element
     */
    public void doubleClick() {
        try {
            Logger.debug("Double-clicking element");
            waitForClickable();
            scrollTo();
            Actions actions = new Actions(driver);
            actions.doubleClick(getElement()).perform();
        } catch (Exception e) {
            Logger.error("Failed to double-click element", e);
            throw e;
        }
    }
    
    /**
     * Move to the element and click
     */
    public void moveToElementAndClick() {
        try {
            Logger.debug("Moving to element and clicking");
            waitForVisible();
            Actions actions = new Actions(driver);
            actions.moveToElement(getElement()).click().build().perform();
        } catch (Exception e) {
            Logger.error("Failed to move to element and click", e);
            throw e;
        }
    }
    
    /**
     * Drag and drop this element to a target element
     * @param target Target element
     */
    public void dragAndDrop(BaseElement target) {
        try {
            Logger.debug("Dragging and dropping element");
            waitForVisible();
            target.waitForVisible();
            
            Actions actions = new Actions(driver);
            actions.dragAndDrop(getElement(), target.getElement()).perform();
        } catch (Exception e) {
            Logger.error("Failed to drag and drop element", e);
            throw e;
        }
    }
    
    /**
     * Send keys to the element
     * @param keysToSend Keys to send
     */
    @Override
    public void sendKeys(CharSequence... keysToSend) {
        try {
            Logger.debug("Sending keys to element");
            waitForVisible();
            getElement().sendKeys(keysToSend);
        } catch (Exception e) {
            Logger.error("Failed to send keys to element", e);
            throw e;
        }
    }
    
    /**
     * Send keys to the element without clearing first
     * @param keysToSend Keys to send
     */
    public void sendKeysWithoutClear(CharSequence... keysToSend) {
        try {
            Logger.debug("Sending keys without clear");
            waitForVisible();
            getElement().sendKeys(keysToSend);
        } catch (Exception e) {
            Logger.error("Failed to send keys without clear", e);
            throw e;
        }
    }
    
    /**
     * Submit the element
     */
    @Override
    public void submit() {
        try {
            Logger.debug("Submitting element");
            getElement().submit();
        } catch (Exception e) {
            Logger.error("Failed to submit element", e);
            throw e;
        }
    }
    
    /**
     * Get the tag name of the element
     * @return Tag name
     */
    @Override
    public String getTagName() {
        try {
            return getElement().getTagName();
        } catch (Exception e) {
            Logger.error("Failed to get tag name", e);
            throw e;
        }
    }
    
    /**
     * Check if the element is selected
     * @return true if the element is selected
     */
    @Override
    public boolean isSelected() {
        try {
            return getElement().isSelected();
        } catch (Exception e) {
            Logger.error("Failed to check if element is selected", e);
            throw e;
        }
    }
    
    /**
     * Get the location of the element
     * @return Point representing the location
     */
    @Override
    public Point getLocation() {
        try {
            return getElement().getLocation();
        } catch (Exception e) {
            Logger.error("Failed to get element location", e);
            throw e;
        }
    }
    
    /**
     * Get the size of the element
     * @return Dimension representing the size
     */
    @Override
    public Dimension getSize() {
        try {
            return getElement().getSize();
        } catch (Exception e) {
            Logger.error("Failed to get element size", e);
            throw e;
        }
    }
    
    /**
     * Get the rectangle of the element
     * @return Rectangle representing the element
     */
    @Override
    public Rectangle getRect() {
        try {
            return getElement().getRect();
        } catch (Exception e) {
            Logger.error("Failed to get element rectangle", e);
            throw e;
        }
    }
    
    /**
     * Take a screenshot of the element
     * @param target Output type
     * @return Screenshot as the specified type
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        try {
            return getElement().getScreenshotAs(target);
        } catch (Exception e) {
            Logger.error("Failed to take screenshot of element", e);
            throw e;
        }
    }
    
    /**
     * Find an element within this element
     * @param by Locator to find the element
     * @return WebElement found
     */
    @Override
    public WebElement findElement(By by) {
        try {
            WebElement found = getElement().findElement(by);
            return found;
        } catch (Exception e) {
            Logger.error("Failed to find element with locator: " + by, e);
            throw e;
        }
    }
    
    /**
     * Find elements within this element
     * @param by Locator to find the elements
     * @return List of WebElements found
     */
    @Override
    public List<WebElement> findElements(By by) {
        try {
            List<WebElement> foundElements = getElement().findElements(by);
            return foundElements;
        } catch (Exception e) {
            Logger.error("Failed to find elements with locator: " + by, e);
            throw e;
        }
    }
    
    /**
     * Check if an attribute is present
     * @param attribute Attribute name
     * @return true if the attribute is present
     */
    public boolean isAttributePresent(String attribute) {
        try {
            String value = getElement().getAttribute(attribute);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }
} 