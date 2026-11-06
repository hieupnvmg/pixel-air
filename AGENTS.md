# Working on Pixel Air

Pixel Air is an Android Home launcher written in Java with native Android views. The package and application ID are `dev.pixelair.launcher`. It supports Android 13 and later (min SDK 33) and targets API 36. REDMAGIC 11 Air Global on Android 16 is an important physical test device. Keep emulator results separate from device results.

## Build and check

Use the checked-in Gradle wrapper, JDK 25 for Gradle, and Android SDK Platform 37. App source and bytecode use Java 17. Configure the SDK through `ANDROID_HOME` or Android Studio's local `local.properties`. See [README.md](README.md#build) for setup.

```sh
./gradlew assembleDebug lintDebug
./scripts/test-models.sh
```

The debug APK is at `app/build/outputs/apk/debug/app-debug.apk`. The shell script runs plain Java model checks from `tests/`. Add or update a focused check when changing model behavior that the existing checks do not cover. For UI and lifecycle changes, also exercise the affected flow with Pixel Air selected as the default Home app. Report the Android version, ROM or emulator, and any checks you could not run.

## Code map

The Java files below are under `app/src/main/java/dev/pixelair/launcher/`.

| Area | Start here |
| --- | --- |
| Home, drawer wiring, gestures and lifecycle | `HomeActivity` |
| Home pages, dock and persistence | `WorkspacePager`, `WorkspaceState`, `HomeActivity` |
| Drawer ranking, grouping and pagination | `AppRanking`, `AppCategories`, `DrawerSections`, `DrawerSectionView`, `DrawerPages` |
| Settings, defaults and editors | `SettingsActivity`, `LauncherDefaults`, `LauncherEditors` |
| Widgets | `WidgetWorkspace`, `WidgetGeometry`, widget host and placement in `HomeActivity` |
| Icon packs | `IconPackManager`, `IconPackMapping` |
| Unlock and gesture motion | `UnlockGate`, `UnlockIconAnimator`, `SwipePhysics`, `SpringMotion` |
| Home registration and task behavior | `app/src/main/AndroidManifest.xml`, `HomeActivity` |

## Compatibility and regression checks

- Preserve existing Home layouts, folders, dock entries, widget IDs and launcher settings across upgrades. Review the `home` SharedPreferences keys and workspace restore path before changing defaults or storage. Add an upgrade path when changing the stored format.
- Put user-facing strings in `app/src/main/res/values/strings.xml` and update `values-vi/strings.xml` alongside them. Android selects the language from the device settings.
- For drawer changes, check vertical and horizontal modes, search across categories (including collapsed ones), category expansion, and drag-to-Home.
- For widget changes, check placement, moving, resizing, saving, cancelling and removing. The launcher supports one widget. Cancelling a replacement should preserve the existing widget.
- For Home and motion changes, check continuous wallpaper behind both system bars, return from an app, and a real lock/unlock cycle. Check that unlock plays once when enabled and respects both launcher and system animation settings. Lock screen, Recents and app-to-Home transitions also depend on the device ROM. Keep claims about these behaviors tied to the devices tested.
- `v0.18.1` restored the `v0.18` app behavior after the experimental unlock styles in 0.19–0.21 flashed on REDMAGIC. Keep that rollback in mind before reusing code from those releases. See [VALIDATION.md](VALIDATION.md) and the [Vietnamese changelog](docs/CHANGELOG.vi.md) for prior results.

## Collaborating

Check `git status` first and preserve changes from other contributors. Keep patches scoped and reuse the existing views and models. In a PR, describe the user-visible change and list the checks that ran. Keep machine-specific SDK paths, signing keys and generated APKs out of commits. Bump version metadata and publish releases only when requested.
