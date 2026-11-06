package dev.pixelair.launcher;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import java.util.HashMap;

/**
 * Background of an expanded drawer section. Styled, it is a soft rounded panel with a tiled category motif and one
 * large faint category symbol; plain, it is the original barely tinted horizontal wash.
 * The vertical drawer shows a section as a heading plus several list rows, so each view binds the slice of the
 * section it covers and everything is drawn in section coordinates: the panel, motif and symbol continue across rows.
 */
final class DrawerBackdrop extends Drawable {
    static final String PREF_KEY = "drawerBackdrops";
    static final boolean DEFAULT = true;
    /** Heading and app-row heights of the vertical drawer, in dp. */
    static final int HEADING_DP = 64, ROW_DP = 100;

    private static final HashMap<String, Bitmap> TILES = new HashMap<>();
    private final float density;
    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG), motif = new Paint(Paint.ANTI_ALIAS_FLAG),
            symbol = new Paint(Paint.ANTI_ALIAS_FLAG), wash = new Paint();
    private final Path panelPath = new Path(), symbolPath = new Path();
    private final RectF panel = new RectF();
    private final Matrix symbolMatrix = new Matrix();
    private String key;
    private int color;
    private boolean styled;
    private float sliceTop, sectionHeight;
    private int shapedWidth = -1;
    private float shapedHeight = -1;

    DrawerBackdrop(float density) { this.density = density; }

    /** Height in px of a vertical-drawer section with {@code rows} app rows. */
    static float sectionHeight(float density, int rows) { return (HEADING_DP + rows * ROW_DP) * density; }
    /** Top in px, within its section, of app row {@code part}; the heading is at 0. */
    static float rowTop(float density, int part) { return (HEADING_DP + part * ROW_DP) * density; }

    /**
     * @param sliceTop where this view's top sits inside the section, in px
     * @param sectionHeight full section height in px, or 0 to treat this view as the whole section
     */
    void bind(String key, float sliceTop, float sectionHeight, boolean styled) {
        int next = DrawerSectionView.accent(key);
        boolean changed = next != color || styled != this.styled || !key.equals(this.key)
                || sliceTop != this.sliceTop || sectionHeight != this.sectionHeight;
        this.key = key; this.color = next; this.styled = styled; this.sliceTop = sliceTop; this.sectionHeight = sectionHeight;
        if (!changed) return;
        shapedWidth = -1; invalidateSelf();
    }

    private float dp(float value) { return value * density; }

    private void shape(int width, float height) {
        if (width == shapedWidth && height == shapedHeight) return;
        shapedWidth = width; shapedHeight = height;
        if (!styled) {
            int tint = ColorHarmony.withAlpha(color, 0x06);
            wash.setShader(new LinearGradient(0, 0, Math.max(1, width), 0, new int[]{tint, tint & 0x00FFFFFF}, null, Shader.TileMode.CLAMP));
            return;
        }
        panel.set(dp(6), dp(8), width - dp(6), height - dp(4));
        float radius = dp(24);
        panelPath.rewind(); panelPath.addRoundRect(panel, radius, radius, Path.Direction.CW);
        fill.setShader(new LinearGradient(0, panel.top, 0, Math.max(panel.top + 1, panel.bottom),
                new int[]{ColorHarmony.withAlpha(color, 0x24), ColorHarmony.withAlpha(color, 0x0C)}, null, Shader.TileMode.CLAMP));
        motif.setShader(new BitmapShader(tile(key, color, density), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        // Large symbol tucked into the top-right corner, partly cut by the panel edge.
        float size = dp(150);
        symbolMatrix.setScale(size / 24f, size / 24f);
        symbolMatrix.postTranslate(panel.right - size * .62f, panel.top - size * .14f);
        symbol.setColor(ColorHarmony.withAlpha(color, 0x1A));
        symbol.setStrokeCap(Paint.Cap.ROUND); symbol.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override public void draw(Canvas canvas) {
        if (key == null) return;
        Rect bounds = getBounds();
        float height = sectionHeight > 0 ? sectionHeight : bounds.height();
        shape(bounds.width(), height);
        canvas.save();
        canvas.clipRect(bounds);
        canvas.translate(bounds.left, bounds.top - sliceTop);
        if (!styled) {
            canvas.drawRect(0, sliceTop, bounds.width(), sliceTop + bounds.height(), wash);
            canvas.restore(); return;
        }
        canvas.drawPath(panelPath, fill);
        canvas.clipPath(panelPath);
        canvas.drawRect(panel, motif);
        canvas.concat(symbolMatrix);
        symbol.setStrokeWidth(.75f);
        DrawerSectionView.drawSymbol(canvas, symbol, symbolPath, key);
        canvas.restore();
    }

    /** Seamless motif tile per category, cached per color and density. */
    private static Bitmap tile(String key, int color, float density) {
        String id = key + '/' + Integer.toHexString(color) + '/' + density;
        Bitmap cached = TILES.get(id);
        if (cached != null) return cached;
        if (TILES.size() > 32) TILES.clear();
        int size = 56;
        Bitmap bitmap = Bitmap.createBitmap(Math.round(size * density), Math.round(size * density), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.scale(bitmap.getWidth() / (float) size, bitmap.getHeight() / (float) size);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(ColorHarmony.withAlpha(color, 0x24));
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(1.2f);
        p.setStrokeCap(Paint.Cap.ROUND); p.setStrokeJoin(Paint.Join.ROUND);
        Path path = new Path();
        switch (key) {
            case "games": // scattered controller-button shapes
                c.drawLine(10, 16, 20, 16, p); c.drawLine(15, 11, 15, 21, p);
                c.drawCircle(42, 14, 4, p);
                path.moveTo(14, 36); path.lineTo(19, 45); path.lineTo(9, 45); path.close(); c.drawPath(path, p);
                c.drawRect(37, 37, 45, 45, p); break;
            case "tools": { // blueprint grid
                Paint faint = new Paint(p); faint.setColor(ColorHarmony.withAlpha(color, 0x18)); faint.setStrokeWidth(.8f);
                for (int n = 1; n < 4; n++) { c.drawLine(n * 14, 0, n * 14, size, faint); c.drawLine(0, n * 14, size, n * 14, faint); }
                c.drawLine(0, 0, size, 0, p); c.drawLine(0, 0, 0, size, p);
                c.drawCircle(28, 28, 3, p); break;
            }
            case "media": { // equalizer bars
                int[] heights = {8, 20, 12, 28, 16, 6, 22};
                p.setStrokeWidth(2.2f);
                for (int n = 0; n < heights.length; n++) c.drawLine(4 + n * 8, 28 - heights[n] / 2f, 4 + n * 8, 28 + heights[n] / 2f, p);
                break;
            }
            case "productivity": // checklist
                c.drawRoundRect(6, 8, 15, 17, 2, 2, p);
                path.moveTo(8.5f, 12.5f); path.lineTo(10.5f, 14.5f); path.lineTo(13.5f, 10.5f); c.drawPath(path, p);
                c.drawLine(21, 12.5f, 48, 12.5f, p);
                c.drawRoundRect(6, 36, 15, 45, 2, 2, p);
                c.drawLine(21, 40.5f, 40, 40.5f, p); break;
            case "social": // chat bubbles
                c.drawCircle(17, 18, 9, p); c.drawCircle(42, 40, 5, p); c.drawCircle(46, 16, 2.2f, p); c.drawCircle(14, 45, 2.2f, p); break;
            case "maps": // topographic contours
                for (int k = 0; k < 5; k++) {
                    path.rewind();
                    for (int x = 0; x <= size; x += 2) {
                        float y = 5 + k * 11.2f + 3.5f * (float) Math.sin(2 * Math.PI * x / size + k * .9);
                        if (x == 0) path.moveTo(x, y); else path.lineTo(x, y);
                    }
                    c.drawPath(path, p);
                }
                break;
            case "news": // newspaper columns
                p.setStrokeWidth(2.4f); c.drawLine(4, 6, 24, 6, p); p.setStrokeWidth(1.2f);
                for (int n = 0; n < 5; n++) c.drawLine(4, 14 + n * 8, n == 4 ? 16 : 24, 14 + n * 8, p);
                for (int n = 0; n < 6; n++) c.drawLine(32, 6 + n * 8, n == 5 ? 44 : 52, 6 + n * 8, p);
                break;
            case "shopping": // tag lattice
                c.drawLine(-2, -2, size + 2, size + 2, p); c.drawLine(size + 2, -2, -2, size + 2, p);
                c.drawCircle(28, 10, 1.6f, p); c.drawCircle(28, 46, 1.6f, p); break;
            case "frequent": // sparkles
                sparkle(c, p, path, 15, 17, 7); sparkle(c, p, path, 41, 40, 4.5f);
                p.setStyle(Paint.Style.FILL); c.drawCircle(44, 13, 1.4f, p); c.drawCircle(13, 44, 1.2f, p); break;
            default: // quiet dot grid
                p.setStyle(Paint.Style.FILL);
                for (int y = 0; y < 4; y++) for (int x = 0; x < 4; x++) c.drawCircle(7 + x * 14, 7 + y * 14, 1.1f, p);
        }
        TILES.put(id, bitmap);
        return bitmap;
    }

    private static void sparkle(Canvas c, Paint p, Path path, float cx, float cy, float r) {
        path.rewind();
        path.moveTo(cx, cy - r);
        path.quadTo(cx, cy, cx + r, cy); path.quadTo(cx, cy, cx, cy + r);
        path.quadTo(cx, cy, cx - r, cy); path.quadTo(cx, cy, cx, cy - r);
        c.drawPath(path, p);
    }

    @Override public void setAlpha(int alpha) { fill.setAlpha(alpha); motif.setAlpha(alpha); wash.setAlpha(alpha); }
    @Override public void setColorFilter(ColorFilter filter) { fill.setColorFilter(filter); motif.setColorFilter(filter); symbol.setColorFilter(filter); wash.setColorFilter(filter); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
}
