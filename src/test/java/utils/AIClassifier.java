package utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIClassifier {

    private static final String OLLAMA_URL =
            "http://localhost:11434/api/generate";

    private static final String MODEL =
            "llama3.2:3b";

    public static String classifyDream(String dreamName)
            throws Exception {

        String prompt =
                "Classify this dream as Good or Bad. " +
                "Return only one word: Good or Bad.\n\n" +
                "Dream: " + dreamName;

        String jsonBody =
                "{"
                + "\"model\":\"" + MODEL + "\","
                + "\"prompt\":\"" + escapeJson(prompt) + "\","
                + "\"stream\":false"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        String responseBody = response.body();

        String result = extractResponse(responseBody);

        return result.trim();
    }

    private static String extractResponse(String json) {

        String marker = "\"response\":\"";

        int start = json.indexOf(marker);

        if (start == -1) {
            throw new RuntimeException(
                    "Could not find AI response: " + json
            );
        }

        start += marker.length();

        int end = json.indexOf("\"", start);

        if (end == -1) {
            throw new RuntimeException(
                    "Could not parse AI response: " + json
            );
        }

        return json.substring(start, end);
    }

    private static String escapeJson(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}