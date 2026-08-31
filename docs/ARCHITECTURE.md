# Architecture

## 1. Separation of concerns

Nenoling apps consist of:

### Engine
Reusable Android behaviour independent of the language pair:

- course loading and validation;
- module/lesson/item navigation;
- support and target TTS;
- quiz rendering, scoring and persistence;
- answer shuffling;
- progress persistence;
- external resource launching;
- version/release-date display;
- compact reusable UI components.

### Course profile
Defines which modules exist and their ordering. A full app may contain:

1. Level 1 — visitor/travel situations
2. Level 2 — everyday life and work
3. Level 3 — professional/business communication
4. Children — age-appropriate everyday language
5. Grammar — practical grammar/reference
6. Practical resources — links/information separate from lesson items

The engine must not assume that every future course has every module. Modules are data-driven.

### Language pack
Contains language-specific data:

- support language and locale;
- target language and locale;
- course titles and module titles;
- linguistic production JSON;
- grammar explanations;
- cultural notes;
- practical-resource links;
- terminology policy;
- optional branding/text overrides.

## 2. Language roles

Use generic roles everywhere:

- `support` — language used to help the learner;
- `target` — language being learned.

Do not hard-code `French`, `Danish`, `Portuguese`, `fr-FR`, `da-DK` or `pt-PT` in generic engine code.

TTS buttons, quiz answer display and labels are resolved from the language-pack manifest.

## 3. Data flow

```text
linguistic production JSON
        ↓ validate
canonical course asset
        ↓
ContentRepository
        ↓
models
        ↓
UI / TTS / Quiz / Progress
```

Practical resources follow a separate path:

```text
resource production JSON
        ↓ validate
resources asset
        ↓
ResourceRepository
        ↓
external browser via ACTION_VIEW
```

Lessons must remain usable offline. External links are optional actions and must not make lesson loading depend on a network connection.

## 4. Stable identities

Stable IDs are part of the persistence contract. Do not derive progress solely from array position. Course, module, lesson, item, quiz, question and answer IDs should be globally unambiguous within the course.

## 5. Reusable UI baseline

- compact support/target text panels;
- small support/target TTS buttons in one row;
- reusable round previous/next navigation adjacent to lesson content;
- final-next action opens the lesson quiz;
- no technical “position saved” noise in learner UI;
- grammar normally appears after teaching/audience modules;
- footer/brand links are configuration, not hard-coded language logic;
- version and release date come from one build/config source.

## 6. Test layers

A new app should have:

1. schema/content validation;
2. per-lesson quiz-integrity validation;
3. repository/model tests using the same mapping as runtime;
4. answer-display/no-leakage tests;
5. answer-shuffle/scoring tests;
6. Android build test;
7. emulator smoke test where available;
8. physical-device test before release.
