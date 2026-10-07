# Dream Portal QA Automation

## Overview

This project contains automated UI tests for the Dream Portal website using Selenium WebDriver with Java and TestNG.

The automation verifies the main website flow, Dream Diary data, Dream Summary information, recurring dreams, and loading behavior. It also includes an AI-based validation that classifies each dream name as Good or Bad using a Large Language Model (LLM) and compares the result with the Dream Diary table.

**Application:** https://arjitnigam.github.io/myDreams/

## Tech Stack

* Java 17
* Selenium WebDriver 4.34.0
* TestNG 7.10.2
* Maven
* WebDriverManager
* Gson (JSON parsing for the AI API response)
* Groq API with the `openai/gpt-oss-20b` model (AI validation)
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
        │   ├── TotalDreamsPageTest.java
        │   └── AIValidationTest.java
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

### AIClassifier

Classifies a dream name as Good or Bad. See the **AI Validation** section below.

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

### AI Validation (Bonus)

Verify that the Good/Bad type of each dream name predicted by an AI model matches the Dream Type shown in the Dream Diary table.

## AI Validation

`AIValidationTest` reads every dream name from the Dream Diary table, asks an LLM to classify it as `Good` or `Bad`, and compares the answer with the Dream Type in the table.

**How it works**

1. The test opens the Dream Diary page and reads each dream name and its type.
2. `AIClassifier` sends the dream name to the Groq API (OpenAI-compatible chat endpoint) using the `openai/gpt-oss-20b` model, asking for a single word: `Good` or `Bad`.
3. The AI answer is compared with the table value, and each row is printed as `MATCH` or `MISMATCH`.
4. Because AI answers can be subjective, the test passes when the agreement is at least 60 percent (configurable). In the latest run, **10 out of 10** dream types matched.

Example console output:

```text
Monster chase     table=Bad   ai=Bad   source=LLM_API   MATCH
Agreement: 10/10 (100%) | AI model used: yes
```

**Setup**

1. Create a free API key at https://console.groq.com
2. Set the key as an environment variable. The key is never stored in the repository.

PowerShell:

```powershell
$env:AI_API_KEY="your_key_here"
```

Command Prompt:

```text
set AI_API_KEY=your_key_here
```

macOS / Linux:

```bash
export AI_API_KEY=your_key_here
```

3. Run the tests in the same terminal.

**Optional settings**

| Variable / property | Purpose | Default |
| ------------------- | ------- | ------- |
| `AI_API_KEY` | API key | none |
| `AI_MODEL` | Model name | `openai/gpt-oss-20b` |
| `AI_BASE_URL` | OpenAI-compatible endpoint | `https://api.groq.com/openai/v1` |
| `-Dai.min.agreement` | Minimum agreement (0 to 1) | `0.6` |

**Fallback behaviour**

If no API key is set, or the API call fails, `AIClassifier` falls back to simple keyword rules so that the test suite does not break. The fallback is **not** an AI model. The console output states which source produced each answer (`LLM_API` or `OFFLINE_RULES`), and the agreement check is enforced only when every answer came from the AI model.

**Earlier attempts**

A local Ollama model (`llama3.2`) was tried first, but the Ollama runtime failed with a Windows `llama-server` process error. A hosted OpenAI key was also out of quota. The Groq API was used instead.

## Running the Tests

Make sure Java 17 and Maven are installed.

Open a terminal in the project root (the folder containing `pom.xml`).

Run all tests:

```bash
mvn clean test
```

Run only the AI validation test:

```bash
mvn test -Dtest=AIValidationTest
```

A successful execution should show:

```text
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
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

The Selenium automation suite passes successfully:

```text
8 Tests
0 Failures
0 Errors
0 Skipped
BUILD SUCCESS
```

## Notes

* The tests use explicit waits where synchronization is required, particularly for the loading animation and newly opened browser tabs.
* ChromeDriver is managed using WebDriverManager, so a driver executable does not need to be manually downloaded.
* The console may show a warning such as `Unable to find CDP implementation matching <version>`. This only means the installed Chrome is newer than the DevTools protocol bundled with Selenium. It does not affect these tests.

## Possible Future Improvements

* Allure reporting with screenshots attached to the report
* Headless execution and a GitHub Actions workflow
* Cross-browser runs
* Moving the base URL to a configuration file