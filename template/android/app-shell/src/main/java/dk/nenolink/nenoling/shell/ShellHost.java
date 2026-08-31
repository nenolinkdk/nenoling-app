package dk.nenolink.nenoling.shell;

import android.view.View;

/** Thin product host boundary. The reusable shell decides what to render. */
public interface ShellHost {
    void show(View view);
}
