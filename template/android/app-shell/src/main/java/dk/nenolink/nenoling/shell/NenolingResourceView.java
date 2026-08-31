package dk.nenolink.nenoling.shell;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dk.nenolink.nenoling.content.ResourceModels.ExternalResource;
import dk.nenolink.nenoling.content.ResourceModels.ResourceCollection;

/** Generic practical-resource renderer; destinations remain language-pack data. */
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
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        Button back = new Button(context);
        back.setText(config.ui.back);
        back.setAllCaps(false);
        back.setOnClickListener(v -> backAction.back());
        root.addView(back);
        for (ResourceCollection collection : collections) {
            TextView title = new TextView(context);
            title.setText(collection.title.support + "\n" + collection.title.target);
            title.setTextSize(18);
            title.setTextColor(theme.primaryDark);
            root.addView(title);
            if (collection.intro != null) {
                TextView intro = new TextView(context);
                intro.setText(collection.intro.support + "\n" + collection.intro.target);
                intro.setTextColor(theme.muted);
                root.addView(intro);
            }
            for (ExternalResource resource : collection.items) {
                Button entry = new Button(context);
                entry.setAllCaps(false);
                entry.setText(resource.title.support + "\n" + resource.title.target);
                entry.setOnClickListener(v -> open.open(resource));
                root.addView(entry);
            }
        }
        return root;
    }
}
