# Content model

## Standard course size

The Learn-FR-DA reference pattern uses 10 lessons per teaching module, 10 communication items per lesson and 3 quiz questions per lesson.

This is a **default product pattern**, not an engine limitation.

For a full five-module course using Level 1, Level 2, Level 3, Children and Grammar, the reference size is:

- 50 lessons;
- 500 linguistic items;
- 150 quiz questions.

Practical resources are counted separately.

## Generic text pair

Bilingual content uses role names rather than language names:

```json
{
  "support": "Support-language text",
  "target": "Target-language text"
}
```

## Language configuration

Each course declares support and target languages and locales. Example only:

```json
{
  "supportLanguage": {"language": "fr", "locale": "fr-FR"},
  "targetLanguage": {"language": "da", "locale": "da-DK"}
}
```

A different app replaces these values without modifying generic engine code.

## Modules

Supported reference module types:

- `level`
- `children`
- `grammar`

Resources are kept outside ordinary linguistic items.

## Lessons

A lesson contains:

- stable `id`;
- `moduleId`;
- `order`;
- bilingual title;
- bilingual situation/context;
- tags;
- ordered items;
- quiz.

## Items

Items use stable IDs and ordered bilingual text. TTS is configured by generic role + locale. Notes can carry linguistic, cultural, digital or practical information without overloading dialogue text.

## Quizzes

Every question contains stable IDs, bilingual prompt data, answer objects, one correct answer and an explicit display role where required.

Reference field:

```json
"answerDisplayRole": "support"
```

or

```json
"answerDisplayRole": "target"
```

See `QUIZ_STANDARD.md`.

## Source of truth

Human-edited linguistic production files are the source of truth. Generated/canonical Android assets must be reproducible from them. Do not correct only the generated asset.

## Content QA

Before release check:

- semantic equivalence support ↔ target;
- natural target-language use;
- appropriate support-language register;
- no repeated generic generated dialogue patterns;
- no remnants from another language pair;
- correct locales and TTS roles;
- correct characters/accents;
- stable IDs;
- quiz references and answer roles.
