package dev.pixelair.launcher;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

/** Native, expandable section heading with a small category glyph and a quiet motif. */
final class DrawerSectionView extends LinearLayout {
    private final Glyph glyph, motif;
    private final TextView title, count;
    private final ExpandIndicator chevron;
    private String boundKey;
    private boolean boundExpanded;
    private final Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int accent;
    private int dp(float value) { return Math.round(value * getResources().getDisplayMetrics().density); }
    static int accent(String key) {
        switch (key) {
            case "games": return 0xFFD8B4FF;
            case "tools": return 0xFF91E2CB;
            case "media": return 0xFFF3A5A1;
            case "productivity": return 0xFFA6CFFF;
            case "social": return 0xFFF2BD98;
            case "maps": return 0xFF8BDADD;
            case "news": return 0xFFC5B5ED;
            case "shopping": return 0xFFE8CD94;
            case "other": return 0xFFBDC6D5;
            default: return 0xFFB4D7FF;
        }
    }
    DrawerSectionView(Context context) {
        super(context);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(dp(8), dp(16), dp(8), dp(6));
        setWillNotDraw(false);
        setAccessibilityHeading(true);
        setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
        glyph = new Glyph(context, false);
        addView(glyph, new LayoutParams(dp(23), dp(26)));
        title = new TextView(context);
        title.setTextSize(16); title.setTypeface(null, Typeface.BOLD);
        title.setSingleLine(); title.setEllipsize(TextUtils.TruncateAt.END);
        LayoutParams label = new LayoutParams(0, -2, 1); label.leftMargin = dp(10);
        addView(title, label);
        count = new TextView(context); count.setTextSize(12); count.setPadding(dp(12), 0, dp(14), 0);
        addView(count, new LayoutParams(-2, -2));
        motif = new Glyph(context, true);
        addView(motif, new LayoutParams(dp(28), dp(24)));
        chevron = new ExpandIndicator(context);
        addView(chevron, new LayoutParams(dp(40), dp(40)));
        setBackground(new android.graphics.drawable.RippleDrawable(android.content.res.ColorStateList.valueOf(0x18FFFFFF), null, new android.graphics.drawable.ColorDrawable(Color.WHITE)));
        for (int n = 0; n < getChildCount(); n++) getChildAt(n).setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
    }
    void bind(String key, String name, int total, boolean expandable, boolean expanded) {
        accent = accent(key); title.setText(name); title.setTextColor(accent);
        count.setText(String.valueOf(total)); count.setTextColor(accent);
        glyph.bind(key, accent); motif.bind(key, accent);
        setContentDescription(getResources().getString(R.string.drawer_section_description, name, total));
        setClickable(expandable); setFocusable(expandable);
        setStateDescription(expandable ? getResources().getString(expanded ? R.string.drawer_expanded : R.string.drawer_collapsed) : null);
        chevron.setVisibility(expandable ? VISIBLE : GONE); chevron.bind(accent);
        motif.setVisibility(expandable ? GONE : VISIBLE);
        float rotation = expanded ? 180 : 0;
        chevron.animate().cancel();
        if (expandable && key.equals(boundKey) && expanded != boundExpanded && android.animation.ValueAnimator.areAnimatorsEnabled())
            chevron.animate().rotation(rotation).setDuration(220).setInterpolator(new android.view.animation.PathInterpolator(.2f,0f,0f,1f)).start();
        else chevron.setRotation(rotation);
        boundKey=key; boundExpanded=expanded;
        invalidate();
    }
    @Override protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        float textWidth = Math.min(title.getWidth(), title.getPaint().measureText(title.getText().toString()));
        float start = title.getLeft() + textWidth + dp(12), end = count.getLeft() - dp(10);
        if (end > start) {
            line.setColor((accent & 0x00FFFFFF) | 0x55000000); line.setStrokeWidth(dp(1));
            float y = glyph.getTop() + glyph.getHeight() / 2f;
            canvas.drawLine(start, y, end, y, line);
        }
    }
    /** Font-independent, centered disclosure glyph; its circle replaces the decorative motif. */
    private static final class ExpandIndicator extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();
        private int color;
        ExpandIndicator(Context context) { super(context); }
        void bind(int value) { color=value; invalidate(); }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float density=getResources().getDisplayMetrics().density;
            float cx=getWidth()/2f, cy=getHeight()/2f;
            paint.setStyle(Paint.Style.FILL);paint.setColor((color & 0x00FFFFFF) | 0x14000000);
            canvas.drawCircle(cx,cy,14*density,paint);
            paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(1.7f*density);
            paint.setStrokeCap(Paint.Cap.ROUND);paint.setStrokeJoin(Paint.Join.ROUND);paint.setColor(color);
            path.rewind();path.moveTo(cx-4*density,cy-2*density);path.lineTo(cx,cy+2*density);path.lineTo(cx+4*density,cy-2*density);
            canvas.drawPath(path,paint);
        }
    }
    /** A barely tinted horizontal wash: no enclosing cards or rounded category tiles. */
    static final class Wash extends Drawable {
        private final Paint paint = new Paint();
        private int color;
        void bind(String key) {
            int next = (accent(key) & 0x00FFFFFF) | 0x06000000;
            if (next == color) return;
            color = next; updateShader(); invalidateSelf();
        }
        private void updateShader() {
            Rect r = getBounds();
            paint.setShader(new LinearGradient(r.left, 0, Math.max(r.left + 1, r.right), 0,
                    new int[]{color, color & 0x00FFFFFF}, null, Shader.TileMode.CLAMP));
        }
        @Override protected void onBoundsChange(Rect bounds) { updateShader(); }
        @Override public void draw(Canvas canvas) { canvas.drawRect(getBounds(), paint); }
        @Override public void setAlpha(int value) { paint.setAlpha(value); }
        @Override public void setColorFilter(ColorFilter filter) { paint.setColorFilter(filter); }
        @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
    }
    private static final class Glyph extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();
        private final boolean decorative;
        private String key = "all";
        private int color;
        Glyph(Context context, boolean decorative) { super(context); this.decorative = decorative; }
        void bind(String key, int color) { this.key = key; this.color = color; invalidate(); }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas); canvas.save();
            float scale = Math.min(getWidth(), getHeight()) / 24f;
            canvas.translate((getWidth() - scale * 24) / 2, (getHeight() - scale * 24) / 2);
            canvas.scale(scale, scale);
            paint.setColor(decorative ? (color & 0x00FFFFFF) | 0x88000000 : color);
            paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(1.8f);
            paint.setStrokeCap(Paint.Cap.ROUND); paint.setStrokeJoin(Paint.Join.ROUND);
            path.reset();
            if (decorative) {
                if (key.equals("media")) {
                    int[] heights = {6, 13, 20, 12, 5};
                    for (int n = 0; n < heights.length; n++) canvas.drawLine(3 + n * 4.5f, 12 - heights[n] / 2f, 3 + n * 4.5f, 12 + heights[n] / 2f, paint);
                } else if (key.equals("productivity") || key.equals("news")) {
                    for (int n = 0; n < 4; n++) canvas.drawLine(3, 5 + n * 4, 21 - n * 4, 5 + n * 4, paint);
                } else {
                    paint.setStyle(Paint.Style.FILL);
                    for (int n = 0; n < 6; n++) {
                        float x = 5 + (n % 2) * 9, y = 3 + (n / 2) * 8;
                        if (key.equals("games")) canvas.drawRect(x, y, x + 5, y + 5, paint);
                        else canvas.drawCircle(x + 2, y + 2, 1.6f, paint);
                    }
                }
            } else switch (key) {
                case "games":
                    canvas.drawRoundRect(2, 6, 22, 18, 4, 4, paint);
                    canvas.drawLine(5, 12, 11, 12, paint); canvas.drawLine(8, 9, 8, 15, paint);
                    canvas.drawCircle(16, 10, .7f, paint); canvas.drawCircle(19, 14, .7f, paint); break;
                case "tools":
                    for (int n = 0; n < 3; n++) { float x = 4 + n * 8; canvas.drawLine(x, 3, x, 21, paint); }
                    for (int n = 0; n < 3; n++) { float x = 4 + n * 8, y = n == 1 ? 16 : 8; paint.setStyle(Paint.Style.FILL); canvas.drawCircle(x, y, 3.3f, paint); paint.setStyle(Paint.Style.STROKE); } break;
                case "media":
                    path.moveTo(6, 3); path.lineTo(21, 12); path.lineTo(6, 21); path.close(); canvas.drawPath(path, paint); break;
                case "productivity":
                    canvas.drawRoundRect(3, 5, 21, 21, 2, 2, paint); canvas.drawRoundRect(8, 1, 16, 6, 1, 1, paint); break;
                case "social":
                    canvas.drawRoundRect(2, 3, 22, 18, 5, 5, paint); path.moveTo(6, 18); path.lineTo(5, 22); path.lineTo(12, 18); canvas.drawPath(path, paint); break;
                case "maps":
                    path.moveTo(12, 2); path.lineTo(21, 22); path.lineTo(12, 17); path.lineTo(3, 22); path.close(); canvas.drawPath(path, paint); break;
                case "news":
                    canvas.drawRoundRect(3, 2, 21, 22, 2, 2, paint); canvas.drawLine(7, 7, 17, 7, paint); canvas.drawLine(7, 12, 17, 12, paint); canvas.drawLine(7, 17, 13, 17, paint); break;
                case "shopping":
                    canvas.drawRoundRect(3, 7, 21, 22, 2, 2, paint); canvas.drawArc(7, 1, 17, 13, 180, 180, false, paint); break;
                case "frequent":
                    for (int n = 0; n < 10; n++) { double angle = -Math.PI / 2 + n * Math.PI / 5; float radius = n % 2 == 0 ? 11 : 5; float x = 12 + (float)Math.cos(angle) * radius, y = 12 + (float)Math.sin(angle) * radius; if (n == 0) path.moveTo(x, y); else path.lineTo(x, y); }
                    path.close(); paint.setStyle(Paint.Style.FILL); canvas.drawPath(path, paint); break;
                default:
                    for (int n = 0; n < 4; n++) canvas.drawRoundRect(3 + (n % 2) * 11, 3 + (n / 2) * 11, 10 + (n % 2) * 11, 10 + (n / 2) * 11, 1, 1, paint);
            }
            canvas.restore();
        }
    }
}
