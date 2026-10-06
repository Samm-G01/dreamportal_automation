package tests;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.DriverFactory;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        if (!result.isSuccess() && driver != null) {

            try {
                Path screenshotFolder =
                        Paths.get("test-output", "screenshots");

                Files.createDirectories(screenshotFolder);

                String fileName =
                        result.getMethod().getMethodName()
                        + "_" + System.currentTimeMillis()
                        + ".png";

                File source =
                        ((TakesScreenshot) driver)
                                .getScreenshotAs(OutputType.FILE);

                File destination =
                        screenshotFolder.resolve(fileName).toFile();

                Files.copy(
                        source.toPath(),
                        destination.toPath()
                );

                System.out.println(
                        "[INFO] Failure screenshot saved: "
                        + destination.getAbsolutePath()
                );

            } catch (Exception e) {

                System.out.println(
                        "[WARNING] Could not save screenshot: "
                        + e.getMessage()
                );
            }
        }

        DriverFactory.quitDriver();
    }
}