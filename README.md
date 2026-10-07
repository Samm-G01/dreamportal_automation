# Dream Portal QA Automation

## About this project

This project has automated UI tests for the Dream Portal website, written with Selenium WebDriver, Java and TestNG.

Website: https://arjitnigam.github.io/myDreams/

The tests check:

* the home page and its loading animation
* the Dream Diary table
* the Dream Summary numbers and recurring dreams
* (bonus) whether an AI model agrees with the Good/Bad type shown in the diary

## Tech used

* Java 17
* Selenium WebDriver 4.34.0
* TestNG 7.10.2
* Maven
* WebDriverManager (downloads ChromeDriver automatically)
* Gson (reads the AI response)
* Groq API for the AI check
* Chrome browser

## Project structure

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

## How it is built (Page Object Model)

Each page of the website has its own class in the `pages` folder. These classes find elements and read data from the page. The test classes only decide what is expected and do the assertions. This way, if something on a page changes, I only have to fix it in one place.

* **HomePage** - opens the site, handles the My Dreams button and the loading animation
* **DiaryPage** - reads the dream table (count, name, days ago, type) and counts how often a dream appears
* **TotalDreamsPage** - reads the Good, Bad, Total and Recurring dream numbers
* **DriverFactory** - starts and closes Chrome
* **BaseTest** - common setup and cleanup for every test, and takes a screenshot when a test fails

## What the tests check

**Home page**

1. The My Dreams button is visible.
2. The loading animation appears and then disappears.
3. The main content shows after loading.
4. Clicking My Dreams opens the Dream Diary and Dream Summary pages in new tabs.

**Dream Diary**

1. There are exactly 10 dreams.
2. Every row has a Dream Name, Days Ago and Dream Type.
3. Dream Type is only Good or Bad.
4. "Flying over mountains" and "Lost in maze" are recurring (they appear more than once).

**Dream Summary**

* Good Dreams: 6
* Bad Dreams: 4
* Total Dreams: 10
* Recurring Dreams: 2

**AI check (bonus)**

The AI's Good/Bad answer for each dream name is compared with the type in the diary table.

## AI check

For the bonus part, `AIValidationTest` does this:

1. Opens the Dream Diary and reads each dream name and its type.
2. Sends each dream name to an AI model and asks for one word: Good or Bad.
3. Compares the AI answer with the type in the table.

I used the Groq API with the `openai/gpt-oss-20b` model. In my run, all 10 dreams matched.

Since AI answers can sometimes differ from the table, the test passes if at least 60% of the answers match.

**To run it with the AI**

1. Make a free API key at https://console.groq.com
2. Set it in the terminal before running the tests. The key is not saved in the project.

PowerShell:

```text
$env:AI_API_KEY="your_key_here"
```

Command Prompt:

```text
set AI_API_KEY=your_key_here
```

3. Run `mvn clean test` in the same terminal.

**If there is no key**

If the key is missing or the API does not respond, the code switches to a simple keyword check (for example, "monster" means Bad) so the other tests still run. This backup is not AI, and the match check is only enforced when the AI model answered.

**Why Groq**

I first tried a local Ollama model (llama3.2), but it crashed on my Windows machine. My OpenAI key was also out of quota, so I switched to Groq.

## Running the tests

You need Java 17, Maven and Chrome. Open a terminal in the project folder (where `pom.xml` is) and run:

```bash
mvn clean test
```

To run only the AI test:

```bash
mvn test -Dtest=AIValidationTest
```

A good run ends with:

```text
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Report

I used the Maven Surefire report. To create it:

```bash
mvn clean test surefire-report:report
```

Open `target/site/surefire-report.html` in a browser.

## Failure screenshots

If a test fails, `BaseTest` saves a screenshot in `test-output/screenshots/`. The file name has the test name and a timestamp.

## Notes

* I used explicit waits (not fixed sleeps) for the loading animation and the new tabs.
* You may see a warning like "Unable to find CDP implementation" in the console. It only means my Chrome is newer than the DevTools version in Selenium. It does not affect these tests.

## What I would improve next

* Add Allure reports and proper logging
* Run the tests in headless mode and in CI
* Move the website URL into a config file