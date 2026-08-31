package dk.nenolink.nenoling.shell;

/** Generic formatting helpers; product supplies words through ShellConfig.UiText. */
public final class ShellText {
    private ShellText() {}

    public static String progress(int completed, int total) {
        return completed + "/" + total;
    }

    public static String itemPosition(ShellConfig.UiText ui, int current, int total) {
        return ui.item + " " + current + " " + ui.of + " " + total;
    }

    public static String questionPosition(ShellConfig.UiText ui, int current, int total) {
        return ui.question + " " + current + " " + ui.of + " " + total;
    }

    public static String savedResult(ShellConfig.UiText ui, String result) {
        return result == null || result.isEmpty() ? "" : ui.resultSaved + ": " + result;
    }
}
