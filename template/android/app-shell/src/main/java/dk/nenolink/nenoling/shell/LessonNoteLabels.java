package dk.nenolink.nenoling.shell;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Maps language-pack note keys to product-facing labels without engine language conditionals. */
public final class LessonNoteLabels {
    private final Map<String, String> labels;

    public LessonNoteLabels(Map<String, String> labels) {
        this.labels = Collections.unmodifiableMap(new LinkedHashMap<>(labels));
    }

    public Map<String, String> all() {
        return labels;
    }

    public static LessonNoteLabels defaults() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("grammar", "Grammar");
        values.put("cultural", "Cultural context");
        values.put("digital", "Digital context");
        values.put("pronunciation", "Pronunciation");
        return new LessonNoteLabels(values);
    }
}
