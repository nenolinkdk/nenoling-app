# Language packs

## Goal

The engine stays stable while each product supplies its own linguistic structure and cultural content.

Recommended layout:

```text
template/language-packs/
  _template/
    manifest.json
    README.md
    linguistic/
      production/
    resources/
  fr-da/
  pt-da/
  es-da/
  ...
```

Git does not retain empty directories, so create the `linguistic/production` and `resources` folders when the first files are added.

## Manifest responsibilities

A language-pack manifest should define:

- pair ID;
- support language code/name/locale;
- target language code/name/locale;
- primary TTS role;
- app/course display titles;
- module profile;
- optional footer/site link;
- country/market tags;
- content version.

## Individual language structures

Not every language pair needs identical grammar organization or cultural notes. The engine should accept the same core content contract while allowing language-specific production files.

Examples:

- Portuguese may need dedicated conjugation/verb paradigms;
- Danish-target courses may need V2/subordinate-clause word-order material;
- Romance-target courses may need gender/agreement and richer verb tables;
- a children module may use shorter/directer language and different situation sets.

These differences live inside the language pack. They should not create language-name conditionals in generic UI code.

## Shared vs individual content

**Shared product structure:** lesson count conventions, quiz rules, progress, TTS roles, UI, business-module concept.

**Individual language structure:** actual vocabulary, dialogues, grammar taxonomy, locale, cultural notes, target-country practical resources.

## Starting a new pack

Copy `_template`, rename it to `<support>-<target>`, fill the manifest and create linguistic production files. Validate the pack before adding it to an Android product.
