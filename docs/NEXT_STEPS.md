# Nenoling template roadmap

## Gate 1 — engine build

**Passed at reusable-code level on 2026-08-31.** The CI run after Gradle alignment passed both the engine unit-test step and `:engine:assembleDebug`.

Gate 1 acceptance remains protected by `.github/workflows/engine-ci.yml`; future engine changes must keep these steps green.

## Gate 2 — generic app shell

**In progress.** First shell extraction now lives in `template/android/app-shell` and depends on `:engine`.

Extracted so far:

- product/UI string configuration (`ShellConfig`);
- standard data-driven module ordering (`ModuleOrder`);
- reusable navigation state (`ShellState`);
- quiz presentation/session lifecycle (`QuizSession`), including one shuffle per presented question and identity-based scoring;
- generic progress/item/question text formatting (`ShellText`).

Next Gate 2 increment: move the reusable Android View construction from Learn-FR-DA `MainActivity` into the shell. It must consume `ShellConfig` for all labels/branding and engine course locales for TTS. FR/DA strings, note labels and transport-specific presentation must not be copied as constants.

Gate 2 pass criteria:

- app-shell unit tests pass;
- `:app-shell:assembleDebug` passes;
- home/module/lesson/item/quiz/resource rendering can be hosted by a product without language-name conditions;
- support/target TTS buttons derive language labels/locales from product configuration/data;
- footer/branding is configured by product;
- final-item navigation opens quiz and answer order remains stable after rendering/feedback.

## Gate 3 — reference product integration

Use Learn-FR-DA as the first host of the reusable engine/shell without changing its linguistic content or expected user behaviour. This is the compatibility proof.

## Gate 4 — new language app

Only after the reference product passes build + physical smoke testing against the reusable template. A new language app should then mostly consist of product configuration, branding and a validated language pack rather than copied engine code.
