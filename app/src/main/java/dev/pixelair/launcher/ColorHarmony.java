package dev.pixelair.launcher;

/** Pure color math for the launcher palette, kept free of Android types so it can be unit tested. */
public final class ColorHarmony {
    private ColorHarmony() {}

    /** Largest hue rotation, in degrees, applied when nudging a color toward the accent hue. */
    public static final float MAX_SHIFT = 15f;

    /** Replaces the alpha channel of an ARGB color. */
    public static int withAlpha(int color, int alpha) {
        return (Math.max(0, Math.min(255, alpha)) << 24) | (color & 0x00FFFFFF);
    }

    /** Linear blend of two opaque colors; amount 0 returns {@code from}, 1 returns {@code to}. */
    public static int blend(int from, int to, float amount) {
        float t = Math.max(0f, Math.min(1f, amount));
        int a = Math.round(((from >>> 24) & 0xFF) * (1 - t) + ((to >>> 24) & 0xFF) * t);
        int r = Math.round(((from >> 16) & 0xFF) * (1 - t) + ((to >> 16) & 0xFF) * t);
        int g = Math.round(((from >> 8) & 0xFF) * (1 - t) + ((to >> 8) & 0xFF) * t);
        int b = Math.round((from & 0xFF) * (1 - t) + (to & 0xFF) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /** Hue in degrees [0, 360) of an RGB color; achromatic colors return -1. */
    public static float hue(int color) {
        float r = ((color >> 16) & 0xFF) / 255f, g = ((color >> 8) & 0xFF) / 255f, b = (color & 0xFF) / 255f;
        float max = Math.max(r, Math.max(g, b)), min = Math.min(r, Math.min(g, b)), delta = max - min;
        if (delta < 1e-4f) return -1;
        float h;
        if (max == r) h = ((g - b) / delta) % 6f;
        else if (max == g) h = (b - r) / delta + 2f;
        else h = (r - g) / delta + 4f;
        h *= 60f;
        return h < 0 ? h + 360f : h;
    }

    /** Signed shortest rotation from hue {@code from} to hue {@code to}, in degrees (-180, 180]. */
    public static float hueDelta(float from, float to) {
        float d = (to - from) % 360f;
        if (d > 180f) d -= 360f;
        if (d <= -180f) d += 360f;
        return d;
    }

    /**
     * Rotates {@code color} toward the hue of {@code source} by at most {@link #MAX_SHIFT} degrees,
     * keeping saturation, value and alpha. Category accents stay distinct but sit in the wallpaper palette.
     */
    public static int harmonize(int color, int source) {
        float from = hue(color), to = hue(source);
        if (from < 0 || to < 0) return color;
        float delta = hueDelta(from, to);
        float shift = Math.max(-MAX_SHIFT, Math.min(MAX_SHIFT, delta * 0.5f));
        return rotateHue(color, shift);
    }

    public static int rotateHue(int color, float degrees) {
        float r = ((color >> 16) & 0xFF) / 255f, g = ((color >> 8) & 0xFF) / 255f, b = (color & 0xFF) / 255f;
        float max = Math.max(r, Math.max(g, b)), min = Math.min(r, Math.min(g, b)), delta = max - min;
        if (delta < 1e-4f) return color;
        float h = hue(color) + degrees;
        h = ((h % 360f) + 360f) % 360f;
        float s = delta / max, v = max;
        float c = v * s, x = c * (1 - Math.abs((h / 60f) % 2f - 1)), m = v - c;
        float rr, gg, bb;
        if (h < 60) { rr = c; gg = x; bb = 0; }
        else if (h < 120) { rr = x; gg = c; bb = 0; }
        else if (h < 180) { rr = 0; gg = c; bb = x; }
        else if (h < 240) { rr = 0; gg = x; bb = c; }
        else if (h < 300) { rr = x; gg = 0; bb = c; }
        else { rr = c; gg = 0; bb = x; }
        int ri = Math.round((rr + m) * 255), gi = Math.round((gg + m) * 255), bi = Math.round((bb + m) * 255);
        return (color & 0xFF000000) | (ri << 16) | (gi << 8) | bi;
    }
}
