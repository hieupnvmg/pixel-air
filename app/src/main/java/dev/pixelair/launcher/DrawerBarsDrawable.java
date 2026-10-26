package dev.pixelair.launcher;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.View;

/** Tint only the status/navigation insets; never double-tint the drawer itself. */
public final class DrawerBarsDrawable extends Drawable {
    private final View root;
    private final Paint paint = new Paint();
    public DrawerBarsDrawable(View root) { this.root = root; paint.setColor(Color.TRANSPARENT); }
    public void setAmount(int opacity, float progress) {
        paint.setColor(Color.argb(Math.round(opacity * 2.55f * progress), 23, 26, 34)); invalidateSelf();
    }
    @Override public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, root.getWidth(), root.getPaddingTop(), paint);
        canvas.drawRect(0, root.getHeight()-root.getPaddingBottom(), root.getWidth(), root.getHeight(), paint);
    }
    @Override public void setAlpha(int alpha) {} // Driven by drawer progress.
    @Override public void setColorFilter(ColorFilter filter) { paint.setColorFilter(filter); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
}
