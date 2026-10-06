package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DiaryPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public DiaryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openDiaryPage() {
        driver.get("https://arjitnigam.github.io/myDreams/dreams-diary.html");
    }

    public List<WebElement> getDreamRows() {

        return driver.findElements(
                By.cssSelector("table tbody tr")
        );
    }

    public int getDreamCount() {
        return getDreamRows().size();
    }

    public String getDreamName(int rowNumber) {

        WebElement row = getDreamRows().get(rowNumber);

        return row.findElements(By.tagName("td"))
                .get(0)
                .getText()
                .trim();
    }

    public String getDaysAgo(int rowNumber) {

        WebElement row = getDreamRows().get(rowNumber);

        return row.findElements(By.tagName("td"))
                .get(1)
                .getText()
                .trim();
    }

    public String getDreamType(int rowNumber) {

        WebElement row = getDreamRows().get(rowNumber);

        return row.findElements(By.tagName("td"))
                .get(2)
                .getText()
                .trim();
    }
    public int countDreamByName(String dreamName) {

    int count = 0;

    for (WebElement row : getDreamRows()) {

        String name = row.findElements(By.tagName("td"))
                .get(0)
                .getText()
                .trim();

        if (name.equals(dreamName)) {
            count++;
        }
    }

    return count;
}
}