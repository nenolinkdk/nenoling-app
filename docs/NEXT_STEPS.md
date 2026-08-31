# Nenoling template roadmap

## Gate 1 — engine build

Current priority. GitHub Actions workflow `.github/workflows/engine-ci.yml` compiles the extracted Android library and runs its unit tests.

Pass criteria:

- `:engine:testDebugUnitTest` passes;
- `:engine:assembleDebug` passes;
- no language-pair-specific constants are introduced to make the build green.

If CI fails, repair the generic engine and document material changes in `ENGINE_EXTRACTION.md`.

## Gate 2 — generic app shell

Only after Gate 1 is green, extract reusable presentation from Learn-FR-DA. The shell must receive strings, locale labels, branding and resource presentation through product configuration/data rather than French/Danish conditions.

Candidate shell responsibilities:

- home/module/lesson navigation;
- compact support/target panels;
- TTS controls;
- round previous/next navigation;
- final-item-to-quiz transition;
- quiz screen and feedback;
- progress presentation;
- optional resource presentation;
- generic footer slots.

Do not move French wording, Danish wording, transport-specific copy or product branding into the shell.

## Gate 3 — reference product integration

Use Learn-FR-DA as the first host of the reusable engine/shell without changing its linguistic content or expected user behaviour. This is the compatibility proof.

## Gate 4 — new language app

Only after the reference product passes build + physical smoke testing against the reusable template. A new language app should then mostly consist of product configuration, branding and a validated language pack rather than copied engine code.
