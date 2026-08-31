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

import java.util.List;

import dk.nenolink.nenoling.content.ContentModels.Module;

/** Reusable home/module renderer with no language-pair-specific strings. */
public final class NenolingModuleView {
    public interface Actions {
        void open(Module module);
        void openResources();
    }

    private final Context context;
    private final ShellConfig config;
    private final ShellTheme theme;

    public NenolingModuleView(Context context, ShellConfig config, ShellTheme theme) {
        this.context = context;
        this.config = config;
        this.theme = theme;
    }

    public View build(List<Module> modules, ModuleProgressProvider progress,
                      boolean showResources, Actions actions) {
        LinearLayout root = column();
        root.addView(centered(config.ui.modulesTitle, 20, theme.primaryDark, true));
        root.addView(centered(config.ui.modulesIntro, 14, theme.muted, false), matchWrap(4));

        for (Module module : ModuleOrder.ordered(modules)) {
            String suffix = progress == null ? "" : progress.progress(module);
            Button button = button(module.title.support + "\n" + module.title.target + suffix,
                    theme.primary, theme.primaryDark);
            button.setOnClickListener(v -> actions.open(module));
            root.addView(button, matchWrap(8));
        }

        if (showResources) {
            Button resources = button(config.ui.practicalLinks, theme.panel, theme.primaryDark);
            resources.setOnClickListener(v -> actions.openResources());
            root.addView(resources, matchWrap(8));
        }
        return root;
    }

    public interface ModuleProgressProvider {
        String progress(Module module);
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

    private int dp(int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
