package tests;

import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.HomePage;

public class HomePageTest extends BaseTest {

    @Test
    public void verifyMyDreamsButtonIsVisible() {

        HomePage homePage = new HomePage(driver);

        homePage.openWebsite();

        Assert.assertTrue(
                homePage.isMyDreamsButtonVisible(),
                "My Dreams button should be visible"
        );

        System.out.println("My Dreams button is visible.");
    }

    @Test
public void verifyMyDreamsOpensNewTabs() {

    HomePage homePage = new HomePage(driver);

    homePage.openWebsite();

    String originalWindow = driver.getWindowHandle();

    homePage.clickMyDreams();

    WebDriverWait wait = new WebDriverWait(
            driver,
            Duration.ofSeconds(10)
    );

    wait.until(d -> d.getWindowHandles().size() == 3);

    boolean diaryPageOpened = false;
    boolean totalPageOpened = false;

    for (String windowHandle : driver.getWindowHandles()) {

        if (windowHandle.equals(originalWindow)) {
            continue;
        }

        driver.switchTo().window(windowHandle);

        String currentUrl = driver.getCurrentUrl();

        System.out.println("Opened page: " + currentUrl);

        if (currentUrl.contains("dreams-diary.html")) {
            diaryPageOpened = true;
        }

        if (currentUrl.contains("dreams-total.html")) {
            totalPageOpened = true;
        }
    }

    Assert.assertTrue(
            diaryPageOpened,
            "Dreams Diary page should open in a new tab"
    );

    Assert.assertTrue(
            totalPageOpened,
            "Dreams Total page should open in a new tab"
    );
}
    @Test
public void verifyLoadingAnimationAppearsAndDisappears() {

    HomePage homePage = new HomePage(driver);

    homePage.openWebsite();

    Assert.assertTrue(
            homePage.isLoadingAnimationVisible(),
            "Loading animation should be visible when the page loads"
    );

    System.out.println("[INFO] Loading animation is visible.");

    homePage.waitForLoadingAnimationToDisappear();

    System.out.println("[INFO] Loading animation disappeared.");

    Assert.assertTrue(
            homePage.isMyDreamsButtonVisible(),
            "Main page content should be visible after loading animation disappears"
    );

    System.out.println("[INFO] Main page content is visible.");
}
}