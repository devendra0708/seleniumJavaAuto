# Salesforce Automation Framework

A robust, reusable Selenium automation framework for testing Salesforce applications, built with Java, TestNG, and Selenium WebDriver.

## Features

- **Page Object Model (POM)** design pattern
- **TestNG** for test orchestration and assertions
- **WebDriverManager** for automatic driver management
- **Cross-browser testing** capability
- **Configuration management** for test data and settings
- **Salesforce-specific utility methods** for handling Lightning UI
- **Reusable components** for common Salesforce functionality
- **Detailed logging** and reporting

## Project Structure

```
├── src
│   └── test
│       ├── java
│       │   └── com
│       │       └── test
│       │           └── automation
│       │               ├── base
│       │               │   ├── BasePage.java
│       │               │   └── BaseTest.java
│       │               ├── pages
│       │               │   ├── LoginPage.java
│       │               │   ├── SalesforceHomePage.java
│       │               │   └── LeadsPage.java
│       │               ├── tests
│       │               │   ├── SingleLoginTest.java
│       │               │   └── LeadTest.java
│       │               └── utils
│       │                   ├── ConfigReader.java
│       │                   └── SalesforceUtils.java
│       └── resources
│           └── config.properties
├── pom.xml
├── testng.xml
└── README.md
```

## Setup Instructions

1. **Prerequisites**:
   - Java JDK 11 or higher
   - Apache Maven 3.6.0 or higher
   - Chrome/Firefox/Edge browser

2. **Configuration**:
   - Update `config.properties` with your Salesforce credentials and environment details
   - Customize the `testng.xml` file to include/exclude specific tests

3. **Running Tests**:
   ```
   mvn clean test
   ```

   To run a specific test class:
   ```
   mvn clean test -Dtest=SingleLoginTest
   ```

## Creating New Tests

1. Create a new page object in the `pages` package for each new Salesforce page
2. Extend the `BasePage` class to inherit common functionality
3. Create a new test class in the `tests` package
4. Add your test class to the `testng.xml` file

## Best Practices

- Keep locators in the page classes, not in test classes
- Use proper waiting mechanisms from the BasePage class
- Create reusable methods in the page objects for complex operations
- Use the SalesforceUtils class for Salesforce-specific functionality
- Generate unique test data for test independence
- Always clean up test data after tests when possible

## Known Issues and Limitations

- Salesforce Lightning UI may change frequently, requiring locator updates
- Some Salesforce operations might be slow and require additional waits
- iFrames and shadow DOM elements may require special handling

## Contributing

1. Create a feature branch
2. Add your changes
3. Add appropriate tests
4. Ensure all tests pass
5. Submit a pull request 