package dev.pixelair.launcher;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import java.util.HashMap;

/**
 * Tracks which packages have notifications worth a dot on their icon. Runs in the launcher process once the user
 * grants notification access; callbacks arrive on the main thread, where Home reads the counts.
 * Like Pixel, it skips group summaries, ongoing notifications, empty ones and channels that opted out of badges.
 */
public final class NotificationDots extends NotificationListenerService {
    static final String PREF_KEY = "notificationDots";
    static final boolean DEFAULT = true;

    private static final HashMap<String, Integer> counts = new HashMap<>();
    private static Runnable listener;

    static void setListener(Runnable next) { listener = next; }
    static void clearListener(Runnable current) { if (listener == current) listener = null; }
    static boolean has(String packageName) { Integer n = counts.get(packageName); return n != null && n > 0; }

    static boolean accessGranted(Context context) {
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        return manager != null && manager.isNotificationListenerAccessGranted(new ComponentName(context, NotificationDots.class));
    }

    @Override public void onListenerConnected() { rebuild(); }
    @Override public void onListenerDisconnected() { if (!counts.isEmpty()) { counts.clear(); changed(); } }
    @Override public void onNotificationPosted(StatusBarNotification sbn, RankingMap ranking) { rebuild(); }
    @Override public void onNotificationRemoved(StatusBarNotification sbn, RankingMap ranking) { rebuild(); }
    @Override public void onNotificationRankingUpdate(RankingMap ranking) { rebuild(); }

    private void rebuild() {
        StatusBarNotification[] active;
        RankingMap ranking;
        try { active = getActiveNotifications(); ranking = getCurrentRanking(); }
        catch (RuntimeException e) { return; } // not connected (yet)
        if (active == null) return;
        HashMap<String, Integer> next = new HashMap<>();
        Ranking rank = new Ranking();
        for (StatusBarNotification sbn : active) {
            // Home and the main drawer show this profile's apps; a work or private profile app must not light them up.
            if (!android.os.Process.myUserHandle().equals(sbn.getUser())) continue;
            Notification n = sbn.getNotification();
            if ((n.flags & (Notification.FLAG_GROUP_SUMMARY | Notification.FLAG_ONGOING_EVENT)) != 0) continue;
            if (ranking != null && ranking.getRanking(sbn.getKey(), rank) && !rank.canShowBadge()) continue;
            if (TextUtils.isEmpty(n.extras.getCharSequence(Notification.EXTRA_TITLE))
                    && TextUtils.isEmpty(n.extras.getCharSequence(Notification.EXTRA_TEXT))) continue;
            next.merge(sbn.getPackageName(), 1, Integer::sum);
        }
        if (next.equals(counts)) return;
        counts.clear(); counts.putAll(next); changed();
    }

    private static void changed() { if (listener != null) listener.run(); }

    /** The dot drawn over an icon's top-end corner, as an ImageView foreground. */
    static final class Dot extends Drawable {
        private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG), ring = new Paint(Paint.ANTI_ALIAS_FLAG);
        Dot(int color, float density) {
            fill.setColor(color);
            ring.setStyle(Paint.Style.STROKE); ring.setStrokeWidth(1.5f * density); ring.setColor(0x66000000);
        }
        @Override public void draw(Canvas canvas) {
            Rect b = getBounds();
            float size = Math.min(b.width(), b.height()), radius = size * .11f;
            float cx = b.exactCenterX() + size * .36f, cy = b.exactCenterY() - size * .36f;
            canvas.drawCircle(cx, cy, radius, fill);
            canvas.drawCircle(cx, cy, radius, ring);
        }
        @Override public void setAlpha(int alpha) { fill.setAlpha(alpha); ring.setAlpha(alpha); }
        @Override public void setColorFilter(ColorFilter filter) { fill.setColorFilter(filter); }
        @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
    }
}
