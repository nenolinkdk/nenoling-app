# Nenoling App Template

Reusable Android language-app architecture for Nenoling courses.

The template is derived from the working structure and documentation of **Learn-FR-DA** and is intended for future language pairs and for modernising older apps such as **LearnPortuguese2**.

## Purpose

The repository separates three concerns:

- **engine** — reusable Android behaviour: navigation, TTS, progress, quizzes, resources, version display and offline operation;
- **course structure** — Levels 1–3, Children, Grammar and practical resources;
- **language pack** — language-specific content, locales, titles, linguistic conventions and external resources.

A new app should primarily replace configuration and language-pack data, not copy and rewrite language-specific Java logic.

## Current baseline

The feature baseline follows Learn-FR-DA through the 0.4.x test series:

- compact bilingual lesson view;
- support/target TTS buttons side by side;
- reusable round previous/next navigation;
- silent progress persistence;
- data-driven modules and lesson counts;
- Level 3 professional/business module;
- Children and Grammar modules;
- separate practical-resource links;
- offline-first lessons;
- per-lesson quiz integrity;
- quiz answer display by language role (`support` or `target`);
- no bilingual answer leakage;
- randomized answer order while scoring by stable answer identity;
- version + release date from one build/config source;
- predictable test-APK packaging.

## Repository layout

```text
docs/
  ARCHITECTURE.md
  CONTENT_MODEL.md
  QUIZ_STANDARD.md
  BUSINESS_LEVEL.md
  LANGUAGE_PACKS.md
  SYNC_POLICY.md
  LEARNPORTUGUESE2_MIGRATION.md

template/
  config/
    app.template.json
    course-profile.template.json
  language-packs/
    _template/
      manifest.json
      README.md
    fr-da/
      README.md
    pt-da/
      README.md
  android/
    README.md
```

## Creating a new language app

1. Copy `template/language-packs/_template` to a new pair, for example `es-da`.
2. Fill in language codes, locales, titles, TTS roles and course identity.
3. Create the production JSON content under the language pack.
4. Keep IDs stable from the first production version.
5. Validate every lesson and quiz before generating the Android asset.
6. Configure package/application ID and display name.
7. Build and test on a physical Android device.

## Relationship to Learn-FR-DA

Learn-FR-DA is currently the reference implementation. General engine improvements discovered there should be transferred here as reusable rules/tests rather than copied as French/Danish-specific behaviour.

See `docs/SYNC_POLICY.md`.
