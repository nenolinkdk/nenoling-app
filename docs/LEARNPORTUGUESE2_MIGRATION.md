# LearnPortuguese2 modernization plan

The template can be used to bring LearnPortuguese2 up to the current Nenoling feature baseline without replacing its Portuguese linguistic content.

## Preserve

- existing Portuguese lessons/dialogues;
- Level 1/2/3 and Children content where already present;
- grammar/conjugation material;
- progress semantics where IDs can be mapped safely;
- EU Portuguese TTS locale;
- existing content that has already been linguistically reviewed.

## Adopt from Nenoling template

1. Generic support/target language roles.
2. Compact lesson UI.
3. Side-by-side support/target TTS.
4. Reusable round previous/next navigation.
5. Data-driven module discovery and counts.
6. Silent progress persistence.
7. Per-lesson quiz-integrity validation.
8. `answerDisplayRole` to prevent bilingual answer leakage.
9. Runtime answer shuffling with scoring by stable identity.
10. Separate practical-resource model where useful.
11. Version/release date from one build/config source.
12. Predictable test-APK output/delivery procedure.

## Portuguese-specific structure

Do not force Portuguese grammar into a Danish-target grammar taxonomy. The Portuguese language pack may retain dedicated:

- conjugation tables;
- tense/person paradigms;
- irregular verb notes;
- pronunciation/orthographic notes;
- Portugal-specific practical/cultural material.

If the generic content contract cannot represent a useful existing Portuguese feature, extend the template generically rather than special-casing LearnPortuguese2.

## Migration sequence

1. Inventory current LearnPortuguese2 modules, IDs and assets.
2. Map locales and support/target roles.
3. Add/normalize stable IDs without losing progress where feasible.
4. Port engine/UI features before rewriting content.
5. Validate every quiz per lesson.
6. Fix answer-display leakage if present.
7. Add answer shuffling.
8. Preserve or adapt Portuguese grammar/conjugation structures.
9. Build test APK.
10. Test on physical device.
11. Only then consider content expansion.

## Acceptance target

LearnPortuguese2 should feel like the same Nenoling product family as new apps while retaining its own language pedagogy and content.
