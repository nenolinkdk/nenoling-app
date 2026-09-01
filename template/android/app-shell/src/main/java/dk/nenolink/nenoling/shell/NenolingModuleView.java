package dk.nenolink.nenoling.shell;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dk.nenolink.nenoling.content.ContentModels.Module;

/** Reusable front-page/module renderer aligned with the approved FR-DA 0.5.2 product shell. */
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

        root.addView(centered(config.appName, 22, theme.primaryDark, true));
        if (!config.appIntro.isEmpty()) {
            TextView intro = centered(config.appIntro, 13, theme.muted, false);
            intro.setPadding(dp(8), dp(4), dp(8), dp(6));
            root.addView(intro);
        }

        root.addView(centered(config.ui.modulesTitle, 20, theme.primaryDark, true), matchWrap(10));
        if (config.ui.modulesIntro != null && !config.ui.modulesIntro.trim().isEmpty()) {
            root.addView(centered(config.ui.modulesIntro, 14, theme.muted, false), matchWrap(4));
        }

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

        addFooter(root);
        return root;
    }

    private void addFooter(LinearLayout root) {
        if (has(config.footerCredit)) {
            root.addView(centered(config.footerCredit, 11, theme.muted, false), matchWrap(18));
        }
        if (has(config.footerLinkLabel)) {
            TextView link = centered(config.footerLinkLabel, 12, theme.primaryDark, false);
            link.setPadding(0, dp(2), 0, 0);
            link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            if (has(config.footerLinkUrl)) {
                link.setClickable(true);
                link.setFocusable(true);
                link.setOnClickListener(v -> openExternalUrl(config.footerLinkUrl));
            }
            root.addView(link, matchWrap(2));
        }
        // Product host supplies the already formatted version/date line, e.g. "Version 0.5.2 · 31.08.2026".
        if (has(config.versionLinePattern)) {
            TextView version = centered(config.versionLinePattern, 10, theme.muted, false);
            version.setPadding(0, dp(3), 0, 0);
            root.addView(version, matchWrap(2));
        }
    }

    private void openExternalUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(context, "Link unavailable", Toast.LENGTH_SHORT).show();
        }
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

    private boolean has(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private int dp(int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
