# Learn-FR-DA ↔ Nenoling template sync policy

Learn-FR-DA is the reference implementation. `nenoling-app` is the reusable product template.

## Authoritative reference

The physically tested and approved reference is **Learn-FR-DA 0.5.2**, branch `release/clean-0.5.2`.

Reference rules established by 0.5.2:

- the existing Learn-FR-DA product appearance and user flow are authoritative;
- the template adapts to the proven product, not the other way around;
- the app title is followed by a short configurable purpose/audience introduction on the front page only;
- the footer is shown on the front page/modules screen only;
- version and release date belong in the footer, not the header;
- module and lesson counts are data-driven;
- lesson overview supports direct quiz entry;
- final lesson item can continue directly to the quiz;
- round previous/next navigation is reusable;
- support-language and target-language TTS remain distinct and configurable;
- quiz questions are shuffled once per attempt and remain stable during that attempt;
- answer choices are shuffled once per presented question and remain stable while feedback is shown;
- answer correctness is independent of display position;
- answer display must not leak the untranslated correct answer;
- practical resources remain separate from linguistic lesson content and can be exposed at configured placements;
- progress/persistence is silent: no technical save-state wording in the normal product UI;
- external links open outside the app; the learning app itself does not require INTERNET permission.

## What flows from Learn-FR-DA to the template

Transfer changes when they are **general engine/product improvements**, for example:

- navigation fixes;
- generic TTS improvements;
- progress/persistence fixes;
- quiz integrity rules;
- question and answer randomization;
- answer leakage prevention;
- data-driven module handling;
- reusable resource handling;
- reusable UI components;
- product-shell conventions;
- build/test automation;
- schema changes that apply to more than FR→DA.

Do not transfer FR/DA-specific linguistic corrections as engine code.

## What stays in Learn-FR-DA

- French support-language wording;
- Danish target-language wording;
- Denmark-specific content unless promoted to a configurable resource example;
- FR/DA content QA logs;
- release APK binaries;
- package/application ID and product-specific signing configuration.

## Update procedure

When Learn-FR-DA changes:

1. classify the change as engine, product pattern, or language-specific;
2. if engine/product pattern, update `nenoling-app` documentation/config/tests;
3. add a regression rule/test if the change fixed a defect;
4. record the source version/commit in the template changelog or sync documentation;
5. do not copy language-specific values into generic code.

When `nenoling-app` improves independently:

1. identify affected existing apps;
2. apply the reusable improvement to Learn-FR-DA and other Nenoling apps as appropriate;
3. preserve each app's linguistic data and stable IDs;
4. rebuild and run physical-device smoke tests before treating the change as a new product reference.

## Sync checklist

- [ ] front-page intro is configurable and front-page-only
- [ ] footer/version/date are front-page-only
- [ ] language-role generic, not pair-specific
- [ ] IDs/persistence preserved
- [ ] lesson-overview quiz callback works
- [ ] final-item-to-quiz navigation works
- [ ] quiz mapping tested per lesson
- [ ] question shuffle tested per attempt
- [ ] answer display role tested
- [ ] answer shuffle tested and stable during feedback
- [ ] TTS locales taken from config
- [ ] offline content preserved
- [ ] practical resources remain optional/external
- [ ] documentation updated
- [ ] migration note added if schema changed

## Current baseline

Template extraction began 2026-08-31 from the Learn-FR-DA 0.4.x architecture.

**Current authoritative product baseline: Learn-FR-DA 0.5.2, physically approved 2026-09-01.**

The synchronization branch `sync/fr-da-0.5.2-reference` exists specifically to bring the reusable shell and tests into parity with this approved baseline before changes are proposed for `main`.
