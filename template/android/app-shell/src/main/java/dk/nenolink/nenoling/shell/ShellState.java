package dk.nenolink.nenoling.shell;

/** Navigation state shared by product hosts and reusable shell views. */
public final class ShellState {
    public enum Screen { MODULES, LESSONS, LESSON, ITEM, QUIZ, QUIZ_RESULT, RESOURCES }

    public Screen screen = Screen.MODULES;
    public String moduleId = "";
    public String lessonId = "";
    public int itemIndex = 0;
    public int questionIndex = 0;

    public void modules() {
        screen = Screen.MODULES;
        moduleId = "";
        lessonId = "";
        itemIndex = 0;
        questionIndex = 0;
    }

    public void lessons(String moduleId) {
        screen = Screen.LESSONS;
        this.moduleId = value(moduleId);
        lessonId = "";
        itemIndex = 0;
        questionIndex = 0;
    }

    public void lesson(String moduleId, String lessonId) {
        screen = Screen.LESSON;
        this.moduleId = value(moduleId);
        this.lessonId = value(lessonId);
        itemIndex = 0;
        questionIndex = 0;
    }

    public void item(int index) {
        screen = Screen.ITEM;
        itemIndex = Math.max(0, index);
        questionIndex = 0;
    }

    public void quiz() {
        screen = Screen.QUIZ;
        questionIndex = 0;
    }

    public void quizQuestion(int index) {
        screen = Screen.QUIZ;
        questionIndex = Math.max(0, index);
    }

    public void quizResult() {
        screen = Screen.QUIZ_RESULT;
    }

    public void resources() {
        screen = Screen.RESOURCES;
    }

    private static String value(String value) {
        return value == null ? "" : value;
    }
}
