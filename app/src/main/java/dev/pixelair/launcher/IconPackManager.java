package dev.pixelair.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.util.*;

/** Resource-only appfilter support. Never loads executable code from an icon pack. */
public final class IconPackManager {
    public static final String[] ACTIONS = {"org.adw.launcher.THEMES", "com.novalauncher.THEME",
            "com.gau.go.launcherex.theme", "ch.deletescape.lawnchair.ICONPACK",
            "app.lawnchair.icons.THEMED_ICON", "com.sonymobile.home.ICON_PACK", "ginlemon.smartlauncher.THEMES"};
    public static final class Pack {
        public final String packageName, label;
        Pack(String pkg, String name) { packageName = pkg; label = name; }
    }
    private final Context packContext;
    private final Resources resources;
    private final String packageName;
    private final Map<String, String> icons = new HashMap<>(), calendars = new HashMap<>();
    private final LruCache<String, Drawable.ConstantState> cache = new LruCache<>(128);
    private final Set<String> missing = new HashSet<>();
    private int day;

    private IconPackManager(Context context, String pkg) throws Exception {
        packageName = pkg;
        packContext = context.createPackageContext(pkg, 0);
        resources = packContext.getResources();
        int theme = context.getPackageManager().getApplicationInfo(pkg, 0).theme;
        if (theme != 0) packContext.setTheme(theme);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
    public static List<Pack> installed(Context context) {
        PackageManager pm = context.getPackageManager();
        Map<String, Pack> found = new HashMap<>();
        ArrayList<Intent> intents = new ArrayList<>();
        for (String action : ACTIONS) intents.add(new Intent(action));
        intents.add(new Intent(Intent.ACTION_MAIN).addCategory("com.anddoes.launcher.THEME"));
        for (Intent intent : intents) for (ResolveInfo r : pm.queryIntentActivities(intent, 0)) {
            String pkg = r.activityInfo.packageName;
            try {
                Resources res = pm.getResourcesForApplication(pkg);
                if (hasFilter(res, pkg)) found.put(pkg, new Pack(pkg, pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString()));
            } catch (Exception ignored) { /* Package may have been removed during discovery. */ }
        }
        ArrayList<Pack> result = new ArrayList<>(found.values());
        java.text.Collator collator = java.text.Collator.getInstance(); result.sort((a,b) -> collator.compare(a.label,b.label));
        return result;
    }
    private static boolean hasFilter(Resources res, String pkg) {
        if (res.getIdentifier("appfilter", "xml", pkg) != 0 || res.getIdentifier("appfilter", "raw", pkg) != 0) return true;
        try (InputStream ignored = res.getAssets().open("appfilter.xml")) { return true; } catch (Exception e) { return false; }
    }
    public static IconPackManager load(Context context, String pkg) {
        if (pkg == null || pkg.isEmpty()) return null;
        try {
            IconPackManager pack = new IconPackManager(context, pkg);
            int xml = pack.resources.getIdentifier("appfilter", "xml", pkg);
            if (xml != 0) {
                try (XmlResourceParser parser = pack.resources.getXml(xml)) { pack.parse(parser); }
            } else {
                int raw = pack.resources.getIdentifier("appfilter", "raw", pkg);
                try (InputStream stream = raw != 0 ? pack.resources.openRawResource(raw) : pack.resources.getAssets().open("appfilter.xml")) {
                    XmlPullParser parser = Xml.newPullParser(); parser.setInput(stream, "UTF-8"); pack.parse(parser);
                }
            }
            return pack.icons.isEmpty() && pack.calendars.isEmpty() ? null : pack;
        } catch (Exception e) { return null; }
    }
    private void parse(XmlPullParser parser) throws Exception {
        int entries = 0;
        for (int event = parser.getEventType(); event != XmlPullParser.END_DOCUMENT; event = parser.next()) {
            if (event != XmlPullParser.START_TAG) continue;
            if (++entries > 100000) throw new IllegalArgumentException("Icon filter too large");
            String component = IconPackMapping.normalize(parser.getAttributeValue(null, "component"));
            if (component == null) continue;
            if (parser.getName().equals("item")) {
                String drawable = parser.getAttributeValue(null, "drawable"); if (drawable != null) icons.put(component, drawable);
            } else if (parser.getName().equals("calendar")) {
                String prefix = parser.getAttributeValue(null, "prefix"); if (prefix != null) calendars.put(component, prefix);
            }
        }
    }
    public boolean refreshDayIfNeeded() {
        int now = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (day == now) return false;
        day = now; cache.evictAll(); missing.clear(); return true;
    }
    public Drawable icon(String component, Drawable fallback) {
        String key = IconPackMapping.normalize(component);
        if (key == null) return fallback;
        Drawable.ConstantState cached = cache.get(key);
        if (cached != null) return cached.newDrawable(resources).mutate();
        if (missing.contains(key)) return fallback;
        String name = calendars.containsKey(key) ? calendars.get(key) + day : icons.get(key);
        if (name != null) try {
            int id = resources.getIdentifier(name, "drawable", packageName);
            if (id == 0) id = resources.getIdentifier(name, "mipmap", packageName);
            if (id != 0) {
                Drawable drawable = resources.getDrawable(id, packContext.getTheme());
                if (drawable.getConstantState() != null) cache.put(key, drawable.getConstantState());
                return drawable.mutate();
            }
        } catch (RuntimeException ignored) { /* Unsupported resource: retain the app's own icon. */ }
        missing.add(key); return fallback;
    }
}
