package dev.pixelair.launcher;

import android.graphics.*;
import android.graphics.drawable.Drawable;

/** Translucent tint with a quiet glass highlight; real backdrop blur is provided by WindowManager. */
public final class DrawerGlassDrawable extends Drawable {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final int opacity;
    private final float radius;
    private final boolean glass;
    private Shader tint;
    public DrawerGlassDrawable(float density, int opacity, boolean glass) {
        this.opacity = Math.max(35, Math.min(100, opacity)); this.glass = glass; radius = 28 * density;
    }
    @Override protected void onBoundsChange(Rect bounds) {
        rect.set(bounds);
        int alpha = Math.round(opacity * 2.55f);
        tint = new LinearGradient(0, rect.top, rect.width(), rect.bottom,
                glass ? new int[]{Color.argb(Math.min(255, alpha + 12), 61, 72, 94), Color.argb(alpha, 20, 27, 41), Color.argb(alpha, 33, 43, 59)}
                      : new int[]{Color.argb(alpha, 23, 26, 34), Color.argb(alpha, 23, 26, 34)}, null, Shader.TileMode.CLAMP);
    }
    @Override public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL); paint.setShader(tint);
        canvas.drawRoundRect(rect, radius, radius, paint); paint.setShader(null);
        if (glass) { paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(1.5f); paint.setColor(0x44EAF2FF); canvas.drawRoundRect(new RectF(rect.left+1,rect.top+1,rect.right-1,rect.bottom-1),radius,radius,paint); }
    }
    @Override public void setAlpha(int alpha) {} // Opacity is configured explicitly; view alpha handles transitions.
    @Override public void setColorFilter(ColorFilter filter) { paint.setColorFilter(filter); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
}
