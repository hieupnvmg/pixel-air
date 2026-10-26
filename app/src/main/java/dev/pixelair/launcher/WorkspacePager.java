package dev.pixelair.launcher;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.os.SystemClock;

/** Full-width, scroll-based workspace. Pages are prepared before a gesture, never in MOVE. */
public final class WorkspacePager extends ViewGroup {
    public interface Adapter {
        int pageCount();
        View createPage(int page);
        void onPageSelected(int page);
    }

    private final SparseArray<View> pages = new SparseArray<>();
    private SpringMotion spring;
    private long settleStarted;
    private double springFrequency = 24;
    private Adapter adapter;
    private int selected, settlingTo = -1;
    private float touchAnchor;
    private boolean pendingPosition, moving;

    public WorkspacePager(Context context) {
        super(context);
        setClipChildren(true);
        setClipToPadding(true);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setWillNotDraw(true);
    }

    public void setAdapter(Adapter value) { adapter = value; }
    public void setMotionStyle(int style) { springFrequency = style == 0 ? 19 : style == 2 ? 30 : 24; }

    /** Refresh only after an edit/resume, not during ordinary page changes. */
    public void refresh(int page) {
        stopAtCurrentPosition();
        moving = false;
        removeAllViews();
        pages.clear();
        selected = clampPage(page);
        ensureNeighbors();
        pendingPosition = true;
        requestLayout();
    }

    private int clampPage(int page) {
        return Math.max(0, Math.min(adapter.pageCount() - 1, page));
    }

    private void ensureNeighbors() {
        int first = Math.max(0, selected - 2);
        int last = Math.min(adapter.pageCount() - 1, selected + 2);
        for (int i = pages.size() - 1; i >= 0; i--) {
            int page = pages.keyAt(i);
            if (page < first || page > last) {
                removeView(pages.valueAt(i));
                pages.removeAt(i);
            }
        }
        for (int page = first; page <= last; page++) {
            if (pages.get(page) == null) {
                View child = adapter.createPage(page);
                pages.put(page, child);
                addView(child, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        }
        updateAccessibility();
    }

    private void updateAccessibility() {
        for (int i = 0; i < pages.size(); i++) {
            pages.valueAt(i).setImportantForAccessibility(pages.keyAt(i) == selected
                    ? IMPORTANT_FOR_ACCESSIBILITY_AUTO : IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
        }
    }

    @Override protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec), height = MeasureSpec.getSize(heightSpec);
        setMeasuredDimension(width, height);
        int w = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int h = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) getChildAt(i).measure(w, h);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            stopAtCurrentPosition();
            pendingPosition = true;
        }
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        for (int i = 0; i < pages.size(); i++) {
            int left = pages.keyAt(i) * width;
            pages.valueAt(i).layout(left, 0, left + width, getHeight());
        }
        if (pendingPosition) {
            scrollTo(selected * width, 0);
            pendingPosition = false;
        }
    }

    /** Stop at the displayed position; do not jump to the old animation's destination. */
    public boolean prepareTouch() {
        boolean wasMoving = moving;
        stopAtCurrentPosition();
        touchAnchor = getScrollX();
        if (pages.size() > 0 && getWidth() > 0) touchAnchor = SpringMotion.inverseRubberBand(touchAnchor,
                pages.keyAt(0) * getWidth(), pages.keyAt(pages.size() - 1) * getWidth(),
                48 * getResources().getDisplayMetrics().density);
        return wasMoving;
    }

    private void stopAtCurrentPosition() {
        // Freeze the last displayed frame, not an extrapolated future position.
        spring = null;
        settlingTo = -1;
    }

    public void startDrag() {
        moving = true;
    }

    public void dragTo(float fingerTravel) {
        int width = getWidth();
        if (width == 0 || pages.size() == 0) return;
        int first = pages.keyAt(0), last = pages.keyAt(pages.size() - 1);
        float position = SpringMotion.rubberBand(touchAnchor - fingerTravel, first * width, last * width, 48 * getResources().getDisplayMetrics().density);
        scrollTo(Math.round(position), 0);
    }

    public void release(float velocity, float travel, boolean cancelled) {
        int width = getWidth();
        if (width == 0 || pages.size() == 0) return;
        float density = getResources().getDisplayMetrics().density;
        int target = cancelled ? selected : SwipePhysics.target(getScrollX(), travel, velocity,
                pages.keyAt(0), pages.keyAt(pages.size() - 1), width, 400 * density, 24 * density);
        settleTo(target, velocity * SpringMotion.velocityFactor(getScrollX(), pages.keyAt(0) * width,
                pages.keyAt(pages.size() - 1) * width, 48 * density));
    }

    public void selectPage(int page) {
        page = clampPage(page);
        if (Math.abs(page - selected) > 1) {
            refresh(page);
            adapter.onPageSelected(page);
        } else {
            stopAtCurrentPosition();
            settleTo(page, 0);
        }
    }

    private void settleTo(int page, float velocity) {
        int distance = page * getWidth() - getScrollX();
        settlingTo = page;
        moving = true;
        if (Math.abs(distance) < 1) {
            finishSettle();
            return;
        }
        if (!android.animation.ValueAnimator.areAnimatorsEnabled()) { finishSettle(); return; }
        float maxVelocity = 7f * getWidth();
        float startVelocity = Math.max(-maxVelocity, Math.min(maxVelocity, -velocity));
        spring = new SpringMotion(getScrollX(), page * getWidth(), startVelocity, springFrequency);
        settleStarted = SystemClock.uptimeMillis();
        postInvalidateOnAnimation();
    }

    @Override public void computeScroll() {
        if (spring == null) return;
        double elapsed = (SystemClock.uptimeMillis() - settleStarted) / 1000.0;
        double position = spring.position(elapsed);
        scrollTo((int)Math.round(position), 0);
        double density = getResources().getDisplayMetrics().density;
        boolean atRest = Math.abs(position - settlingTo * getWidth()) < .4 * density
                && Math.abs(spring.velocity(elapsed)) < 5 * density;
        if (elapsed > 1.1 || elapsed > .06 && atRest) finishSettle();
        else postInvalidateOnAnimation();
    }

    private void finishSettle() {
        if (settlingTo < 0) return;
        spring = null;
        selected = settlingTo;
        settlingTo = -1;
        scrollTo(selected * getWidth(), 0);
        moving = false;
        // Queue neighbor preparation after the last animation frame.
        post(() -> {
            if (!moving && settlingTo < 0) {
                ensureNeighbors();
                adapter.onPageSelected(selected);
            }
        });
    }

    @Override protected void onDetachedFromWindow() {
        spring = null;
        moving = false;
        settlingTo = -1;
        super.onDetachedFromWindow();
    }
}
