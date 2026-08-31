package dk.nenolink.nenoling.shell;

/** Navigation state shared by product hosts and future Android views. */
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
        this.moduleId = moduleId;
        lessonId = "";
        itemIndex = 0;
        questionIndex = 0;
    }

    public void lesson(String moduleId, String lessonId) {
        screen = Screen.LESSON;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
        itemIndex = 0;
        questionIndex = 0;
    }

    public void item(int index) {
        screen = Screen.ITEM;
        itemIndex = Math.max(0, index);
    }

    public void quiz() {
        screen = Screen.QUIZ;
        questionIndex = 0;
    }
}
