package dk.nenolink.nenoling.shell;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import dk.nenolink.nenoling.content.ContentModels.Item;
import dk.nenolink.nenoling.content.ContentModels.Note;
import dk.nenolink.nenoling.speech.SpeechController;
import dk.nenolink.nenoling.ui.RoundNavBar;

/** Compact reusable item view. Product supplies strings, colours and language data. */
public final class NenolingLessonView {
    public interface Actions {
        void previous();
        void next();
        void markComplete();
    }

    private final Context context;
    private final ShellConfig config;
    private final ShellTheme theme;
    private final LessonNoteLabels noteLabels;
    private final SpeechController speech;
    private final SpeechController.Listener speechListener;

    public NenolingLessonView(Context context, ShellConfig config, ShellTheme theme,
                              LessonNoteLabels noteLabels, SpeechController speech,
                              SpeechController.Listener speechListener) {
        this.context = context;
        this.config = config;
        this.theme = theme;
        this.noteLabels = noteLabels;
        this.speech = speech;
        this.speechListener = speechListener;
    }

    public View build(Item item, int index, int total, boolean complete,
                      boolean previousEnabled, String supportLocale, String targetLocale,
                      Actions actions) {
        return build(item, index, total, complete, previousEnabled, false,
                supportLocale, targetLocale, actions);
    }

    /**
     * Builds one lesson item. On the final item, hasQuiz makes the transition to the
     * lesson quiz explicit instead of relying only on the arrow's content description.
     */
    public View build(Item item, int index, int total, boolean complete,
                      boolean previousEnabled, boolean hasQuiz,
                      String supportLocale, String targetLocale, Actions actions) {
        LinearLayout root = column();
        root.addView(centered(ShellText.itemPosition(config.ui, index + 1, total), 12, theme.muted, false));
        if (!item.speaker.isEmpty()) root.addView(centered(item.speaker, 12, theme.muted, false));

        TextView targetText = centered(item.text.target, 22, theme.text, true);
        targetText.setPadding(dp(12), dp(10), dp(12), dp(10));
        targetText.setBackground(panel(theme.panel));
        root.addView(targetText, matchWrap(8));

        TextView supportText = centered(item.text.support, 15, theme.muted, false);
        supportText.setPadding(dp(8), dp(4), dp(8), dp(2));
        root.addView(supportText, matchWrap(4));

        boolean finalItem = index + 1 >= total;
        RoundNavBar nav = new RoundNavBar(context, theme.primary, theme.panel, theme.primaryDark, theme.muted);
        nav.bind(previousEnabled, true, config.ui.previous,
                finalItem && hasQuiz ? config.ui.openQuiz : config.ui.next,
                new RoundNavBar.Actions() {
                    @Override public void onPrevious() { actions.previous(); }
                    @Override public void onNext() { actions.next(); }
                });
        root.addView(nav, matchWrap(8));

        if (finalItem && hasQuiz) {
            Button quiz = button(config.ui.openQuiz, theme.accent, theme.onAccent);
            quiz.setOnClickListener(v -> actions.next());
            root.addView(quiz, matchWrap(8));
        }

        LinearLayout tts = new LinearLayout(context);
        tts.setGravity(Gravity.CENTER);
        Button supportButton = button(config.ui.listenSupport, theme.primary, theme.primaryDark);
        supportButton.setOnClickListener(v -> speech.speak(item.text.support, supportLocale, speechListener));
        Button targetButton = button(config.ui.listenTarget, theme.primary, theme.primaryDark);
        targetButton.setOnClickListener(v -> speech.speak(item.text.target, targetLocale, speechListener));
        tts.addView(supportButton);
        LinearLayout.LayoutParams targetParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        targetParams.leftMargin = dp(8);
        tts.addView(targetButton, targetParams);
        root.addView(tts, matchWrap(8));

        Button completion = button(complete ? config.ui.completed : config.ui.markComplete,
                theme.accent, theme.onAccent);
        completion.setEnabled(!complete);
        completion.setOnClickListener(v -> actions.markComplete());
        root.addView(completion, matchWrap(8));

        for (Map.Entry<String, String> entry : noteLabels.all().entrySet()) {
            Note note = item.notes.get(entry.getKey());
            if (note == null) continue;
            String value = entry.getValue() + "\n" + note.support;
            if (!note.targetDetail.isEmpty()) value += "\n" + note.targetDetail;
            TextView noteView = text(value, 14, theme.text, false);
            noteView.setPadding(dp(12), dp(8), dp(12), dp(8));
            noteView.setBackground(panel(theme.panel));
            root.addView(noteView, matchWrap(8));
        }
        return root;
    }

    private LinearLayout column() {
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        return view;
    }

    private TextView centered(String value, int sp, int color, boolean bold) {
        TextView view = text(value, sp, color, bold);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    private TextView text(String value, int sp, int color, boolean bold) {
        TextView view = new TextView(context);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setLineSpacing(0, 1.06f);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return view;
    }

    private Button button(String label, int background, int textColor) {
        Button button = new Button(context);
        button.setText(label);
        button.setTextSize(13);
        button.setTextColor(textColor);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);
        button.setMinHeight(dp(40));
        button.setMinimumHeight(dp(40));
        button.setPadding(dp(12), dp(6), dp(12), dp(6));
        button.setStateListAnimator(null);
        button.setBackground(panel(background));
        return button;
    }

    private GradientDrawable panel(int color) {
        GradientDrawable background = new GradientDrawable();
        background.setColor(color);
        background.setCornerRadius(dp(12));
        return background;
    }

    private LinearLayout.LayoutParams matchWrap(int top) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(top);
        return params;
    }

    private int dp(int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
