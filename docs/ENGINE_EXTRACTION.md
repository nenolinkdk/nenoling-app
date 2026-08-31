# Engine extraction log

## 2026-08-31 — first extraction

Source reference: `nenolinkdk/Learn-FR-DA`, PR #3 branch `cursor/quiz-answer-shuffle-0.4.2-fca8`, SHA `ac29e36b8741f6ce46dc243ab87058ec5841230c`.

Reason for using this source rather than only Learn-FR-DA `main`: the 0.4.2 source branch contains the newest reusable quiz behaviour, including answer display roles and runtime answer shuffling.

## Extracted unchanged in behaviour

- stable-ID progress persistence;
- locale-driven TTS;
- support/target text model;
- round previous/next navigation;
- answer-order shuffling and identity-based scoring.

## Genericized during extraction

- package `dk.nenolink.learnfrda` → `dk.nenolink.nenoling`;
- hard-coded course/resource asset paths → `EngineConfig`;
- expected quiz count → configuration;
- module levels → positive data-driven values;
- Denmark-specific resource URL allow-list → generic HTTPS validation;
- TTS → support/target roles and product locales.

## Build verification

Gate 1 was verified by GitHub Actions on 2026-08-31: engine unit tests passed and `:engine:assembleDebug` passed using JDK 17 / Gradle 9.6.1. The workflow remains the regression gate.

## 2026-08-31 — app-shell extraction started

Learn-FR-DA `MainActivity` mixes reusable navigation/presentation state with FR/DA labels and product-specific resource presentation. The first generic shell increment moves only the language-neutral behaviour:

- `ShellConfig`: product strings/branding contract;
- `ModuleOrder`: standard module home order;
- `ShellState`: reusable navigation state;
- `QuizSession`: per-question stable shuffle, answer lifecycle and score;
- `ShellText`: generic progress and position labels.

The complete Android View renderer is intentionally not copied verbatim. It will be extracted next and wired to `ShellConfig` + engine data so no French/Danish constants enter the reusable shell.

## Sync rule

Any future Learn-FR-DA change should be classified:

- linguistic/cultural only → remains in FR-DA language pack;
- reusable engine defect/feature → port to `template/android/engine` and add/update regression tests;
- reusable UI-shell change → port to `template/android/app-shell` and add/update shell tests.
