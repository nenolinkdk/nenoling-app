# Synthetic Nenoling test host

This application is deliberately not a real language product. It proves that a Nenoling APK can be assembled from:

- `:engine`;
- `:app-shell`;
- a thin Android host;
- product configuration;
- a language-pack JSON asset.

No Learn-FR-DA `MainActivity` is copied here. The synthetic pack uses English support and a German TTS locale only to make hard-coded FR/DA assumptions visible during development; its linguistic content is test data, not a product.

Build:

```text
gradle :test-host:assembleDebug
```

Expected output:

```text
template/android/test-host/build/outputs/apk/debug/test-host-debug.apk
```
