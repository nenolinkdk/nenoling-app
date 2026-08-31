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
import dk.nenolink.nenoling.content.ContentModels.Module;

/** Reusable lesson-list renderer. */
public final class NenolingLessonListView {
    public interface Actions {
        void back();
        void open(Lesson lesson);
    }

    public interface LessonProgressProvider {
        String progress(Lesson lesson);
    }

    private final Context context;
    private final ShellConfig config;
    private final ShellTheme theme;

    public NenolingLessonListView(Context context, ShellConfig config, ShellTheme theme) {
        this.context = context;
        this.config = config;
        this.theme = theme;
    }

    public View build(Module module, LessonProgressProvider progress, Actions actions) {
        LinearLayout root = column();
        Button back = button(config.ui.back, theme.panel, theme.primaryDark);
        back.setOnClickListener(v -> actions.back());
        root.addView(back, wrap(0));
        root.addView(centered(module.title.support, 20, theme.primaryDark, true), matchWrap(6));
        root.addView(centered(module.title.target, 16, theme.text, true), matchWrap(2));

        for (Lesson lesson : module.lessons) {
            String suffix = progress == null ? "" : progress.progress(lesson);
            Button entry = button(lesson.title.support + "\n" + lesson.title.target + suffix,
                    theme.primary, theme.primaryDark);
            entry.setOnClickListener(v -> actions.open(lesson));
            root.addView(entry, matchWrap(8));
        }
        return root;
    }

    private LinearLayout column() {
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        return view;
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

    private LinearLayout.LayoutParams wrap(int top) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(top);
        return params;
    }

    private int dp(int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
