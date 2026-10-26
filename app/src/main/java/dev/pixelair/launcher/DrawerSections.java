package dev.pixelair.launcher;

import java.util.*;
import java.util.function.Function;

/** Expanded sections in one continuous drawer. Jump chips never filter or remove apps. */
public final class DrawerSections {
    public static final String[] ORDER = {"games", "tools", "media", "productivity", "social", "maps", "news", "shopping", "other"};
    public static final class Section<T> {
        public final String key;
        public final List<T> apps;
        Section(String key, List<T> apps) { this.key = key; this.apps = Collections.unmodifiableList(new ArrayList<>(apps)); }
        public int rowCount(int columns) {
            if (columns < 1) throw new IllegalArgumentException("columns must be positive");
            return 1 + (apps.size() + columns - 1) / columns;
        }
    }
    public static <T> List<Section<T>> build(List<T> ordered, int frequentCount, boolean grouped,
                                             Function<T, String> id, Function<T, String> category) {
        ArrayList<Section<T>> result = new ArrayList<>();
        ArrayList<T> frequent = new ArrayList<>(), remaining = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        int prefix = Math.max(0, Math.min(8, Math.min(frequentCount, ordered.size())));
        for (int n = 0; n < ordered.size(); n++) {
            T app = ordered.get(n);
            if (!seen.add(id.apply(app))) continue;
            (n < prefix ? frequent : remaining).add(app);
        }
        if (!frequent.isEmpty()) result.add(new Section<>("frequent", frequent));
        if (!grouped) {
            if (!remaining.isEmpty()) result.add(new Section<>(frequent.isEmpty() ? "all" : "other-apps", remaining));
            return result;
        }
        Map<String, ArrayList<T>> groups = new LinkedHashMap<>();
        for (String key : ORDER) groups.put(key, new ArrayList<>());
        for (T app : remaining) {
            String key = category.apply(app);
            groups.get(groups.containsKey(key) ? key : "other").add(app);
        }
        for (Map.Entry<String, ArrayList<T>> group : groups.entrySet())
            if (!group.getValue().isEmpty()) result.add(new Section<>(group.getKey(), group.getValue()));
        return result;
    }
    public static <T> Map<String, Integer> headerPositions(List<Section<T>> sections, int columns) {
        Map<String, Integer> result = new LinkedHashMap<>();
        int row = 0;
        for (Section<T> section : sections) { result.put(section.key, row); row += section.rowCount(columns); }
        return result;
    }
}
