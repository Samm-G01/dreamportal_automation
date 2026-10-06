package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.TotalDreamsPage;

public class TotalDreamsPageTest extends BaseTest {

    @Test
    public void verifyDreamSummary() {

        TotalDreamsPage totalPage = new TotalDreamsPage(driver);

        totalPage.openTotalDreamsPage();

        String goodDreams = totalPage.getGoodDreams();
        String badDreams = totalPage.getBadDreams();
        String totalDreams = totalPage.getTotalDreams();
        String recurringDreams = totalPage.getRecurringDreams();

        System.out.println("Good Dreams: " + goodDreams);
        System.out.println("Bad Dreams: " + badDreams);
        System.out.println("Total Dreams: " + totalDreams);
        System.out.println("Recurring Dreams: " + recurringDreams);

        Assert.assertEquals(
        goodDreams,
        "6",
        "Good Dreams count is incorrect"
);

Assert.assertEquals(
        badDreams,
        "4",
        "Bad Dreams count is incorrect"
);

Assert.assertEquals(
        totalDreams,
        "10",
        "Total Dreams count is incorrect"
);

Assert.assertEquals(
        recurringDreams,
        "2",
        "Recurring Dreams count is incorrect"
);
    }
}