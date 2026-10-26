package dev.pixelair.launcher;

import java.util.*;

/** Paginates complete sections without consulting the vertical drawer's collapsed state. */
public final class DrawerPages {
    public static final class Page<T> {
        public final String key;
        public final List<T> apps;
        public final int part, parts, total;
        Page(String key, List<T> apps, int part, int parts, int total) {
            this.key=key; this.apps=Collections.unmodifiableList(new ArrayList<>(apps));
            this.part=part; this.parts=parts; this.total=total;
        }
    }
    public static <T> List<Page<T>> build(List<DrawerSections.Section<T>> sections, int capacity) {
        if(capacity<1)throw new IllegalArgumentException("capacity must be positive");
        List<Page<T>> pages=new ArrayList<>();
        for(DrawerSections.Section<T> section:sections){
            int parts=(section.apps.size()+capacity-1)/capacity;
            for(int part=0;part<parts;part++){
                int start=part*capacity;
                pages.add(new Page<>(section.key,section.apps.subList(start,Math.min(start+capacity,section.apps.size())),part,parts,section.apps.size()));
            }
        }
        return pages;
    }
    public static int firstPage(List<? extends Page<?>> pages, String key){
        for(int n=0;n<pages.size();n++)if(pages.get(n).key.equals(key))return n;
        return -1;
    }
}
