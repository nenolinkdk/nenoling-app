# Quiz standard

This is a mandatory reusable rule set for Nenoling apps.

## 1. Three questions per standard lesson

A standard lesson has exactly three questions unless the course profile explicitly specifies otherwise.

Validation is per lesson. A global total such as “150 questions” is insufficient because questions can exist while being mapped to the wrong lesson.

For every lesson verify:

- quiz reference resolves;
- exactly three questions resolve;
- prompt is non-empty;
- answer list is non-empty;
- exactly one answer is correct;
- all IDs/references resolve;
- final lesson navigation reaches the quiz.

## 2. No answer leakage

A translation question tests one direction at a time.

Target → support:

```text
Question gives target word/phrase
Answers show SUPPORT language only
```

Support → target:

```text
Question gives support word/phrase
Answers show TARGET language only
```

Keep bilingual answer objects when useful for feedback/reuse, but render only the role needed to solve the question.

Use generic metadata such as:

```json
"answerDisplayRole": "support"
```

or `target`.

Never implement this as `if French then...` or `if Danish then...`.

Grammar questions need manual semantic review: show only the forms necessary to solve the grammar problem. Explanatory translations can be shown after answering if useful, but must not reveal the answer beforehand.

## 3. Randomized answer order

The correct answer must not systematically be first.

Rules:

1. Preserve stable answer objects and correct identity.
2. Shuffle answer objects once when a question is presented.
3. Score by stable answer ID / correct flag, never by displayed position.
4. Keep the shuffled order stable while the user answers and feedback is shown.
5. A later attempt may create a new order.

Do not manually reorder JSON files to simulate randomness.

Required regression tests:

- correct identity survives shuffle;
- no answer lost or duplicated;
- number of answers unchanged;
- correct answer can occur in every visible position;
- selecting correct shuffled answer scores correct;
- selecting wrong shuffled answer scores wrong;
- display role still works after shuffle.

## 4. Question types

Useful patterns include:

- target vocabulary → support meaning;
- support vocabulary → target equivalent;
- support situation → choose correct target expression;
- target phrase → choose support meaning;
- target grammar/form choice;
- contextual professional/everyday response.

Avoid 150 variations of the same vocabulary lookup. A course should mix recognition, production-oriented choice, comprehension and contextual use.

## 5. Persistence

Quiz result and lesson completion must survive app/process restart. Persistence must not depend on answer position because positions are randomized.

## 6. Physical-device regression path

Always test the last lesson of each module. A previously observed class of failure appeared after scrolling/navigating to module boundaries even when global content tests passed.
