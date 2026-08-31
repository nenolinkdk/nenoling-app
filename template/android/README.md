# Android engine area

This directory is reserved for the reusable Android engine extracted from the reference apps.

The engine should contain generic implementations for:

- course/resource repositories and models;
- progress store;
- speech/TTS controller;
- compact lesson UI;
- reusable round navigation;
- quiz display role;
- answer shuffling and scoring;
- external-resource launcher;
- build/version configuration;
- automated content/runtime tests.

## Extraction rule

Do not copy `dk.nenolink.learnfrda` and then accumulate language-specific conditionals. Extract/genericize to a neutral package such as `dk.nenolink.nenoling` and move pair-specific values to config/language packs.

Before this folder becomes the canonical buildable Android engine, compare it with the latest **source** commits of Learn-FR-DA (not only APK binaries) so answer-display and shuffle fixes are included in code and tests.
