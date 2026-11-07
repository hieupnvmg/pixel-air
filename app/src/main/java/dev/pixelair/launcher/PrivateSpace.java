package dev.pixelair.launcher;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.LauncherUserInfo;
import android.os.Build;
import android.os.UserHandle;
import android.os.UserManager;

/**
 * Android 15 private space. Android only reveals the private profile to the default Home app holding
 * ACCESS_HIDDEN_PROFILES; locking and unlocking go through quiet mode, and the system asks for the
 * screen lock (or the space's own lock) when unlocking.
 */
final class PrivateSpace {
    static final String PREF_KEY = "showPrivateSpace";
    private PrivateSpace() {}

    static UserHandle find(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) return null;
        LauncherApps apps = context.getSystemService(LauncherApps.class);
        if (apps == null) return null;
        try {
            for (UserHandle user : apps.getProfiles()) {
                LauncherUserInfo info = apps.getLauncherUserInfo(user);
                if (info != null && UserManager.USER_TYPE_PROFILE_PRIVATE.equals(info.getUserType())) return user;
            }
        } catch (RuntimeException ignored) { /* Profile removed while querying. */ }
        return null;
    }

    static boolean locked(Context context, UserHandle user) {
        try { return context.getSystemService(UserManager.class).isQuietModeEnabled(user); }
        catch (RuntimeException e) { return true; }
    }

    /** Asks Android to lock or unlock; unlocking may first show the system's authentication screen. */
    static boolean setLocked(Context context, UserHandle user, boolean lock) {
        try { return context.getSystemService(UserManager.class).requestQuietModeEnabled(lock, user); }
        catch (RuntimeException e) { return false; }
    }
}
