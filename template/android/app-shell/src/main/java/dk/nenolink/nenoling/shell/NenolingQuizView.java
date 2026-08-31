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

import dk.nenolink.nenoling.content.ContentModels.Answer;
import dk.nenolink.nenoling.content.ContentModels.Question;

/** Generic quiz renderer. QuizSession owns shuffle/score lifecycle. */
public final class NenolingQuizView {
    public interface Actions {
        void back();
        void answer(Answer answer);
        void next();
    }

    private final Context context;
    private final ShellConfig config;
    private final ShellTheme theme;

    public NenolingQuizView(Context context, ShellConfig config, ShellTheme theme) {
        this.context = context;
        this.config = config;
        this.theme = theme;
    }

    public View buildQuestion(QuizSession session, Actions actions) {
        if (session == null) throw new IllegalArgumentException("quiz session required");
        LinearLayout root = column();
        Button back = button(config.ui.back, theme.panel, theme.primaryDark);
        back.setOnClickListener(v -> actions.back());
        root.addView(back, wrap(0));

        Question question = session.question();
        root.addView(centered(ShellText.questionPosition(config.ui,
                session.questionNumber(), session.totalQuestions()), 12, theme.muted, false), matchWrap(6));
        root.addView(panelText(pair(question.prompt), theme.panel), matchWrap(6));

        for (Answer answer : session.displayedAnswers()) {
            if (answer == null) continue;
            Button option = button(answerText(question, answer), theme.primary, theme.primaryDark);
            option.setOnClickListener(v -> actions.answer(answer));
            root.addView(option, matchWrap(8));
        }
        return root;
    }

    public View buildFeedback(QuizSession session, boolean correct, Actions actions) {
        if (session == null) throw new IllegalArgumentException("quiz session required");
        LinearLayout root = column();
        Button back = button(config.ui.back, theme.panel, theme.primaryDark);
        back.setOnClickListener(v -> actions.back());
        root.addView(back, wrap(0));
        root.addView(centered(correct ? "✓" : "×", 24,
                correct ? theme.primaryDark : theme.accent, true), matchWrap(8));
        root.addView(panelText(pair(session.question().explanation),
                correct ? theme.primary : theme.panel), matchWrap(6));
        Button next = button(config.ui.next, theme.primary, theme.primaryDark);
        next.setOnClickListener(v -> actions.next());
        root.addView(next, matchWrap(8));
        return root;
    }

    private String answerText(Question question, Answer answer) {
        if (question == null || answer.text == null) return "";
        if ("support".equals(question.answerDisplayRole)) return safe(answer.text.support);
        return safe(answer.text.target);
    }

    private String pair(dk.nenolink.nenoling.content.ContentModels.TextPair pair) {
        if (pair == null) return "";
        String support = safe(pair.support);
        String target = safe(pair.target);
        if (support.isEmpty()) return target;
        if (target.isEmpty()) return support;
        return support + "\n" + target;
    }

    private String safe(String value) { return value == null ? "" : value; }

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

    private TextView panelText(String value, int backgroundColor) {
        TextView view = new TextView(context);
        view.setText(value);
        view.setTextSize(14);
        view.setTextColor(theme.text);
        view.setPadding(dp(12), dp(8), dp(12), dp(8));
        view.setBackground(panel(backgroundColor));
        return view;
    }

    private Button button(String label, int background, int textColor) {
        Button button = new Button(context);
        button.setText(label == null ? "" : label);
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
