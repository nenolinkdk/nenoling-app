# Nenoling template roadmap

## Gate 1 — engine build

Passed. Engine unit tests and `:engine:assembleDebug` are protected by CI.

## Gate 2 — generic app shell

The reusable shell now contains module, lesson, item, quiz, result and resource renderers plus `ShellCoordinator`.

A synthetic Android product host has been added as the Gate 2 integration proof. It contains no Learn-FR-DA Activity code and supplies only a thin host, product configuration and synthetic language-pack JSON.

Gate 2 pass criteria:

- `:app-shell:testDebugUnitTest` passes;
- `:app-shell:assembleDebug` passes;
- `:test-host:assembleDebug` passes;
- CI publishes `nenoling-synthetic-host-apk`;
- synthetic host can navigate modules → lesson → items → quiz → result;
- final item opens quiz;
- support/target answer display remains isolated;
- TTS locales come from the course data;
- progress is stored by stable IDs.

## Gate 3 — reference product integration

After Gate 2 is green, use Learn-FR-DA as the first real compatibility host without changing its linguistic content or expected user behaviour. This requires explicit authorization before modifying Learn-FR-DA.

## Gate 4 — product migration / new app

After reference compatibility, modernize LearnPortuguese2 or create a new language pair primarily from configuration, branding and a validated language pack rather than copied engine code.
