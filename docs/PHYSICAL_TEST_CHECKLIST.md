# Nenoling synthetic host — physical test checklist

Use the CI artifact `nenoling-synthetic-host-apk` from the workflow run for the current `main` commit.

The synthetic host is not a language product. It is the integration proof for the reusable engine and app shell.

## Installation

- Install `test-host-debug.apk` on a physical Android device.
- Confirm the app launches without network access.
- Confirm the title shown is the synthetic host, not Learn-FR-DA branding.

## Navigation

- Modules screen opens.
- Open Level 1.
- Open the synthetic lesson.
- Previous is disabled on the first item.
- Next moves to the second item.
- Next on the final item opens the quiz.
- Android Back returns through the reusable shell rather than closing unexpectedly.

## Lesson presentation

- Target text is visually primary.
- Support text is shown separately below it.
- Two compact TTS buttons are visible side by side.
- Support TTS uses the support locale from course data.
- Target TTS uses the target locale from course data.
- Pronunciation note renders using the configured note label.

## Progress

- Mark item complete.
- Return to the lesson/module list and confirm progress changes.
- Close and reopen the app and confirm completion remains stored.
- Reopen the lesson and confirm the saved item position is restored when applicable.

## Quiz integrity

- The final item enters the quiz.
- Each question shows three answer options.
- Answer options remain in the same order after selecting an answer and while feedback is shown.
- Across repeated attempts, the correct answer can appear in different positions.
- Correct scoring is independent of answer position.
- The support-role question shows only support-language answer text.
- Target-role questions show only target-language answer text.
- No answer button leaks the paired translation.
- Feedback may remain bilingual.
- Final score is shown and saved.

## Offline and external behavior

- Lesson and quiz continue to work in airplane mode.
- This synthetic host has no practical resource collection, so no product-specific resource links should appear.

## Gate 2 acceptance

Gate 2 is accepted when:

1. engine unit tests pass;
2. engine assembles;
3. app-shell unit tests pass;
4. app-shell assembles;
5. synthetic host APK assembles and is published as a CI artifact;
6. the physical checks above pass without language-pair-specific code changes.

After Gate 2 acceptance, the next step is reference-product compatibility with Learn-FR-DA. Do not modify Learn-FR-DA until explicitly authorized.
