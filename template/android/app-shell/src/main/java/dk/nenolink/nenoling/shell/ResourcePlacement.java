package dk.nenolink.nenoling.shell;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dk.nenolink.nenoling.content.ContentModels.Lesson;

/** Language/product pack decides where a practical resource collection is surfaced. */
public final class ResourcePlacement {
    private final Set<String> lessonTags;
    private final Set<String> lessonIds;
    public final boolean showOnModules;

    public ResourcePlacement(boolean showOnModules, Set<String> lessonTags, Set<String> lessonIds) {
        this.showOnModules = showOnModules;
        this.lessonTags = immutable(lessonTags);
        this.lessonIds = immutable(lessonIds);
    }

    public boolean matches(Lesson lesson) {
        if (lesson == null) return false;
        if (lessonIds.contains(lesson.id)) return true;
        for (String tag : lesson.tags) if (lessonTags.contains(tag)) return true;
        return false;
    }

    private static Set<String> immutable(Set<String> values) {
        return values == null ? Collections.emptySet() :
                Collections.unmodifiableSet(new HashSet<>(values));
    }

    public static ResourcePlacement modulesOnly() {
        return new ResourcePlacement(true, Collections.emptySet(), Collections.emptySet());
    }
}
