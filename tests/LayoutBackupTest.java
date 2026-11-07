import dev.pixelair.launcher.LayoutBackup;
import java.util.*;

public final class LayoutBackupTest {
    public static void main(String[] args) {
        assertTrue(LayoutBackup.portable("page0cell3"), "workspace cells travel");
        assertTrue(LayoutBackup.portable("dock1"), "dock travels");
        assertTrue(LayoutBackup.portable("folderName:a/b|c/d"), "folder names travel");
        assertTrue(LayoutBackup.portable("hidden:a/b"), "hidden apps travel");
        assertTrue(LayoutBackup.portable("iconPack"), "settings travel");
        for (String key : new String[]{"widget", "widgetPage", "widgetX", "widgetY", "widgetWidth",
                "widgetHeightFraction", "widgetHeight", "widgetReserved", "pendingPlacement"})
            assertTrue(!LayoutBackup.portable(key), key + " stays local");
        assertTrue(!LayoutBackup.portable(""), "empty key rejected");
        assertTrue(!LayoutBackup.portable(null), "null key rejected");
        List<String> keys = LayoutBackup.restorable(Arrays.asList("widget", "pages", "dock0", "pendingPlacement", "labels"));
        assertTrue(keys.equals(Arrays.asList("dock0", "labels", "pages")), "restorable keys are portable and sorted: " + keys);
        System.out.println("PASS LayoutBackupTest");
    }
    private static void assertTrue(boolean value, String message) { if (!value) throw new AssertionError(message); }
}
