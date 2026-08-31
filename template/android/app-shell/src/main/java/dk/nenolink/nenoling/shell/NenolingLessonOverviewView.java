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

import dk.nenolink.nenoling.content.ContentModels.Lesson;

/** Reusable lesson landing screen between lesson list and lesson items. */
public final class NenolingLessonOverviewView {
    public interface Actions {
        void back();
        void startLesson();
        void openQuiz();
        void openResources();
    }

    private final Context context;
    private final ShellConfig config;
    private final ShellTheme theme;

    public NenolingLessonOverviewView(Context context, ShellConfig config, ShellTheme theme) {
        this.context = context;
        this.config = config;
        this.theme = theme;
    }

    public View build(Lesson lesson, String progress, String savedQuizResult,
                      boolean showResources, Actions actions) {
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);

        Button back = button(config.ui.back, theme.panel, theme.primaryDark);
        back.setOnClickListener(v -> actions.back());
        root.addView(back, params(0));

        root.addView(text(lesson.title.support, 20, theme.primaryDark, true, Gravity.CENTER), params(10));
        root.addView(text(lesson.title.target, 15, theme.muted, false, Gravity.CENTER), params(2));

        TextView situation = text(lesson.situation.support + "\n" + lesson.situation.target,
                15, theme.text, false, Gravity.START);
        situation.setPadding(dp(12), dp(10), dp(12), dp(10));
        situation.setBackground(panel(theme.panel));
        root.addView(situation, params(10));

        if (progress != null && !progress.isEmpty()) {
            root.addView(text(progress.trim(), 13, theme.muted, false, Gravity.CENTER), params(8));
        }

        Button start = button(config.ui.startLesson, theme.primary, theme.primaryDark);
        start.setOnClickListener(v -> actions.startLesson());
        root.addView(start, params(10));

        if (lesson.quiz != null && !lesson.quiz.questions.isEmpty()) {
            String label = config.ui.openQuiz;
            if (savedQuizResult != null && !savedQuizResult.isEmpty()) {
                label += "\n" + config.ui.resultSaved + ": " + savedQuizResult;
            }
            Button quiz = button(label, theme.accent, theme.onAccent);
            quiz.setOnClickListener(v -> actions.openQuiz());
            root.addView(quiz, params(8));
        }

        if (showResources) {
            Button resources = button(config.ui.practicalLinks, theme.panel, theme.primaryDark);
            resources.setOnClickListener(v -> actions.openResources());
            root.addView(resources, params(8));
        }
        return root;
    }

    private TextView text(String value, int sp, int color, boolean bold, int gravity) {
        TextView view = new TextView(context);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setGravity(gravity);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return view;
    }

    private Button button(String label, int background, int textColor) {
        Button button = new Button(context);
        button.setText(label);
        button.setTextSize(13);
        button.setTextColor(textColor);
        button.setAllCaps(false);
        button.setBackground(panel(background));
        return button;
    }

    private GradientDrawable panel(int color) {
        GradientDrawable background = new GradientDrawable();
        background.setColor(color);
        background.setCornerRadius(dp(12));
        return background;
    }

    private LinearLayout.LayoutParams params(int top) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(top);
        return params;
    }

    private int dp(int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
