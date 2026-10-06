package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.DiaryPage;

public class DiaryPageTest extends BaseTest {

    @Test
    public void verifyThereAreExactly10Dreams() {

        DiaryPage diaryPage = new DiaryPage(driver);

        diaryPage.openDiaryPage();

        int dreamCount = diaryPage.getDreamCount();

        System.out.println("Number of dreams found: " + dreamCount);

        Assert.assertEquals(
                dreamCount,
                10,
                "There should be exactly 10 dream entries"
        );
    }

    @Test
    public void verifyEveryDreamRowIsComplete() {

        DiaryPage diaryPage = new DiaryPage(driver);

        diaryPage.openDiaryPage();

        int dreamCount = diaryPage.getDreamCount();

        for (int i = 0; i < dreamCount; i++) {

            String dreamName = diaryPage.getDreamName(i);
            String daysAgo = diaryPage.getDaysAgo(i);
            String dreamType = diaryPage.getDreamType(i);

            System.out.println(
                    "Row " + (i + 1) +
                    " | Name: " + dreamName +
                    " | Days Ago: " + daysAgo +
                    " | Type: " + dreamType
            );

            Assert.assertFalse(
                    dreamName.isEmpty(),
                    "Dream name is empty in row " + (i + 1)
            );

            Assert.assertFalse(
                    daysAgo.isEmpty(),
                    "Days Ago is empty in row " + (i + 1)
            );

            Assert.assertFalse(
                    dreamType.isEmpty(),
                    "Dream Type is empty in row " + (i + 1)
            );

            Assert.assertTrue(
                    dreamType.equals("Good") || dreamType.equals("Bad"),
                    "Dream Type must be Good or Bad in row " + (i + 1)
            );
        }
    }
    @Test
public void verifyRecurringDreams() {

    DiaryPage diaryPage = new DiaryPage(driver);

    diaryPage.openDiaryPage();

    int flyingDreams =
            diaryPage.countDreamByName("Flying over mountains");

    int mazeDreams =
            diaryPage.countDreamByName("Lost in maze");

    System.out.println(
            "Flying over mountains appears: " + flyingDreams + " times"
    );

    System.out.println(
            "Lost in maze appears: " + mazeDreams + " times"
    );

    Assert.assertTrue(
            flyingDreams > 1,
            "Flying over mountains should be a recurring dream"
    );

    Assert.assertTrue(
            mazeDreams > 1,
            "Lost in maze should be a recurring dream"
    );
}
}