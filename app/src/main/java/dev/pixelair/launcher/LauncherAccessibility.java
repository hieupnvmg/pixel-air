package dev.pixelair.launcher;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

/**
 * Only used for Home gestures that Android reserves for accessibility services: locking the screen and, when the
 * status bar API is unavailable, pulling down notifications. It subscribes to no events and reads no window content.
 */
public final class LauncherAccessibility extends AccessibilityService {
    private static LauncherAccessibility running;

    @Override protected void onServiceConnected() { running = this; }
    @Override public boolean onUnbind(android.content.Intent intent) { if (running == this) running = null; return super.onUnbind(intent); }
    @Override public void onDestroy() { if (running == this) running = null; super.onDestroy(); }
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {}
    @Override public void onInterrupt() {}

    /** Runs a global action such as {@link #GLOBAL_ACTION_LOCK_SCREEN}; false when the service is off. */
    static boolean perform(int action) { return running != null && running.performGlobalAction(action); }

    static boolean enabled(Context context) {
        if (running != null) return true;
        String services = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (TextUtils.isEmpty(services)) return false;
        ComponentName self = new ComponentName(context, LauncherAccessibility.class);
        for (String entry : services.split(":")) {
            ComponentName component = ComponentName.unflattenFromString(entry);
            if (self.equals(component)) return true;
        }
        return false;
    }
}
