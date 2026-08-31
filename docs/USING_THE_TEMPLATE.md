# Using the Nenoling template

This is the operational checklist for creating or maintaining a Nenoling language app. Read this together with `ENGINE_EXTRACTION.md` and the content schema before changing a product.

## Architecture

Keep these layers separate:

1. **Engine** (`template/android/engine`) — reusable Android mechanics only.
2. **App shell** — reusable presentation/navigation that consumes configuration; still to be extracted after engine CI is green.
3. **Language pack/product** — course JSON, language pair, locales, grammar, cultural notes, approved external resources, branding and product metadata.

Do not copy language-specific values into the engine to make a product build.

## Creating a language product

Before coding, define a product profile containing at least:

- product/repository name and Android application ID;
- support language + BCP-47 locale;
- target language + BCP-47 locale;
- course asset path;
- optional resources asset path;
- expected quiz questions per lesson;
- product title, version and release date;
- branding/footer/site link;
- module plan (normally Levels 1–3, Children and practical grammar, but engine does not require that exact plan).

Then supply course content conforming to schema v1. Stable IDs are part of persisted progress and must not be casually renamed after release.

## Mandatory quiz rules

For the standard Nenoling profile every lesson has a quiz and every question:

- is `single-choice`;
- has exactly one `correct: true` answer;
- declares `answerDisplayRole` as `support` or `target`;
- displays only that role on answer buttons;
- may keep both roles in the stored answer object for explanation/data purposes;
- randomizes answer order at display time;
- scores by answer identity / `correct`, never by source-array or button position.

Any change to quiz rendering must preserve regression coverage for bilingual leakage and randomized positions.

## TTS

TTS is locale-driven. Product data supplies locales. Never add conditions such as `if French`, `if Danish`, etc. to engine code. If a voice is unavailable, the UI should report that locale/voice availability problem rather than silently substitute another language.

## Progress

Progress is stored against stable IDs using course + module + lesson + entity identity. Content editing must preserve existing IDs unless an intentional migration/reset is planned.

## External resources

The engine validates HTTPS URLs and launches them through Android browser intents. Product QA owns the approved URL list. External browser hand-off does not require the app to request Android INTERNET permission.

## Change classification

Before implementing a change, classify it:

- language/grammar/culture/content → product language pack only;
- reusable data/parser/quiz/progress/TTS defect → engine + regression test;
- reusable visual/navigation behaviour → app shell once that layer exists;
- branding/product metadata → product configuration only.

If a reusable defect is discovered first in a product, fix/test it in the product as necessary, then port the generic fix and regression test to this template. Do not allow product forks to become separate engines.

## Required verification before a new product

The engine CI must be green. After the app shell exists, its tests/build must also be green. Then create the new product from the documented template, add the language pack, run content validation, build a test APK and perform physical-device smoke testing.

Minimum physical smoke test: module navigation, one lesson per module, final-item-to-quiz transition, one quiz per module, randomized answer order, scoring, progress persistence, both TTS roles, offline lesson use and any external resource/browser hand-off.

## Source reference

The first engine extraction used Learn-FR-DA 0.4.2 source SHA `ac29e36b8741f6ce46dc243ab87058ec5841230c`. Later template changes supersede that reference; use this repository's `main` as the reusable source of truth once CI is green.
