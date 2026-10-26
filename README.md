# Pixel Air

A lightweight, experimental Android launcher inspired by Pixel-style Home screens, written in Java with native Android views. Originally built for a REDMAGIC 11 Air running Android 16.

**Current version: 0.11 · Android 13+ · Apache License 2.0**

[Tiếng Việt](README.vi.md) · [Changelog](docs/CHANGELOG.vi.md) · [Testing notes](VALIDATION.md)

## Features

- Multiple Home pages, a shared dock, folders, drag-and-drop shortcuts, and edge dragging between pages.
- Swipe-up app drawer with search, alphabetical sorting, or up to two rows of frequently launched apps followed by app categories.
- Editable app categories; automatic categories use Android metadata and package/name fallbacks.
- Compatible installed icon packs using `appfilter.xml`, individual icon overrides, and adjustable icon sizes from 36–64 dp.
- Dark or frosted-glass drawer backgrounds with adjustable opacity and system blur where available. Default: dark at 80% opacity, category sorting.
- One live app widget: preview, bind/configure through Android, select a page, drag, resize, remove, and preserve its position across restarts.
- Unlock entrance animation: icons are staged behind the keyguard, then expand together from a smaller layout on the first eligible Home draw.
- Dedicated settings and automatic English/Vietnamese localization.

## Status and limitations

This is an early prototype, not the official Google Pixel Launcher. It does not include proprietary Pixel Launcher code or APKs.

- Current builds have been exercised on an Android 17 / API 37 emulator. Android 16 on a physical REDMAGIC has **not** been independently verified by the maintainer.
- Only **one widget** is supported at a time. Saving a new widget replaces the existing widget; cancelling preserves it.
- App-to-Home transitions, Recents, the lock screen, biometrics, and system navigation remain controlled by the device ROM. Pixel/Quickstep-level system integration is not included.
- Frequent apps are ranked by launches from Pixel Air, not by device-wide usage history.
- Icon pack support focuses on resource mappings. Unsupported icons fall back to the original app icon.
- Automatic app categories can be incorrect; they can be overridden in settings.

## Download

[Download the 0.11 prerelease APK](https://github.com/hieupnvmg/pixel-air/releases/tag/v0.11). This is a debug-signed test build, not a Google Play release.

## Build

Install Android Studio, Android SDK Platform **37**, and **JDK 25**. The checked-in Gradle daemon configuration requests JDK 25; Java source/bytecode compatibility is 17. The wrapper pins Gradle 9.6.0 with a distribution checksum, and the project uses Android Gradle Plugin 9.4.1.

1. Clone this repository and open its folder in Android Studio.
2. Let Android Studio create `local.properties`, or set `ANDROID_HOME` to your Android SDK installation.
3. Build and run checks:

```sh
./gradlew assembleDebug lintDebug
./scripts/test-models.sh
```

On macOS, you can use Android Studio's bundled JDK if it is version 25:

```sh
export JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home'
./gradlew assembleDebug lintDebug
./scripts/test-models.sh
```

The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`. It is a **debug build**, intended for testing. Release signing keys and machine-specific SDK configuration are not stored in this repository.

The model test script requires a POSIX shell, such as macOS/Linux, Git Bash, or WSL. On Windows you can also build the Android app from Android Studio.

## Using the launcher

- Install the APK, open Pixel Air, then select it as your Home app in Android's default-app settings.
- Swipe up for the app drawer and left/right for Home pages.
- Hold empty Home space for wallpaper, settings, widgets, or direct layout editing.
- Hold a drawer app and drag it onto Home to create a shortcut.
- Hold a widget and release to open placement controls. Use the corner handle to resize; **Save**, **Cancel**, and **Remove widget** are in the toolbar.
- Unlock animation can be disabled under **Gestures & motion**. It also respects Android's animation-disable setting.

## Contributing

Bug reports and pull requests are welcome. Include the phone model, Android/ROM version, launcher version, reproduction steps, and whether the problem also occurs with animations or blur disabled. Avoid posting account details or private notifications in screenshots and logs.

Run the build/lint checks and the model tests before submitting a change. Document any device-specific behavior and keep existing workspace data compatible where possible.

## License

Pixel Air source code is available under the [Apache License 2.0](LICENSE). See [NOTICE](NOTICE) for third-party notices. Product names and third-party app logos belong to their respective owners.
