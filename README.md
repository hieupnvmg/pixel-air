<p align="center">
  <img src="docs/assets/banner.svg" width="100%" alt="Pixel Air — Your Android. Your Home." />
</p>

<p align="center">
  <a href="https://github.com/hieupnvmg/pixel-air/releases/tag/v0.19"><img alt="Version 0.19 preview" src="https://img.shields.io/badge/v0.19-Preview-bdd5ff?style=flat-square&amp;labelColor=1b2638" /></a>
  <img alt="Android 13 and newer" src="https://img.shields.io/badge/Android-13%2B-a8dab5?style=flat-square&amp;labelColor=1b2638" />
  <a href="LICENSE"><img alt="Apache License 2.0" src="https://img.shields.io/badge/License-Apache_2.0-d6c4f1?style=flat-square&amp;labelColor=1b2638" /></a>
</p>

<p align="center">A lightweight, experimental Android launcher. Native views, a familiar Home screen, and room to make it yours.</p>

<p align="center">
  <strong><a href="https://github.com/hieupnvmg/pixel-air/releases/tag/v0.19">Download APK ↓</a></strong> · <a href="README.vi.md">Tiếng Việt</a> · <a href="docs/CHANGELOG.vi.md">Changelog</a> · <a href="VALIDATION.md">Testing notes</a>
</p>

## A look inside

<table>
  <tr><th>Home</th><th>App drawer</th><th>Make it yours</th></tr>
  <tr>
    <td align="center"><img src="docs/assets/home.png" width="220" alt="Pixel Air Home screen with clock, app grid, dock and search" /></td>
    <td align="center"><img src="docs/assets/drawer.png" width="220" alt="App drawer with frequent apps and app categories" /></td>
    <td align="center"><img src="docs/assets/settings.png" width="220" alt="Launcher settings for icon packs, icon size and motion" /></td>
  </tr>
</table>

Actual emulator captures from development builds. See the testing notes for device coverage.

## Unlock, in one motion

<table>
  <tr>
    <td width="270" align="center">
      <picture>
        <source media="(prefers-reduced-motion: reduce)" srcset="docs/assets/home.png" />
        <img src="docs/assets/unlock.gif" width="250" alt="Recorded unlock animation: the Home icon grid expands into place" />
      </picture>
    </td>
    <td>
      <h3>From wallpaper to your Home.</h3>
      <p>The icon grid starts small and expands together into place, without showing the finished layout first.</p>
      <ul>
        <li>One entrance per unlock</li>
        <li>Widgets and wallpaper stay still</li>
        <li>Respects Android’s animation setting</li>
      </ul>
      <a href="https://github.com/hieupnvmg/pixel-air/releases/download/v0.11/PixelAir-0.11-unlock.mp4">Watch the full recording →</a>
    </td>
  </tr>
</table>

## Features

- Multiple Home pages, a shared dock, folders, drag-and-drop shortcuts, and edge dragging between pages.
- Swipe-up app drawer with search, alphabetical sorting, or up to two rows of frequently launched apps followed by app categories.
- Optional horizontal drawer: **Settings → App drawer → Scroll direction → Horizontal**. Swipe between category pages; larger groups continue on additional pages. Search spans every page. Vertical remains the default.
- Initially expanded, collapsible category sections with themed accents and counts. Tap a header to collapse or expand it; choices persist across restarts. Pinned chips open their group before jumping, and search includes collapsed apps.
- Editable app categories; automatic categories use Android metadata and package/name fallbacks.
- Compatible installed icon packs using `appfilter.xml`, individual icon overrides, and adjustable icon sizes from 36–64 dp.
- Dark or frosted-glass drawer backgrounds with adjustable opacity and system blur where available. Default: dark at 80% opacity, category sorting.
- One live app widget: preview, bind/configure through Android, select a page, drag, resize, remove, and preserve its position across restarts.
- Optional iPhone-inspired unlock style: enlarged icons settle inward while dock icons rise and grow into place. Choose the style and use Preview in **Settings → Gestures & motion**; the existing style remains the default.
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

[Download the 0.19 prerelease APK](https://github.com/hieupnvmg/pixel-air/releases/tag/v0.19). This is a debug-signed test build, not a Google Play release.

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

<p align="center"><img src="docs/assets/drawer-collapsed.png" width="220" alt="Media category collapsed; other app groups remain visible" /></p>

<p align="center"><img src="docs/assets/drawer-horizontal.png" width="220" alt="Optional horizontal drawer with a Media page and page navigation" /></p>

Version 0.17 removes the experimental Home-return feedback because of latency reported on REDMAGIC. Task isolation and Recents exclusion were adjusted; the reported Home-to-Recents issue still needs validation on the physical device.

[Watch the optional iPhone-style unlock recording](https://github.com/hieupnvmg/pixel-air/releases/download/v0.19/PixelAir-0.19-unlock.mp4) — actual API 37 emulator recording; system lock-screen transition is unchanged.
