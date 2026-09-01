package dk.nenolink.nenoling.shell;

/** Product-supplied presentation strings and brand metadata. */
public final class ShellConfig {
    public final String appName;
    /** Short purpose/audience copy shown on the modules/front page only. */
    public final String appIntro;
    /** Already formatted product version/date line. */
    public final String versionLinePattern;
    public final String footerCredit;
    public final String footerLinkLabel;
    public final String footerLinkUrl;
    public final UiText ui;

    public ShellConfig(String appName, String appIntro, String versionLinePattern, String footerCredit,
                       String footerLinkLabel, String footerLinkUrl, UiText ui) {
        this.appName = required(appName, "appName");
        this.appIntro = appIntro == null ? "" : appIntro.trim();
        this.versionLinePattern = required(versionLinePattern, "versionLinePattern");
        this.footerCredit = required(footerCredit, "footerCredit");
        this.footerLinkLabel = required(footerLinkLabel, "footerLinkLabel");
        this.footerLinkUrl = footerLinkUrl == null ? "" : footerLinkUrl.trim();
        this.ui = ui == null ? UiText.defaults() : ui;
    }

    /** Backwards-compatible constructor for existing product hosts. */
    public ShellConfig(String appName, String versionLinePattern, String footerCredit,
                       String footerLinkLabel, String footerLinkUrl, UiText ui) {
        this(appName, "", versionLinePattern, footerCredit, footerLinkLabel, footerLinkUrl, ui);
    }

    private static String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(field + " required");
        return value;
    }

    public static final class UiText {
        public final String modulesTitle;
        public final String modulesIntro;
        public final String startLesson;
        public final String openQuiz;
        public final String completed;
        public final String markComplete;
        public final String previous;
        public final String next;
        public final String back;
        public final String item;
        public final String of;
        public final String question;
        public final String resultSaved;
        public final String listenSupport;
        public final String listenTarget;
        public final String practicalLinks;
        public final String openOfficialSite;
        public final String linkUnavailable;
        /** Product-localized result pattern using %1$d for score and %2$d for total. */
        public final String quizResultPattern;
        public final String repeatQuiz;

        public UiText(String modulesTitle, String modulesIntro, String startLesson, String openQuiz,
                      String completed, String markComplete, String previous, String next, String back,
                      String item, String of, String question, String resultSaved,
                      String listenSupport, String listenTarget, String practicalLinks,
                      String openOfficialSite, String linkUnavailable,
                      String quizResultPattern, String repeatQuiz) {
            this.modulesTitle = modulesTitle;
            this.modulesIntro = modulesIntro;
            this.startLesson = startLesson;
            this.openQuiz = openQuiz;
            this.completed = completed;
            this.markComplete = markComplete;
            this.previous = previous;
            this.next = next;
            this.back = back;
            this.item = item;
            this.of = of;
            this.question = question;
            this.resultSaved = resultSaved;
            this.listenSupport = listenSupport;
            this.listenTarget = listenTarget;
            this.practicalLinks = practicalLinks;
            this.openOfficialSite = openOfficialSite;
            this.linkUnavailable = linkUnavailable;
            this.quizResultPattern = quizResultPattern;
            this.repeatQuiz = repeatQuiz;
        }

        /** Backwards-compatible constructor for hosts created before result-screen parity. */
        public UiText(String modulesTitle, String modulesIntro, String startLesson, String openQuiz,
                      String completed, String markComplete, String previous, String next, String back,
                      String item, String of, String question, String resultSaved,
                      String listenSupport, String listenTarget, String practicalLinks,
                      String openOfficialSite, String linkUnavailable) {
            this(modulesTitle, modulesIntro, startLesson, openQuiz, completed, markComplete,
                    previous, next, back, item, of, question, resultSaved, listenSupport, listenTarget,
                    practicalLinks, openOfficialSite, linkUnavailable, "Result: %1$d/%2$d", "Repeat quiz");
        }

        public static UiText defaults() {
            return new UiText(
                    "Modules", "Choose a module", "Start lesson", "Open quiz",
                    "Completed", "Mark complete", "Previous", "Next", "Back",
                    "Item", "of", "Question", "Saved result",
                    "Listen support", "Listen target", "Practical links",
                    "Open official site", "Link unavailable",
                    "Result: %1$d/%2$d", "Repeat quiz");
        }
    }
}
