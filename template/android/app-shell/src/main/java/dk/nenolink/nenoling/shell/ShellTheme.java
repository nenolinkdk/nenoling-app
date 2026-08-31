package dk.nenolink.nenoling.shell;

/** Product theme values as Android ARGB ints; reusable shell owns no brand palette. */
public final class ShellTheme {
    public final int background;
    public final int panel;
    public final int primary;
    public final int primaryDark;
    public final int accent;
    public final int text;
    public final int muted;
    public final int onAccent;

    public ShellTheme(int background, int panel, int primary, int primaryDark,
                      int accent, int text, int muted, int onAccent) {
        this.background = background;
        this.panel = panel;
        this.primary = primary;
        this.primaryDark = primaryDark;
        this.accent = accent;
        this.text = text;
        this.muted = muted;
        this.onAccent = onAccent;
    }

    public static ShellTheme neutral() {
        return new ShellTheme(
                0xFFF7F7F7,
                0xFFEAEAEA,
                0xFFDDE7DD,
                0xFF263A2D,
                0xFF71524A,
                0xFF202020,
                0xFF666666,
                0xFFFFFFFF);
    }
}
