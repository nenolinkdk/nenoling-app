# Nenoling app shell

Reusable presentation/state layer above `:engine`.

This first extraction takes the language-neutral behaviour that was mixed into Learn-FR-DA `MainActivity` and separates it from the actual Android Activity rendering.

Included now:

- configurable UI/product strings via `ShellConfig`;
- standard module ordering (numeric levels, Children, other modules, Grammar last);
- generic navigation state via `ShellState`;
- stable per-question answer shuffle and score lifecycle via `QuizSession`;
- generic item/question/progress text formatting via `ShellText`;
- tests for module order and quiz-session stability.

Not yet moved into this module: the complete Android View construction from `MainActivity`. That remains the next part of Gate 2, because its colours, button styling, note labels, footer, TTS button labels and resource presentation need to consume `ShellConfig` rather than FR-DA resources.

The product host must not add language-name conditionals to this module.
