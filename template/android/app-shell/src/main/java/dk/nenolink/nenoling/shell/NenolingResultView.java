package dk.nenolink.nenoling.shell;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Minimal reusable quiz-result screen. */
public final class NenolingResultView {
    public interface Actions { void done(); }

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
        TextView title = new TextView(context);
        title.setText(config.ui.resultSaved);
        title.setTextSize(18);
        title.setTextColor(theme.primaryDark);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(title);
        TextView result = new TextView(context);
        result.setText(score + "/" + total);
        result.setTextSize(28);
        result.setTextColor(theme.text);
        result.setGravity(Gravity.CENTER);
        root.addView(result);
        Button done = new Button(context);
        done.setText(config.ui.back);
        done.setAllCaps(false);
        done.setOnClickListener(v -> actions.done());
        root.addView(done);
        return root;
    }
}
