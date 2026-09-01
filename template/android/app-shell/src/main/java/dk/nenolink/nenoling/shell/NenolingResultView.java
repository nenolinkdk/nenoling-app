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

import java.util.Locale;

/** Reusable quiz-result screen aligned with the approved FR-DA 0.5.2 flow. */
public final class NenolingResultView {
    public interface Actions {
        void repeat();
        void back();
    }

    private final Context context;
    private final ShellConfig config;
    private final ShellTheme theme;

    public NenolingResultView(Context context, ShellConfig config, ShellTheme theme) {
        this.context = context;
        this.config = config;
        this.theme = theme;
    }

    public View build(int score, int total, Actions actions) {
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView heading = centered(formatResult(score, total), 20, theme.primaryDark, true);
        root.addView(heading, matchWrap(6));

        TextView saved = centered(config.ui.resultSaved, 12, theme.muted, false);
        root.addView(saved, matchWrap(2));

        Button repeat = button(config.ui.repeatQuiz, theme.primary, theme.primaryDark);
        repeat.setOnClickListener(v -> actions.repeat());
        root.addView(repeat, matchWrap(8));

        Button back = button(config.ui.back, theme.panel, theme.primaryDark);
        back.setOnClickListener(v -> actions.back());
        root.addView(back, matchWrap(8));
        return root;
    }

    /** Compatibility for older hosts that only supplied one completion action. */
    public View build(int score, int total, Runnable done) {
        return build(score, total, new Actions() {
            @Override public void repeat() { done.run(); }
            @Override public void back() { done.run(); }
        });
    }

    private String formatResult(int score, int total) {
        try {
            return String.format(Locale.getDefault(), config.ui.quizResultPattern, score, total);
        } catch (RuntimeException exception) {
            return score + "/" + total;
        }
    }

    private TextView centered(String value, int sp, int color, boolean bold) {
        TextView view = new TextView(context);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return view;
    }

    private Button button(String label, int background, int textColor) {
        Button button = new Button(context);
        button.setText(label);
        button.setTextSize(14);
        button.setTextColor(textColor);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);
        button.setMinHeight(dp(44));
        button.setMinimumHeight(dp(44));
        button.setPadding(dp(10), dp(6), dp(10), dp(6));
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
