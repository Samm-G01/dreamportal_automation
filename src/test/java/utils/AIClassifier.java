package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Set;
import java.util.logging.Logger;

public class AIClassifier {

    private static final Logger LOG = Logger.getLogger(AIClassifier.class.getName());

    public enum Source { LLM_API, OFFLINE_RULES }

    public record Result(String dream, String label, Source source) { }

    private static final String BASE_URL = env("AI_BASE_URL", "https://api.groq.com/openai/v1");
    private static final String MODEL =    env("AI_MODEL", "openai/gpt-oss-20b");
    private static final String API_KEY = env("AI_API_KEY", "");

    private static final Set<String> BAD_WORDS = Set.of(
            "monster", "chase", "chased", "lost", "maze", "falling", "fall", "nightmare",
            "dark", "drown", "drowning", "trapped", "ghost", "scary", "death", "dead",
            "fire", "war", "attack", "attacked", "naked", "teeth", "exam", "late",
            "alone", "storm", "zombie", "spider", "snake", "crash", "fear", "haunted");

    private static final Set<String> GOOD_WORDS = Set.of(
            "flying", "fly", "beach", "dancing", "dance", "winning", "won", "treasure",
            "garden", "friends", "friend", "mountains", "ocean", "party", "family",
            "rainbow", "swimming", "happy", "love", "music", "magic", "castle",
            "birthday", "festival", "adventure", "peaceful", "forest");

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(8))
            .build();

    public boolean isApiConfigured() {
        return !API_KEY.isBlank();
    }

    public Result classify(String dream) {
        if (isApiConfigured()) {
            try {
                String label = callApi(dream);
                if (label != null) {
                    return new Result(dream, label, Source.LLM_API);
                }
            } catch (Exception e) {
                LOG.warning("AI API call failed, using offline rules: " + e.getMessage());
            }
        }
        return new Result(dream, classifyOffline(dream), Source.OFFLINE_RULES);
    }

    private String callApi(String dream) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("model", MODEL);
        body.addProperty("temperature", 0);
        body.addProperty("max_tokens", 300);

        JsonArray messages = new JsonArray();
        messages.add(message("system",
                "You classify dream titles. Reply with exactly one word: Good or Bad."));
        messages.add(message("user", "Dream: " + dream));
        body.add("messages", messages);

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/chat/completions"))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            LOG.warning("AI API returned HTTP " + response.statusCode());
            return null;
        }

        String text = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();

        return normalise(text);
    }

    private static JsonObject message(String role, String content) {
        JsonObject m = new JsonObject();
        m.addProperty("role", role);
        m.addProperty("content", content);
        return m;
    }

    private static String normalise(String text) {
        String t = text == null ? "" : text.toLowerCase();
        boolean good = t.contains("good");
        boolean bad = t.contains("bad");
        if (good && !bad) return "Good";
        if (bad && !good) return "Bad";
        return null;
    }

    static String classifyOffline(String dream) {
        int score = 0;
        for (String word : dream.toLowerCase().split("[^a-z]+")) {
            if (BAD_WORDS.contains(word)) score--;
            if (GOOD_WORDS.contains(word)) score++;
        }
        return score < 0 ? "Bad" : "Good";
    }

    private static String env(String name, String fallback) {
        String v = System.getenv(name);
        return (v == null || v.isBlank()) ? fallback : v;
    }
}