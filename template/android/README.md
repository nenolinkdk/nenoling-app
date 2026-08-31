# Nenoling Android engine

This directory now contains the first extracted, language-neutral Android engine from the Learn-FR-DA 0.4.x reference implementation.

## Baseline

Extraction source: Learn-FR-DA PR #3 / source SHA `ac29e36b8741f6ce46dc243ab87058ec5841230c` (0.4.2 answer-shuffle branch), because it includes the latest quiz fixes: per-lesson integrity, `answerDisplayRole`, no bilingual leakage and display-time answer randomisation.

The code has been moved from the product-specific package to:

`dk.nenolink.nenoling`

## Project type

`template/android` is a standalone nested Gradle project containing an **Android library** module called `engine`. It deliberately does not contain an application ID, app name, language pair or APK packaging. Product apps host this library and provide configuration + language-pack assets.

## Extracted engine components

- `config/EngineConfig` — course/resource asset paths and quiz policy
- `content/ContentModels` — support/target data model
- `content/ContentRepository` — schema parser/validator with configurable course asset
- `content/QuizIntegrity` — per-lesson mapping, display-role and leakage validation
- `content/AnswerOrder` — randomized display order with identity-based scoring
- `content/ResourceModels` + `ResourceRepository` — optional resources without Denmark URL hard-coding
- `progress/ProgressStore` — stable-ID persistence
- `speech/SpeechController` — locale-driven Android TTS
- `ui/RoundNavBar` — reusable compact arrow navigation
- `ui/ExternalResourceLauncher` — browser hand-off for optional external resources
- unit tests for answer ordering and display-role behaviour

## Deliberate differences from Learn-FR-DA

The engine does **not** hard-code:

- `fr`, `da` or any other language code;
- `fr-FR`, `da-DK` or another locale;
- `course.fr-da`;
- `content/fr-da/...` asset paths;
- `dk.nenolink.learnfrda`;
- Danish transport URLs;
- `notaguidedtour.com`;
- app/version/release names.

These values belong to the product configuration/language pack.

The engine also no longer restricts level numbers to 1/2/3. A product profile can still use the standard Levels 1–3, while another language product can add a different level structure without modifying engine code.

## Build verification

This commit is a source extraction performed through GitHub. It has not yet been compiled in a local Android SDK environment.

Next verification step, from this directory on a machine with Android SDK/Gradle available:

```text
gradle :engine:testDebugUnitTest

gradle :engine:assembleDebug
```

After that succeeds, add a Gradle wrapper to this nested project or promote the engine to the repository root if desired.

Do not create a real new language app until this library build is green.
