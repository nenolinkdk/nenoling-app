package dk.nenolink.nenoling.content;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import dk.nenolink.nenoling.config.EngineConfig;
import dk.nenolink.nenoling.content.ContentModels.Answer;
import dk.nenolink.nenoling.content.ContentModels.Course;
import dk.nenolink.nenoling.content.ContentModels.Item;
import dk.nenolink.nenoling.content.ContentModels.Language;
import dk.nenolink.nenoling.content.ContentModels.Lesson;
import dk.nenolink.nenoling.content.ContentModels.Module;
import dk.nenolink.nenoling.content.ContentModels.Note;
import dk.nenolink.nenoling.content.ContentModels.Question;
import dk.nenolink.nenoling.content.ContentModels.Quiz;
import dk.nenolink.nenoling.content.ContentModels.SpeechDefaults;
import dk.nenolink.nenoling.content.ContentModels.SpeechSpec;
import dk.nenolink.nenoling.content.ContentModels.TextPair;

/** JSON repository with no language-pair-specific asset path or locale. */
public final class ContentRepository {
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-z0-9]+(?:[.-][a-z0-9]+)*$");
    private static final Pattern TAG_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    private static final Set<String> MODULE_TYPES = setOf("level", "children", "grammar", "quiz");
    private static final Set<String> ITEM_TYPES = setOf("phrase", "dialogue-turn", "grammar-example", "digital-scenario");
    private static final Set<String> NOTE_TYPES = setOf("grammar", "cultural", "digital", "pronunciation");

    private final Context context;
    private final EngineConfig config;
    private final Set<String> ids = new HashSet<>();
    private String assetName;

    public ContentRepository(Context context, EngineConfig config) {
        this.context = context.getApplicationContext();
        this.config = config;
    }

    public Course loadProductionCourse() throws ContentContractException {
        Course course = loadCourse(config.courseAsset);
        QuizIntegrity.requireEveryLessonQuiz(course, config.expectedQuizQuestionsPerLesson);
        return course;
    }

    public Course loadCourse(String name) throws ContentContractException {
        assetName = name;
        ids.clear();
        try {
            JSONObject root = new JSONObject(readAsset(name));
            requireOnly(root, "$", "schemaVersion", "contentVersion", "course");
            int schemaVersion = requiredPositiveInt(root, "schemaVersion", "$");
            if (schemaVersion != 1) fail("$.schemaVersion", "only version 1 is supported");
            String contentVersion = requiredString(root, "contentVersion", "$");
            JSONObject courseJson = requiredObject(root, "course", "$");
            requireOnly(courseJson, "$.course", "id", "courseLocale", "languages", "tts", "title", "modules");
            String courseId = requiredId(courseJson, "id", "$.course");
            String courseLocale = requiredString(courseJson, "courseLocale", "$.course");
            JSONObject languages = requiredObject(courseJson, "languages", "$.course");
            requireOnly(languages, "$.course.languages", "support", "target");
            Language support = parseLanguage(requiredObject(languages, "support", "$.course.languages"), "$.course.languages.support");
            Language target = parseLanguage(requiredObject(languages, "target", "$.course.languages"), "$.course.languages.target");
            if (!courseLocale.equals(support.locale)) fail("$.course.courseLocale", "must equal support locale");
            SpeechDefaults speech = parseSpeechDefaults(requiredObject(courseJson, "tts", "$.course"), support, target);
            TextPair title = parsePair(requiredObject(courseJson, "title", "$.course"), "$.course.title");
            List<Module> modules = parseModules(requiredArray(courseJson, "modules", "$.course"), support, target);
            if (modules.isEmpty()) fail("$.course.modules", "must contain at least one module");
            return new Course(schemaVersion, contentVersion, courseId, courseLocale, support, target, speech, title, modules);
        } catch (IOException | JSONException exception) {
            throw new ContentContractException(name + ": invalid JSON: " + exception.getMessage(), exception);
        }
    }

    private Language parseLanguage(JSONObject json, String path) throws JSONException, ContentContractException {
        requireOnly(json, path, "language", "locale");
        return new Language(requiredString(json, "language", path), requiredString(json, "locale", path));
    }

    private SpeechDefaults parseSpeechDefaults(JSONObject json, Language support, Language target)
            throws JSONException, ContentContractException {
        String path = "$.course.tts";
        requireOnly(json, path, "primaryRole", "targetLocale", "supportLocale");
        String primary = requiredString(json, "primaryRole", path);
        if (!"support".equals(primary) && !"target".equals(primary)) fail(path + ".primaryRole", "must be support or target");
        String targetLocale = requiredString(json, "targetLocale", path);
        String supportLocale = requiredString(json, "supportLocale", path);
        if (!target.locale.equals(targetLocale)) fail(path + ".targetLocale", "must match target language");
        if (!support.locale.equals(supportLocale)) fail(path + ".supportLocale", "must match support language");
        return new SpeechDefaults(primary, targetLocale, supportLocale);
    }

    private List<Module> parseModules(JSONArray array, Language support, Language target)
            throws JSONException, ContentContractException {
        List<Module> modules = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String path = "$.course.modules[" + i + "]";
            JSONObject json = array.getJSONObject(i);
            requireOnly(json, path, "id", "type", "level", "audience", "title", "tags", "lessons");
            String id = requiredId(json, "id", path);
            String type = requiredString(json, "type", path);
            if (!MODULE_TYPES.contains(type)) fail(path + ".type", "unsupported module type");
            Integer level = json.has("level") && !json.isNull("level") ? requiredPositiveInt(json, "level", path) : null;
            String audience = requiredString(json, "audience", path);
            TextPair title = parsePair(requiredObject(json, "title", path), path + ".title");
            List<String> tags = parseTags(requiredArray(json, "tags", path), path + ".tags");
            List<Lesson> lessons = parseLessons(requiredArray(json, "lessons", path), id, support, target);
            modules.add(new Module(id, type, level, audience, title, tags, lessons));
        }
        return modules;
    }

    private List<Lesson> parseLessons(JSONArray array, String moduleId, Language support, Language target)
            throws JSONException, ContentContractException {
        List<Lesson> lessons = new ArrayList<>();
        Set<Integer> orders = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            String path = "$.course.modules[" + moduleId + "].lessons[" + i + "]";
            JSONObject json = array.getJSONObject(i);
            requireOnly(json, path, "id", "moduleId", "order", "title", "situation", "tags", "items", "quiz");
            String id = requiredId(json, "id", path);
            String reference = requiredString(json, "moduleId", path);
            if (!moduleId.equals(reference)) fail(path + ".moduleId", "must match containing module");
            int order = requiredPositiveInt(json, "order", path);
            if (!orders.add(order)) fail(path + ".order", "duplicate sibling order");
            TextPair title = parsePair(requiredObject(json, "title", path), path + ".title");
            TextPair situation = parsePair(requiredObject(json, "situation", path), path + ".situation");
            List<String> tags = parseTags(requiredArray(json, "tags", path), path + ".tags");
            List<Item> items = parseItems(requiredArray(json, "items", path), path + ".items", support, target);
            if (items.isEmpty()) fail(path + ".items", "must contain at least one item");
            Quiz quiz = json.has("quiz") && !json.isNull("quiz") ? parseQuiz(requiredObject(json, "quiz", path), path + ".quiz") : null;
            lessons.add(new Lesson(id, reference, order, title, situation, tags, items, quiz));
        }
        Collections.sort(lessons, (a, b) -> Integer.compare(a.order, b.order));
        return lessons;
    }

    private List<Item> parseItems(JSONArray array, String path, Language support, Language target)
            throws JSONException, ContentContractException {
        List<Item> items = new ArrayList<>();
        Set<Integer> orders = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            String p = path + "[" + i + "]";
            JSONObject json = array.getJSONObject(i);
            requireOnly(json, p, "id", "order", "type", "speaker", "text", "tts", "notes", "tags");
            String id = requiredId(json, "id", p);
            int order = requiredPositiveInt(json, "order", p);
            if (!orders.add(order)) fail(p + ".order", "duplicate sibling order");
            String type = requiredString(json, "type", p);
            if (!ITEM_TYPES.contains(type)) fail(p + ".type", "unsupported item type");
            String speaker = optionalString(json, "speaker");
            TextPair text = parsePair(requiredObject(json, "text", p), p + ".text");
            SpeechSpec speech = parseSpeech(requiredObject(json, "tts", p), p + ".tts", support, target);
            Map<String, Note> notes = json.has("notes") ? parseNotes(requiredObject(json, "notes", p), p + ".notes") : new HashMap<>();
            List<String> tags = parseTags(requiredArray(json, "tags", p), p + ".tags");
            items.add(new Item(id, order, type, speaker, text, speech, notes, tags));
        }
        Collections.sort(items, (a, b) -> Integer.compare(a.order, b.order));
        return items;
    }

    private SpeechSpec parseSpeech(JSONObject json, String path, Language support, Language target)
            throws JSONException, ContentContractException {
        requireOnly(json, path, "role", "locale", "enabled");
        String role = requiredString(json, "role", path);
        if (!"support".equals(role) && !"target".equals(role)) fail(path + ".role", "must be support or target");
        String locale = requiredString(json, "locale", path);
        String expected = "target".equals(role) ? target.locale : support.locale;
        if (!expected.equals(locale)) fail(path + ".locale", "does not match role language");
        return new SpeechSpec(role, locale, requiredBoolean(json, "enabled", path));
    }

    private Map<String, Note> parseNotes(JSONObject json, String path) throws JSONException, ContentContractException {
        requireOnly(json, path, NOTE_TYPES.toArray(new String[0]));
        Map<String, Note> notes = new HashMap<>();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String type = keys.next();
            JSONObject note = requiredObject(json, type, path);
            String p = path + "." + type;
            if ("grammar".equals(type)) {
                requireOnly(note, p, "support", "targetExample");
                notes.put(type, new Note(requiredString(note, "support", p), optionalString(note, "targetExample")));
            } else if ("pronunciation".equals(type)) {
                requireOnly(note, p, "support", "targetText");
                notes.put(type, new Note(requiredString(note, "support", p), optionalString(note, "targetText")));
            } else {
                requireOnly(note, p, "support");
                notes.put(type, new Note(requiredString(note, "support", p), ""));
            }
        }
        return notes;
    }

    private Quiz parseQuiz(JSONObject json, String path) throws JSONException, ContentContractException {
        requireOnly(json, path, "id", "title", "questions");
        String id = requiredId(json, "id", path);
        TextPair title = parsePair(requiredObject(json, "title", path), path + ".title");
        JSONArray source = requiredArray(json, "questions", path);
        List<Question> questions = new ArrayList<>();
        Set<Integer> orders = new HashSet<>();
        for (int i = 0; i < source.length(); i++) {
            String p = path + ".questions[" + i + "]";
            JSONObject q = source.getJSONObject(i);
            requireOnly(q, p, "id", "order", "type", "answerDisplayRole", "prompt", "answers", "explanation", "tags");
            String qid = requiredId(q, "id", p);
            int order = requiredPositiveInt(q, "order", p);
            if (!orders.add(order)) fail(p + ".order", "duplicate sibling order");
            String type = requiredString(q, "type", p);
            if (!"single-choice".equals(type)) fail(p + ".type", "only single-choice is supported");
            String displayRole = QuizIntegrity.requireAnswerDisplayRole(requiredString(q, "answerDisplayRole", p), p);
            TextPair prompt = parsePair(requiredObject(q, "prompt", p), p + ".prompt");
            List<Answer> answers = parseAnswers(requiredArray(q, "answers", p), p + ".answers");
            TextPair explanation = parsePair(requiredObject(q, "explanation", p), p + ".explanation");
            List<String> tags = parseTags(requiredArray(q, "tags", p), p + ".tags");
            questions.add(new Question(qid, order, type, displayRole, prompt, answers, explanation, tags));
        }
        Collections.sort(questions, (a, b) -> Integer.compare(a.order, b.order));
        return new Quiz(id, title, questions);
    }

    private List<Answer> parseAnswers(JSONArray array, String path) throws JSONException, ContentContractException {
        if (array.length() < 2) fail(path, "single-choice requires at least two answers");
        List<Answer> answers = new ArrayList<>();
        int correct = 0;
        for (int i = 0; i < array.length(); i++) {
            String p = path + "[" + i + "]";
            JSONObject json = array.getJSONObject(i);
            requireOnly(json, p, "id", "text", "correct");
            Answer answer = new Answer(requiredId(json, "id", p), parsePair(requiredObject(json, "text", p), p + ".text"), requiredBoolean(json, "correct", p));
            if (answer.correct) correct++;
            answers.add(answer);
        }
        if (correct != 1) fail(path, "exactly one correct answer required");
        return answers;
    }

    private TextPair parsePair(JSONObject json, String path) throws JSONException, ContentContractException {
        requireOnly(json, path, "support", "target");
        return new TextPair(requiredString(json, "support", path), requiredString(json, "target", path));
    }

    private List<String> parseTags(JSONArray array, String path) throws JSONException, ContentContractException {
        List<String> tags = new ArrayList<>();
        Set<String> unique = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            Object raw = array.get(i);
            if (!(raw instanceof String) || !TAG_PATTERN.matcher((String) raw).matches()) fail(path + "[" + i + "]", "invalid tag");
            if (!unique.add((String) raw)) fail(path + "[" + i + "]", "duplicate tag");
            tags.add((String) raw);
        }
        return tags;
    }

    private String requiredId(JSONObject json, String key, String path) throws JSONException, ContentContractException {
        String value = requiredString(json, key, path);
        if (!ID_PATTERN.matcher(value).matches()) fail(path + "." + key, "invalid stable ID");
        if (!ids.add(value)) fail(path + "." + key, "duplicate stable ID");
        return value;
    }

    private String requiredString(JSONObject json, String key, String path) throws JSONException, ContentContractException {
        if (!json.has(key) || json.isNull(key) || !(json.get(key) instanceof String)) fail(path + "." + key, "required string");
        String value = json.getString(key);
        if (value.trim().isEmpty()) fail(path + "." + key, "must not be blank");
        return value;
    }

    private String optionalString(JSONObject json, String key) throws JSONException {
        if (!json.has(key) || json.isNull(key)) return "";
        return json.getString(key);
    }

    private int requiredPositiveInt(JSONObject json, String key, String path) throws JSONException, ContentContractException {
        if (!json.has(key) || json.isNull(key) || !(json.get(key) instanceof Number)) fail(path + "." + key, "required integer");
        int value = json.getInt(key);
        if (value <= 0) fail(path + "." + key, "must be positive");
        return value;
    }

    private boolean requiredBoolean(JSONObject json, String key, String path) throws JSONException, ContentContractException {
        if (!json.has(key) || json.isNull(key) || !(json.get(key) instanceof Boolean)) fail(path + "." + key, "required boolean");
        return json.getBoolean(key);
    }

    private JSONObject requiredObject(JSONObject json, String key, String path) throws JSONException, ContentContractException {
        if (!json.has(key) || json.isNull(key) || !(json.get(key) instanceof JSONObject)) fail(path + "." + key, "required object");
        return json.getJSONObject(key);
    }

    private JSONArray requiredArray(JSONObject json, String key, String path) throws JSONException, ContentContractException {
        if (!json.has(key) || json.isNull(key) || !(json.get(key) instanceof JSONArray)) fail(path + "." + key, "required array");
        return json.getJSONArray(key);
    }

    private void requireOnly(JSONObject json, String path, String... allowed) throws ContentContractException {
        Set<String> allowedKeys = new HashSet<>(Arrays.asList(allowed));
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!allowedKeys.contains(key)) fail(path + "." + key, "unknown field");
        }
    }

    private String readAsset(String name) throws IOException {
        try (InputStream input = context.getAssets().open(name);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) text.append(line).append('\n');
            return text.toString();
        }
    }

    private static Set<String> setOf(String... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    private void fail(String path, String reason) throws ContentContractException {
        throw new ContentContractException(assetName + " " + path + ": " + reason);
    }
}
