package dk.nenolink.nenoling.config;

/**
 * Product-supplied configuration for the reusable Nenoling engine.
 * No language pair, locale, course ID or site URL belongs in generic engine code.
 */
public final class EngineConfig {
    public final String courseAsset;
    public final String resourcesAsset;
    public final int expectedQuizQuestionsPerLesson;

    public EngineConfig(String courseAsset, String resourcesAsset, int expectedQuizQuestionsPerLesson) {
        if (blank(courseAsset)) throw new IllegalArgumentException("courseAsset required");
        if (expectedQuizQuestionsPerLesson < 0) {
            throw new IllegalArgumentException("expectedQuizQuestionsPerLesson must be >= 0");
        }
        this.courseAsset = courseAsset;
        this.resourcesAsset = resourcesAsset == null ? "" : resourcesAsset.trim();
        this.expectedQuizQuestionsPerLesson = expectedQuizQuestionsPerLesson;
    }

    public boolean hasResources() {
        return !resourcesAsset.isEmpty();
    }

    private static boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
