package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TotalDreamsPage {

    private WebDriver driver;

    public TotalDreamsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openTotalDreamsPage() {
        driver.get("https://arjitnigam.github.io/myDreams/dreams-total.html");
    }

    public String getGoodDreams() {
        return driver.findElement(
                By.xpath("//*[contains(text(),'Good Dreams')]/following-sibling::*[1]")
        ).getText().trim();
    }

    public String getBadDreams() {
        return driver.findElement(
                By.xpath("//*[contains(text(),'Bad Dreams')]/following-sibling::*[1]")
        ).getText().trim();
    }

    public String getTotalDreams() {
        return driver.findElement(
                By.xpath("//*[contains(text(),'Total Dreams')]/following-sibling::*[1]")
        ).getText().trim();
    }

    public String getRecurringDreams() {
        return driver.findElement(
                By.xpath("//*[contains(text(),'Recurring Dreams')]/following-sibling::*[1]")
        ).getText().trim();
    }
}