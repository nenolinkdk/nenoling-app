# Nenoling app shell

Reusable presentation/state layer above `:engine`.

This extraction takes the language-neutral behaviour that was mixed into Learn-FR-DA `MainActivity` and separates it from product-specific Android rendering.

Included now:

- configurable UI/product strings via `ShellConfig`;
- configurable colours via `ShellTheme`;
- configurable note labels via `LessonNoteLabels`;
- data-driven practical-resource placement via `ResourcePlacement`;
- standard module ordering via `ModuleOrder`;
- generic navigation state via `ShellState`;
- stable quiz lifecycle via `QuizSession`;
- generic text formatting via `ShellText`;
- `NenolingModuleView` for home/module rendering;
- `NenolingLessonListView` for lesson lists;
- `NenolingLessonView` for item rendering, TTS, navigation, completion and notes;
- `NenolingQuizView` for question and feedback rendering;
- tests for module order and quiz-session stability.

Still intentionally outside the reusable shell: a concrete product `Activity` that wires repository loading, version metadata, progress persistence and footer branding to these renderers. That host should be thin and contain no language-specific branching.

The next Gate 2 increment is a reusable controller/coordinator that drives these views and handles final-item-to-quiz, back navigation, progress display and optional resource screens without reintroducing FR/DA conditions.
