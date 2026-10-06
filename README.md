# Dream Portal QA Automation

## Overview

This project contains automated UI tests for the Dream Portal website using Selenium WebDriver with Java and TestNG.

The automation verifies the main website flow, Dream Diary data, Dream Summary information, recurring dreams, and loading behavior.

**Application:** https://arjitnigam.github.io/myDreams/

## Tech Stack

* Java 17
* Selenium WebDriver 4.34.0
* TestNG 7.10.2
* Maven
* WebDriverManager
* Chrome Browser

## Project Structure

```text
src
└── test
    └── java
        ├── pages
        │   ├── HomePage.java
        │   ├── DiaryPage.java
        │   └── TotalDreamsPage.java
        │
        ├── tests
        │   ├── BaseTest.java
        │   ├── HomePageTest.java
        │   ├── DiaryPageTest.java
        │   └── TotalDreamsPageTest.java
        │
        └── utils
            ├── DriverFactory.java
            └── AIClassifier.java
```

## Page Object Model

The project follows the Page Object Model (POM) design pattern.

### HomePage

Handles:

* Opening the Dream Portal
* My Dreams button
* Loading animation
* Opening Dream Diary and Dream Summary pages

### DiaryPage

Handles:

* Dream table
* Dream count
* Dream name
* Days ago
* Dream type
* Recurring dream validation

### TotalDreamsPage

Handles:

* Good Dreams count
* Bad Dreams count
* Total Dreams count
* Recurring Dreams count

### DriverFactory

Manages WebDriver creation and cleanup.

### BaseTest

Provides common test setup and teardown.

Failure screenshots are automatically captured and stored under:

```text
test-output/screenshots/
```

## Test Coverage

### Home Page

1. Verify the My Dreams button is visible.
2. Verify the loading animation appears and disappears.
3. Verify the main page content becomes available after loading.
4. Verify My Dreams opens the Dream Diary and Dream Summary pages in new tabs.

### Dream Diary

1. Verify exactly 10 dream entries are present.
2. Verify every row contains:

   * Dream Name
   * Days Ago
   * Dream Type
3. Verify Dream Type is either Good or Bad.
4. Verify recurring dreams:

   * Flying over mountains
   * Lost in maze

### Dream Summary

Verify:

| Metric           | Expected |
| ---------------- | -------: |
| Good Dreams      |        6 |
| Bad Dreams       |        4 |
| Total Dreams     |       10 |
| Recurring Dreams |        2 |

## Running the Tests

Make sure Java and Maven are installed.

Open a terminal in the project root:

```text
C:\DreamPortal_Wingify\dreamportal_wingify\dreamportal_wingify
```

Run all tests:

```bash
mvn clean test
```

A successful execution should show:

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Test Reporting

Maven Surefire is used for test execution and reporting.

Generate the HTML report using:

```bash
mvn clean test surefire-report:report
```

The generated report is available at:

```text
target/site/surefire-report.html
```

## Failure Screenshot

When a test fails, `BaseTest` automatically captures a screenshot.

Screenshots are saved under:

```text
test-output/screenshots/
```

The screenshot filename contains the failed test method name and timestamp.

## AI Classification

An optional AI classification component was explored using Ollama with the local `llama3.2` model.

The purpose of the AI component is to classify dream names as either:

```text
Good
```

or

```text
Bad
```

and compare the AI prediction with the Dream Diary classification.

The local Ollama runtime was successfully installed, but the model runtime encountered a Windows `llama-server` process error during API execution. Therefore, the AI classification is kept as an optional component and is not required for the core Selenium test suite.

## Design Approach

The automation separates test logic from page interaction logic using Page Object Model.

Tests are responsible for:

* Defining the expected behavior
* Performing assertions
* Logging test information

Page classes are responsible for:

* Locating web elements
* Performing page actions
* Reading page data

This separation makes the test code easier to maintain when the UI changes.

## Current Test Result

The core Selenium automation suite passes successfully:

```text
7 Tests
0 Failures
0 Errors
0 Skipped
BUILD SUCCESS
```

## Notes

The tests use explicit waits where synchronization is required, particularly for the loading animation and newly opened browser tabs.

ChromeDriver is managed using WebDriverManager, so a driver executable does not need to be manually downloaded.
