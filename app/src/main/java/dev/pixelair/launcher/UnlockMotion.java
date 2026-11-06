package dev.pixelair.launcher;

/**
 * Timing and shape of the unlock entrance, kept free of Android types so it can be unit tested.
 * Icons ripple outward from the screen center and settle on an underdamped spring with a small overshoot.
 */
public final class UnlockMotion {
    private UnlockMotion() {}

    /** Spring segment per icon; the visible arrival happens near 45% of it, the rest is a soft settle. */
    public static final long DURATION = 420;
    /** Extra delay for the icon farthest from the center; nearer icons start proportionally earlier. */
    public static final long STAGGER = 80;
    /** Fade-in per icon; shorter than the spring so icons are opaque before they overshoot. */
    public static final long FADE = 160;
    /** Starting scale and pull toward the center for Home grid icons. */
    public static final float START_SCALE = .82f, PULL = .18f;
    /** Dock icons travel this fraction of the grid motion so the bottom row stays calm. */
    public static final float DOCK_FACTOR = .5f;
    /** The clock starts this much larger and settles with the icons. */
    public static final float CLOCK_SCALE = 1.04f;

    static final double DAMPING = .6;
    /** Natural frequency chosen so the envelope decays to 0.5% at the end of {@link #DURATION}. */
    static final double OMEGA = 5.3 / (DAMPING * DURATION / 1000.0);

    /** Underdamped spring from 0 to 1 over a fraction of {@link #DURATION}; overshoots about 9%. */
    public static float spring(float fraction) {
        if (fraction <= 0f) return 0f;
        if (fraction >= 1f) return 1f;
        double s = fraction * DURATION / 1000.0, wd = OMEGA * Math.sqrt(1 - DAMPING * DAMPING);
        double envelope = Math.exp(-DAMPING * OMEGA * s);
        return (float) (1 - envelope * (Math.cos(wd * s) + DAMPING * OMEGA / wd * Math.sin(wd * s)));
    }

    /** Ease-out used for opacity, so icons never become more than fully opaque during the overshoot. */
    public static float fade(float fraction) {
        float t = Math.max(0f, Math.min(1f, fraction)), inverse = 1 - t;
        return 1 - inverse * inverse * inverse;
    }

    /** Start delay for an icon at {@code distance} from the center, given the farthest icon's distance. */
    public static long rippleDelay(float distance, float maxDistance) {
        if (!(maxDistance > 0f) || !(distance > 0f)) return 0;
        return Math.round(Math.min(1f, distance / maxDistance) * STAGGER);
    }

    /** Starting scale for an icon; dock icons move less. */
    public static float startScale(boolean dock) {
        return 1f - (1f - START_SCALE) * (dock ? DOCK_FACTOR : 1f);
    }

    /** Fraction of the offset from the center that an icon starts pulled in by; dock icons move less. */
    public static float pull(boolean dock) {
        return PULL * (dock ? DOCK_FACTOR : 1f);
    }

    /** Time from the first icon starting to the last icon settling. */
    public static long totalDuration() {
        return STAGGER + DURATION;
    }
}
