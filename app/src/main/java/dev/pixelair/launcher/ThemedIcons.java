package dev.pixelair.launcher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import java.util.HashMap;

/**
 * Material You "themed icons": an app's monochrome adaptive-icon layer, tinted with the launcher palette on a
 * palette-colored background and clipped to the system icon shape. Apps without a monochrome layer keep their
 * own icon, as on Pixel. Rendered once per app into a bitmap so list rows can rebind cheaply.
 */
final class ThemedIcons {
    static final String PREF_KEY = "themedIcons";
    static final boolean DEFAULT = false;

    private final HashMap<String, Bitmap> cache = new HashMap<>();
    private final int background, foreground, size;

    ThemedIcons(LauncherPalette palette, int sizePx) {
        background = palette.field; foreground = palette.action; size = Math.max(1, sizePx);
    }

    /** Themed icon for {@code id}, or null when the app ships no monochrome layer. */
    Drawable icon(Resources res, String id, Drawable original) {
        Bitmap bitmap;
        if (cache.containsKey(id)) bitmap = cache.get(id);
        else { bitmap = render(original); cache.put(id, bitmap); }
        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }

    void clear() { cache.clear(); }

    private Bitmap render(Drawable original) {
        if (!(original instanceof AdaptiveIconDrawable)) return null;
        Drawable mono = ((AdaptiveIconDrawable) original).getMonochrome();
        if (mono == null) return null;
        Drawable layer = mono.getConstantState() != null ? mono.getConstantState().newDrawable().mutate() : mono.mutate();
        layer.setTint(foreground);
        AdaptiveIconDrawable themed = new AdaptiveIconDrawable(new ColorDrawable(background), layer);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        themed.setBounds(0, 0, size, size);
        themed.draw(new Canvas(bitmap));
        return bitmap;
    }
}
