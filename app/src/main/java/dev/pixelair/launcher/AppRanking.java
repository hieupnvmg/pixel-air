package dev.pixelair.launcher;

import java.text.Collator;

/** Usage means successful opens through this launcher; no device-wide usage access required. */
public final class AppRanking {
    private AppRanking() {}
    public static int compare(String a, String aid, long aopens,
                              String b, String bid, long bopens,
                              boolean byUsage, Collator collator) {
        if (byUsage && aopens != bopens) return Long.compare(bopens, aopens);
        int name = collator.compare(a, b);
        return name != 0 ? name : aid.compareTo(bid);
    }
}
