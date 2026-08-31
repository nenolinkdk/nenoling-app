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

### Package

`dk.nenolink.learnfrda` → `dk.nenolink.nenoling`

### Course asset

FR-DA hard-coded course path → `EngineConfig.courseAsset`.

### Resource asset

FR-DA hard-coded resource path → optional `EngineConfig.resourcesAsset`.

### Quiz size

Three questions remains the standard course profile, but the engine receives the expected count from configuration. The reusable library is not permanently limited to three.

### Module levels

The reference app validates Levels 1–3. The generic parser accepts positive level numbers so a future product can define another level structure.

### Resource URLs

The FR-DA repository had a Denmark-specific URL allow-list. Generic engine validation now requires valid HTTPS URLs only. Product/language-pack QA owns the approved destination list.

### TTS

Both support and target remain generic roles. Primary TTS role may be either support or target if a future product profile requires it.

## Not extracted yet

`MainActivity` is still too product/presentation specific to copy unchanged. It contains French UI strings, FR/DA TTS labels, fixed note labels and transport-specific presentation. The core engine is extracted first as requested.

The next engine phase is a reusable **app-shell/presenter** that consumes product UI strings/configuration and the engine library without language-name conditionals. Do this only after the current library compiles and its tests pass.

## Sync rule

Any future Learn-FR-DA change should be classified:

- linguistic/cultural only → remains in FR-DA language pack;
- reusable engine defect/feature → port to `nenoling-app/template/android/engine` and add/update regression tests;
- reusable UI-shell change → port to the later generic app-shell layer.
