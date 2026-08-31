# New language pack

Copy this directory to `<support>-<target>`.

Add language-specific production content in subdirectories such as:

```text
linguistic/production/
resources/
```

Keep the manifest generic-role based. Do not rename fields to `textFr`, `textDa`, `textPt`, etc. Use `support` and `target`.

The pack owns linguistic and cultural differences, including its grammar structure. The Android engine owns navigation, TTS plumbing, progress, quiz behaviour and reusable UI.
