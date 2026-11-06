package dev.pixelair.launcher;

import android.graphics.*;
import android.graphics.drawable.Drawable;

/**
 * Background of a Home slot. While editing it is an inset rounded tile, so neighbouring slots read as separate
 * cells; during a drag it marks the drop target, and a ring around the icon signals that dropping creates a folder.
 * It reports no padding, so swapping states never shifts the slot's content.
 */
final class HomeCellFrame extends Drawable {
    static final int IDLE = 0, TARGET = 1, FOLDER = 2;
    private final float density;
    private final boolean framed;
    private final int fill, stroke, accent;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF tile = new RectF(), ring = new RectF();
    private int state = IDLE;

    HomeCellFrame(float density, boolean framed, int fill, int stroke, int accent) {
        this.density = density; this.framed = framed; this.fill = fill; this.stroke = stroke; this.accent = accent;
    }

    int state() { return state; }

    void setState(int next) {
        if (next == state) return;
        state = next; invalidateSelf();
    }

    /** Arms the folder ring around the icon, given in this drawable's coordinates. */
    void showFolderRing(float left, float top, float right, float bottom) {
        float grow = 7 * density;
        ring.set(left - grow, top - grow, right + grow, bottom + grow);
        setState(FOLDER);
    }

    @Override public void draw(Canvas canvas) {
        if (!framed && state == IDLE) return;
        Rect b = getBounds();
        float inset = 4 * density, radius = 20 * density;
        tile.set(b.left + inset, b.top + inset, b.right - inset, b.bottom - inset);
        paint.setStyle(Paint.Style.FILL);
        if (framed) { paint.setColor(fill); canvas.drawRoundRect(tile, radius, radius, paint); }
        if (state != IDLE) { paint.setColor(ColorHarmony.withAlpha(accent, state == FOLDER ? 0x18 : 0x2A)); canvas.drawRoundRect(tile, radius, radius, paint); }
        paint.setStyle(Paint.Style.STROKE);
        if (state == IDLE) { paint.setStrokeWidth(density); paint.setColor(stroke); }
        else { paint.setStrokeWidth(2 * density); paint.setColor(ColorHarmony.withAlpha(accent, state == FOLDER ? 0x66 : 0xD0)); }
        canvas.drawRoundRect(tile, radius, radius, paint);
        if (state == FOLDER) {
            paint.setStyle(Paint.Style.FILL); paint.setColor(ColorHarmony.withAlpha(accent, 0x48));
            canvas.drawOval(ring, paint);
            paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(2.5f * density); paint.setColor(accent);
            canvas.drawOval(ring, paint);
        }
    }

    @Override public void setAlpha(int alpha) { paint.setAlpha(alpha); }
    @Override public void setColorFilter(ColorFilter filter) { paint.setColorFilter(filter); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
}
