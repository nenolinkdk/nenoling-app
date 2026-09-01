# Nenoling production and release convention

## Official source locations

- `nenolinkdk/nenoling-app`: reusable Nenoling engine, app shell, test host, schemas and template documentation.
- `main` in `nenoling-app`: official stable production template after reviewed/tested changes are merged.
- One product repository per language product, for example `Learn-FR-DA` and `Learn-DA-PT`.
- `main` in each product repository: official stable production source for that product.
- Development and release work happens on dedicated branches and reaches `main` only after CI and required physical testing.

## APK naming

Official product APKs MUST use a meaningful product/version filename:

- `Learn-FR-DA-0.5.2.apk`
- `Learn-DA-PT-1.0.0.apk`

Do not publish official product APKs as `app-release.apk`, `app-debug.apk`, or `test-host-debug.apk`.

Debug and synthetic smoke-test APKs are test artifacts only and must remain clearly distinguishable from production APKs.

## Official binary location

The canonical distributable APK for a released product belongs in that product repository's GitHub Release for the matching version/tag. A local release archive may mirror it under a versioned `releases/<version>/` directory, but the GitHub Release is the official distribution record.

The reusable `nenoling-app` repository does not publish a language-product APK. Its synthetic test-host APK is CI/test evidence only.

## Version identity

For each product release, these values must agree:

1. Android `versionName`.
2. Monotonically increasing Android `versionCode`.
3. Release tag/version.
4. Version shown in the app footer.
5. APK filename.
6. Release notes/date.

## Application identity

Each language product has its own stable `applicationId`. Once a production product has been distributed, its `applicationId` must not be reused for another product.

Synthetic/test hosts use isolated test-only application IDs and must never reuse a production product ID.

## Signing

Production APKs are release-signed with the approved permanent Nenoling signing identity for that product/signing strategy. Keystores and passwords are never committed to GitHub. CI signing secrets, if configured, are stored only in the repository's secret store.

Debug and smoke-test builds may use debug signing and are not production releases.

## Release gate

A product or template change is production-ready only after the relevant checks pass:

- CI/unit/build checks are green.
- Product behavior follows the approved Nenoling conventions.
- Required physical Android smoke testing has passed.
- Version/application identity is correct.
- Production APK is release-signed and signature-verified.
- APK has the canonical product/version filename.
- Release notes identify the source commit and version.

## Current references

- Approved product behavior reference: Learn-FR-DA 0.5.2.
- Reusable template synchronization reference: FR-DA 0.5.2 behavior and UI conventions.
- Next language product: Learn-DA-PT, to be created as an independent product using the stable Nenoling template and its own content/configuration/application identity.
