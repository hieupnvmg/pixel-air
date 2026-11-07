package dev.pixelair.launcher;

import java.util.*;

/**
 * Which launcher preferences travel in a layout backup. Everything in the "home" preferences does (pages, dock,
 * folders and their names, custom icons, categories, hidden apps, settings), except widget state: widget IDs belong
 * to this installation's widget host and are meaningless after a reinstall or on another phone.
 */
public final class LayoutBackup {
    static final String APP = "dev.pixelair.launcher";
    static final int FORMAT = 1;
    private static final Set<String> LOCAL = new HashSet<>(Arrays.asList(
            "widget", "widgetPage", "widgetX", "widgetY", "widgetWidth", "widgetHeightFraction", "widgetHeight",
            "widgetReserved", "pendingPlacement"));

    private LayoutBackup() {}

    public static boolean portable(String key) { return key != null && !key.isEmpty() && !LOCAL.contains(key); }

    /** The keys to write when restoring: every portable key from the backup, nothing local. */
    public static List<String> restorable(Collection<String> backupKeys) {
        ArrayList<String> result = new ArrayList<>();
        for (String key : backupKeys) if (portable(key)) result.add(key);
        Collections.sort(result);
        return result;
    }
}
