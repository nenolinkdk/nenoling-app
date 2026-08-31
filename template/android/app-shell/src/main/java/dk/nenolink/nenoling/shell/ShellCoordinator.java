package dk.nenolink.nenoling.shell;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import dk.nenolink.nenoling.content.ContentModels.Answer;
import dk.nenolink.nenoling.content.ContentModels.Course;
import dk.nenolink.nenoling.content.ContentModels.Item;
import dk.nenolink.nenoling.content.ContentModels.Lesson;
import dk.nenolink.nenoling.content.ContentModels.Module;
import dk.nenolink.nenoling.content.ContentModels.Quiz;
import dk.nenolink.nenoling.content.ResourceModels.ExternalResource;
import dk.nenolink.nenoling.content.ResourceModels.ResourceCollection;
import dk.nenolink.nenoling.progress.ProgressStore;
import dk.nenolink.nenoling.speech.SpeechController;

/** Reusable controller between product host, engine data and shell views. */
public final class ShellCoordinator {
    private final Context context;
    private final ShellHost host;
    private final ShellConfig config;
    private final ShellTheme theme;
    private final LessonNoteLabels noteLabels;
    private final ResourcePlacement resourcePlacement;
    private final Course course;
    private final List<ResourceCollection> resources;
    private final ProgressStore progress;
    private final SpeechController speech;
    private final SpeechController.Listener speechListener;
    private final ShellState state = new ShellState();

    private Module module;
    private Lesson lesson;
    private QuizSession quizSession;
    private boolean lastAnswerCorrect;

    public ShellCoordinator(Context context, ShellHost host, ShellConfig config, ShellTheme theme,
                            LessonNoteLabels noteLabels, ResourcePlacement resourcePlacement,
                            Course course, List<ResourceCollection> resources,
                            ProgressStore progress, SpeechController speech,
                            SpeechController.Listener speechListener) {
        if (context == null || host == null || config == null || course == null || progress == null || speech == null) {
            throw new IllegalArgumentException("context, host, config, course, progress and speech are required");
        }
        this.context = context;
        this.host = host;
        this.config = config;
        this.theme = theme == null ? ShellTheme.neutral() : theme;
        this.noteLabels = noteLabels == null ? LessonNoteLabels.defaults() : noteLabels;
        this.resourcePlacement = resourcePlacement == null ? ResourcePlacement.modulesOnly() : resourcePlacement;
        this.course = course;
        this.resources = resources == null ? Collections.emptyList() : resources;
        this.progress = progress;
        this.speech = speech;
        this.speechListener = speechListener;
    }

    public ShellState state() { return state; }

    public void start() { state.modules(); renderModules(); }

    public boolean back() {
        switch (state.screen) {
            case MODULES: return false;
            case LESSONS:
            case RESOURCES: start(); return true;
            case LESSON:
            case ITEM:
            case QUIZ:
            case QUIZ_RESULT:
                if (module != null) { openModule(module); return true; }
                start(); return true;
            default: return false;
        }
    }

    private void renderModules() {
        NenolingModuleView view = new NenolingModuleView(context, config, theme);
        host.show(view.build(course.modules, this::moduleProgress,
                resourcePlacement.showOnModules && !resources.isEmpty(),
                new NenolingModuleView.Actions() {
                    @Override public void open(Module selected) { openModule(selected); }
                    @Override public void openResources() { renderResources(); }
                }));
    }

    private void openModule(Module selected) {
        module = selected;
        lesson = null;
        quizSession = null;
        state.lessons(selected.id);
        NenolingLessonListView view = new NenolingLessonListView(context, config, theme);
        host.show(view.build(selected, this::lessonProgress, new NenolingLessonListView.Actions() {
            @Override public void back() { start(); }
            @Override public void open(Lesson selectedLesson) { openLesson(selectedLesson); }
        }));
    }

    private void openLesson(Lesson selected) {
        lesson = selected;
        state.lesson(module.id, selected.id);
        renderItem(restoredItemIndex(selected));
    }

    private int restoredItemIndex(Lesson selected) {
        String saved = progress.getLastPosition();
        if (saved == null || saved.isEmpty()) return 0;
        for (int i = 0; i < selected.items.size(); i++) {
            if (progress.progressId(course.id, module.id, selected.id, selected.items.get(i).id).equals(saved)) return i;
        }
        return 0;
    }

    private void renderItem(int index) {
        if (lesson == null || lesson.items.isEmpty()) return;
        state.item(index);
        Item item = lesson.items.get(index);
        String itemProgressId = progress.progressId(course.id, module.id, lesson.id, item.id);
        progress.saveLastPosition(itemProgressId);
        boolean complete = progress.isItemComplete(itemProgressId);
        NenolingLessonView view = new NenolingLessonView(context, config, theme, noteLabels, speech, speechListener);
        host.show(view.build(item, index, lesson.items.size(), complete, index > 0,
                course.supportLanguage.locale, course.targetLanguage.locale,
                new NenolingLessonView.Actions() {
                    @Override public void previous() { if (index > 0) renderItem(index - 1); }
                    @Override public void next() {
                        if (index + 1 < lesson.items.size()) renderItem(index + 1);
                        else openQuiz();
                    }
                    @Override public void markComplete() { progress.markItemComplete(itemProgressId); renderItem(index); }
                }));
    }

    private void openQuiz() {
        Quiz quiz = lesson == null ? null : lesson.quiz;
        if (quiz == null || quiz.questions.isEmpty()) { openModule(module); return; }
        state.quiz();
        quizSession = new QuizSession(quiz);
        renderQuizQuestion();
    }

    private void renderQuizQuestion() {
        NenolingQuizView view = new NenolingQuizView(context, config, theme);
        host.show(view.buildQuestion(quizSession, new NenolingQuizView.Actions() {
            @Override public void back() { openModule(module); }
            @Override public void answer(Answer answer) { lastAnswerCorrect = quizSession.answer(answer); renderQuizFeedback(); }
            @Override public void next() { }
        }));
    }

    private void renderQuizFeedback() {
        NenolingQuizView view = new NenolingQuizView(context, config, theme);
        host.show(view.buildFeedback(quizSession, lastAnswerCorrect, new NenolingQuizView.Actions() {
            @Override public void back() { openModule(module); }
            @Override public void answer(Answer answer) { }
            @Override public void next() { if (quizSession.next()) renderQuizQuestion(); else finishQuiz(); }
        }));
    }

    private void finishQuiz() {
        String quizProgressId = progress.progressId(course.id, module.id, lesson.id, lesson.quiz.id);
        progress.saveQuizResult(quizProgressId, quizSession.score(), quizSession.totalQuestions());
        state.screen = ShellState.Screen.QUIZ_RESULT;
        NenolingResultView result = new NenolingResultView(context, config, theme);
        host.show(result.build(quizSession.score(), quizSession.totalQuestions(), () -> openModule(module)));
    }

    private void renderResources() {
        state.screen = ShellState.Screen.RESOURCES;
        NenolingResourceView view = new NenolingResourceView(context, config, theme);
        host.show(view.build(resources, this::openExternalResource, this::start));
    }

    private void openExternalResource(ExternalResource resource) {
        dk.nenolink.nenoling.ui.ExternalResourceLauncher.open(context, resource.url);
    }

    private String lessonProgress(Lesson value) {
        return "\n" + ShellText.progress(progress.countCompleted(course.id, module.id, value.id), value.items.size());
    }

    private String moduleProgress(Module value) {
        int completed = 0;
        int total = 0;
        for (Lesson current : value.lessons) {
            completed += progress.countCompleted(course.id, value.id, current.id);
            total += current.items.size();
        }
        return total == 0 ? "" : "\n" + ShellText.progress(completed, total);
    }
}
