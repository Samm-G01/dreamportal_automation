package pages;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openWebsite() {
        driver.get("https://arjitnigam.github.io/myDreams/");
    }

    public WebElement getMyDreamsButton() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[contains(.,'My Dreams')]")
                )
        );
    }

    public boolean isMyDreamsButtonVisible() {
        return getMyDreamsButton().isDisplayed();
    }

    public void clickMyDreams() {
        getMyDreamsButton().click();
    }

    public Set<String> getAllWindowHandles() {
        return driver.getWindowHandles();
    }
    public boolean isLoadingAnimationVisible() {
    return driver.findElement(
            By.id("loadingAnimation")
    ).isDisplayed();
}
public void waitForLoadingAnimationToDisappear() {

    wait.until(
            ExpectedConditions.invisibilityOfElementLocated(
                    By.id("loadingAnimation")
            )
    );
}
}