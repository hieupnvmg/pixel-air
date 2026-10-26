package dev.pixelair.launcher;

public final class IconPackMapping {
    private IconPackMapping() {}
    public static String normalize(String component) {
        if (component == null) return null;
        String value = component.trim();
        if (value.startsWith("ComponentInfo{") && value.endsWith("}"))
            value = value.substring(14, value.length() - 1);
        int slash = value.indexOf('/');
        if (slash < 1 || slash == value.length() - 1 || value.indexOf('/', slash + 1) >= 0) return null;
        String pkg = value.substring(0, slash), activity = value.substring(slash + 1);
        if (pkg.contains(" ") || activity.contains(" ") || value.contains("\n")) return null;
        if (activity.startsWith(".")) activity = pkg + activity;
        return pkg + "/" + activity;
    }
}
