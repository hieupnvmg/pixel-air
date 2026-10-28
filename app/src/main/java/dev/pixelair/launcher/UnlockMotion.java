package dev.pixelair.launcher;

/** Deterministic geometry for the optional iPhone-inspired unlock entrance. */
public final class UnlockMotion {
    public static final int DURATION_MS=600;
    private static float clamp(float value){return Math.max(0,Math.min(1,value));}
    private static float settle(float t){float remaining=1-clamp(t);return 1-remaining*remaining*remaining*remaining;}
    public static float gridScale(float t){return 1+1.1f*(1-settle(t));}
    public static float gridAlpha(float t){return clamp(t/.12f);}
    private static float dockProgress(float t){return clamp((t-.04f)/.86f);}
    public static float dockScale(float t){return .62f+.38f*settle(dockProgress(t));}
    public static float dockSpread(float t){return .78f+.22f*settle(dockProgress(t));}
    public static float dockRise(float t){return 48*(1-settle(dockProgress(t)));}
    public static float dockAlpha(float t){return clamp(dockProgress(t)/.14f);}
}
