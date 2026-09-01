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

import dk.nenolink.nenoling.content.ResourceModels.ExternalResource;
import dk.nenolink.nenoling.content.ResourceModels.ResourceCollection;

/** Generic practical-resource renderer aligned with the approved FR-DA 0.5.2 flow. */
public final class NenolingResourceView {
    public interface OpenAction { void open(ExternalResource resource); }
    public interface BackAction { void back(); }

    private final Context context;
    private final ShellConfig config;
    private final ShellTheme theme;

    public NenolingResourceView(Context context, ShellConfig config, ShellTheme theme) {
        this.context = context;
        this.config = config;
        this.theme = theme;
    }

    public View build(List<ResourceCollection> collections, OpenAction open, BackAction backAction) {
        LinearLayout root = column();

        Button back = button(config.ui.back, theme.panel, theme.primaryDark);
        back.setOnClickListener(v -> backAction.back());
        LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backParams.bottomMargin = dp(8);
        root.addView(back, backParams);

        for (ResourceCollection collection : collections) {
            root.addView(centered(collection.title.support, 20, theme.primaryDark, true));
            root.addView(centered(collection.title.target, 16, theme.text, true));

            if (collection.intro != null) {
                String introText = collection.intro.support + "\n" + collection.intro.target;
                TextView intro = text(introText, 14, theme.text, false);
                intro.setPadding(dp(12), dp(8), dp(12), dp(8));
                intro.setBackground(panel(theme.panel));
                root.addView(intro, matchWrap(8));
            }

            if (has(config.ui.openOfficialSite)) {
                root.addView(centered(config.ui.openOfficialSite, 12, theme.muted, false), matchWrap(2));
            }

            for (ExternalResource resource : collection.items) {
                String label = resource.name;
                if (has(resource.title.support) || has(resource.title.target)) {
                    String titles = resource.title.support + " / " + resource.title.target;
                    label = has(label) ? label + "\n" + titles : titles;
                }
                Button entry = button(label, theme.primary, theme.primaryDark);
                entry.setOnClickListener(v -> open.open(resource));
                root.addView(entry, matchWrap(8));
            }
        }
        return root;
    }

    private LinearLayout column() {
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        return root;
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

    private boolean has(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private int dp(int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
