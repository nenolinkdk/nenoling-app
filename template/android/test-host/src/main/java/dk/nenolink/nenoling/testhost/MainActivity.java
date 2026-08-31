package dk.nenolink.nenoling.testhost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.Collections;

import dk.nenolink.nenoling.config.EngineConfig;
import dk.nenolink.nenoling.content.ContentContractException;
import dk.nenolink.nenoling.content.ContentModels.Course;
import dk.nenolink.nenoling.content.ContentRepository;
import dk.nenolink.nenoling.progress.ProgressStore;
import dk.nenolink.nenoling.shell.LessonNoteLabels;
import dk.nenolink.nenoling.shell.ResourcePlacement;
import dk.nenolink.nenoling.shell.ShellConfig;
import dk.nenolink.nenoling.shell.ShellCoordinator;
import dk.nenolink.nenoling.shell.ShellHost;
import dk.nenolink.nenoling.shell.ShellTheme;
import dk.nenolink.nenoling.speech.SpeechController;

/** Minimal proof that a product can be only host + config + language-pack asset. */
public final class MainActivity extends Activity implements ShellHost, SpeechController.Listener {
    private SpeechController speech;
    private ShellCoordinator coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speech = new SpeechController(this);
        try {
            EngineConfig engine = new EngineConfig("synthetic/course.json", "", 3);
            Course course = new ContentRepository(this, engine).loadProductionCourse();
            ShellConfig shell = new ShellConfig(
                    "Nenoling Synthetic Host",
                    "%s · %s",
                    "Nenoling template",
                    "Nenoling",
                    "",
                    ShellConfig.UiText.defaults());
            coordinator = new ShellCoordinator(
                    this,
                    this,
                    shell,
                    ShellTheme.neutral(),
                    LessonNoteLabels.defaults(),
                    ResourcePlacement.modulesOnly(),
                    course,
                    Collections.emptyList(),
                    new ProgressStore(this),
                    speech,
                    this);
            coordinator.start();
        } catch (ContentContractException exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void show(View view) {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        int padding = Math.round(16 * getResources().getDisplayMetrics().density);
        scroll.setPadding(padding, padding, padding, padding);
        scroll.addView(view, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(scroll);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBackPressed() {
        if (coordinator != null && coordinator.back()) return;
        super.onBackPressed();
    }

    @Override
    public void onUnavailable(String localeTag) {
        Toast.makeText(this, "TTS unavailable: " + localeTag, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "TTS failed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (speech != null) speech.shutdown();
        super.onDestroy();
    }
}
