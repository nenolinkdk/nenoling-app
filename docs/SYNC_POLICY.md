# Learn-FR-DA ↔ Nenoling template sync policy

Learn-FR-DA is the current reference implementation. `nenoling-app` is the reusable product template.

## What flows from Learn-FR-DA to the template

Transfer changes when they are **general engine/product improvements**, for example:

- navigation fixes;
- generic TTS improvements;
- progress/persistence fixes;
- quiz integrity rules;
- answer leakage prevention;
- answer randomization;
- data-driven module handling;
- reusable resource handling;
- reusable UI components;
- build/test automation;
- schema changes that apply to more than FR→DA.

Do not transfer FR/DA-specific linguistic corrections as engine code.

## What stays in Learn-FR-DA

- French support-language wording;
- Danish target-language wording;
- Copenhagen/Denmark-specific notes unless promoted to a configurable resource example;
- FR/DA content QA logs;
- release APK binaries.

## Update procedure

When Learn-FR-DA changes:

1. classify the change as engine, product pattern, or language-specific;
2. if engine/product pattern, update `nenoling-app` documentation/config/tests;
3. add a regression rule/test if the change fixed a defect;
4. record the source version/commit in the template changelog;
5. do not copy language-specific values into generic code.

When `nenoling-app` improves independently:

1. identify affected existing apps;
2. apply the reusable improvement to Learn-FR-DA and LearnPortuguese2 as appropriate;
3. preserve each app's linguistic data and stable IDs;
4. rebuild and run physical-device smoke tests.

## Sync checklist

- [ ] language-role generic, not pair-specific
- [ ] IDs/persistence preserved
- [ ] quiz mapping tested per lesson
- [ ] answer display role tested
- [ ] answer shuffle tested
- [ ] TTS locales taken from config
- [ ] offline content preserved
- [ ] practical resources remain optional/external
- [ ] documentation updated
- [ ] migration note added if schema changed

## Current baseline

Initial template extraction: 2026-08-31, based on the documented Learn-FR-DA 0.4.x architecture and lessons learned during physical-device testing.
