package tests;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.DiaryPage;
import utils.AIClassifier;

public class AIValidationTest extends BaseTest {

    // Minimum agreement required when a real AI model answered.
    // Lower it with: mvn clean test -Dai.min.agreement=0.5
    private static final double MIN_AGREEMENT =
            Double.parseDouble(System.getProperty("ai.min.agreement", "0.6"));

    @Test
    public void aiClassificationShouldMatchDiaryTypes() {

        DiaryPage diary = new DiaryPage(driver);
        diary.openDiaryPage();

        AIClassifier classifier = new AIClassifier();
        int total = diary.getDreamCount();
        Assert.assertTrue(total > 0, "Diary has no dreams to classify");

        List<String> lines = new ArrayList<>();
        int matches = 0;
        boolean allFromApi = true;

        for (int i = 0; i < total; i++) {
            String name = diary.getDreamName(i);
            String expected = diary.getDreamType(i);

            AIClassifier.Result result = classifier.classify(name);
            allFromApi &= result.source() == AIClassifier.Source.LLM_API;

            Assert.assertTrue(
                    result.label().equals("Good") || result.label().equals("Bad"),
                    "Invalid label returned for: " + name);

            boolean match = result.label().equalsIgnoreCase(expected);
            if (match) {
                matches++;
            }

            lines.add(String.format("%-35s table=%-5s ai=%-5s source=%-13s %s",
                    name, expected, result.label(), result.source(),
                    match ? "MATCH" : "MISMATCH"));
        }

        double agreement = (double) matches / total;

        
            

        // Only enforce agreement when a real AI model answered every dream
        if (allFromApi) {
            Assert.assertTrue(agreement >= MIN_AGREEMENT,
                    "AI agreement " + (int) (agreement * 100) + "% is below required "
                            + (int) (MIN_AGREEMENT * 100) + "%");
        }
    }
}