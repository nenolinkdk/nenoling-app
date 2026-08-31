# Nenoling template roadmap

## Gate 1 — engine build

**Passed at reusable-code level on 2026-08-31.** Engine unit tests and `:engine:assembleDebug` passed in GitHub Actions using JDK 17 / Gradle 9.6.1.

## Gate 2 — generic app shell

**In progress.** Reusable shell code now lives in `template/android/app-shell` and depends on `:engine`.

Extracted:

- configurable UI/product strings (`ShellConfig`);
- configurable palette (`ShellTheme`);
- configurable note labels (`LessonNoteLabels`);
- data-driven practical-resource placement (`ResourcePlacement`);
- standard module order (`ModuleOrder`);
- navigation state (`ShellState`);
- stable quiz session (`QuizSession`);
- progress/item/question formatting (`ShellText`);
- module/home renderer (`NenolingModuleView`);
- lesson-list renderer (`NenolingLessonListView`);
- item renderer with two TTS roles, round navigation, completion and notes (`NenolingLessonView`);
- quiz question/feedback renderer (`NenolingQuizView`).

Next increment: add a reusable shell coordinator/controller that owns transitions between MODULES → LESSONS → LESSON → ITEM → QUIZ → QUIZ_RESULT and optional RESOURCES, and which delegates actual product metadata/version/footer concerns to a thin host Activity.

Gate 2 pass criteria:

- app-shell unit tests pass;
- `:app-shell:assembleDebug` passes;
- home/module/lesson/item/quiz/resource rendering can be hosted without language-name conditions;
- support/target TTS labels/locales derive from product config/data;
- footer/branding is configured by product;
- final-item navigation opens quiz;
- answer order remains stable during feedback and scoring is identity-based.

## Gate 3 — reference product integration

Use Learn-FR-DA as the first host of the reusable engine/shell without changing linguistic content or expected user behaviour.

## Gate 4 — new language app

Only after the reference product passes build + physical smoke testing against the reusable template.
