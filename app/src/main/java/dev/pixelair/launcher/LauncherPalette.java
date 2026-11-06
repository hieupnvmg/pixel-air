package dev.pixelair.launcher;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Arrays;

/**
 * Launcher UI colors. "Classic" keeps the original fixed navy palette; "dynamic" maps the same roles
 * onto Android's wallpaper-derived system tonal palettes (Material You, API 31+; Pixel Air needs 33+).
 * Text drawn directly over the wallpaper (clock, date, Home labels, page dots) stays white in both.
 */
final class LauncherPalette {
    static final String PREF_KEY = "dynamicColor";
    static final boolean DEFAULT_DYNAMIC = true;

    private static LauncherPalette current = classic();

    final boolean dynamic;
    // Home
    final int homeSearch, homeSearchHint, folder, editFill, editStroke, toolbar;
    // Shared surfaces and text
    final int field, text, hint, action, primary, onPrimary;
    // Drawer
    final int drawerLabel, handle, chipText, chipBackground, chipStroke, chipSelectedText;
    final int drawerSolid, glassTop, glassMiddle, glassBottom, glassStroke;
    // Settings
    final int settingsBackground, card, divider, settingsText, settingsMuted;

    private LauncherPalette(boolean dynamic, int[] v) {
        this.dynamic = dynamic;
        int i = 0;
        homeSearch = v[i++]; homeSearchHint = v[i++]; folder = v[i++]; editFill = v[i++]; editStroke = v[i++]; toolbar = v[i++];
        field = v[i++]; text = v[i++]; hint = v[i++]; action = v[i++]; primary = v[i++]; onPrimary = v[i++];
        drawerLabel = v[i++]; handle = v[i++]; chipText = v[i++]; chipBackground = v[i++]; chipStroke = v[i++]; chipSelectedText = v[i++];
        drawerSolid = v[i++]; glassTop = v[i++]; glassMiddle = v[i++]; glassBottom = v[i++]; glassStroke = v[i++];
        settingsBackground = v[i++]; card = v[i++]; divider = v[i++]; settingsText = v[i++]; settingsMuted = v[i++];
        signature = v.clone();
    }
    private final int[] signature;

    static LauncherPalette current() { return current; }

    static boolean dynamicEnabled(SharedPreferences prefs) { return prefs.getBoolean(PREF_KEY, DEFAULT_DYNAMIC); }

    /** Resolves the palette for the current preference and wallpaper, and makes it the shared current palette. */
    static LauncherPalette load(Context context, SharedPreferences prefs) {
        current = dynamicEnabled(prefs) ? fromSystem(context) : classic();
        return current;
    }

    /** Recomputes without changing {@link #current()}; used to detect wallpaper or preference changes. */
    static LauncherPalette peek(Context context, SharedPreferences prefs) {
        return dynamicEnabled(prefs) ? fromSystem(context) : classic();
    }

    boolean sameAs(LauncherPalette other) { return other != null && dynamic == other.dynamic && Arrays.equals(signature, other.signature); }

    /** Theme for activities: dynamic accent for system widgets (switches, seek bars, text cursors). */
    int theme() { return dynamic ? R.style.AppTheme_Dynamic : R.style.AppTheme; }

    /** Category accent nudged toward the wallpaper accent so groups stay distinct but match the palette. */
    int category(int base) { return dynamic ? ColorHarmony.harmonize(base, primary) : base; }

    static LauncherPalette classic() {
        return new LauncherPalette(false, new int[]{
                0xE62B2C35, 0xFFCFD0D8, 0xCC303747, 0x222C3648, 0x99D7E3FF, 0xF0232D40,
                0xFF2A303E, 0xFFFFFFFF, 0xFFB4BBCB, 0xFFD7E3FF, 0xFFBDD5FF, 0xFF172033,
                0xFFE6E9F2, 0x667C8599, 0xFFD1DAEB, 0xFF242E40, 0x443F4F68, 0xFF111923,
                0xFF171A22, 0xFF3D485E, 0xFF141B29, 0xFF212B3B, 0x44EAF2FF,
                0xFF10151F, 0xFF1C2433, 0xFF2D374A, 0xFFE8EDF8, 0xFFAAB4C9});
    }

    private static LauncherPalette fromSystem(Context c) {
        int n1_50 = c.getColor(android.R.color.system_neutral1_50);
        int n1_100 = c.getColor(android.R.color.system_neutral1_100);
        int n1_800 = c.getColor(android.R.color.system_neutral1_800);
        int n1_900 = c.getColor(android.R.color.system_neutral1_900);
        int n2_50 = c.getColor(android.R.color.system_neutral2_50);
        int n2_100 = c.getColor(android.R.color.system_neutral2_100);
        int n2_200 = c.getColor(android.R.color.system_neutral2_200);
        int n2_300 = c.getColor(android.R.color.system_neutral2_300);
        int n2_400 = c.getColor(android.R.color.system_neutral2_400);
        int n2_600 = c.getColor(android.R.color.system_neutral2_600);
        int n2_700 = c.getColor(android.R.color.system_neutral2_700);
        int n2_800 = c.getColor(android.R.color.system_neutral2_800);
        int n2_900 = c.getColor(android.R.color.system_neutral2_900);
        int a1_100 = c.getColor(android.R.color.system_accent1_100);
        int a1_200 = c.getColor(android.R.color.system_accent1_200);
        int a1_800 = c.getColor(android.R.color.system_accent1_800);
        int black = 0xFF000000;
        int surfaceDim = ColorHarmony.blend(n1_900, black, 0.35f);
        int surfaceContainer = ColorHarmony.blend(n1_900, n1_800, 0.45f);
        return new LauncherPalette(true, new int[]{
                ColorHarmony.withAlpha(n1_800, 0xE6), n2_200, ColorHarmony.withAlpha(n1_800, 0xCC),
                ColorHarmony.withAlpha(n2_800, 0x22), ColorHarmony.withAlpha(a1_100, 0x99), ColorHarmony.withAlpha(n1_800, 0xF0),
                n1_800, n1_50, n2_200, a1_100, a1_200, a1_800,
                n1_100, ColorHarmony.withAlpha(n2_400, 0x66), n2_100, n2_800, ColorHarmony.withAlpha(n2_600, 0x44), n1_900,
                n1_900, n2_700, n2_900, ColorHarmony.blend(n2_900, n2_800, 0.6f), ColorHarmony.withAlpha(n2_50, 0x44),
                surfaceDim, surfaceContainer, n2_800, n1_100, n2_300});
    }
}
