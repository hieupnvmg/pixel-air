package dev.pixelair.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Stable item addresses: page * 20 + cell; dock cells use -1 through -4. */
public final class WorkspaceState {
    public static final int PAGE_SIZE = 20;
    public final List<String[]> pages = new ArrayList<>();
    public final String[] dock = {"", "", "", ""};
    public int currentPage;

    public WorkspaceState() { addPage(); }
    public int addPage() {
        String[] items = new String[PAGE_SIZE];
        Arrays.fill(items, "");
        pages.add(items);
        return pages.size() - 1;
    }
    public int capacity(int page) { return page == 0 ? 8 : PAGE_SIZE; }
    public String get(int address) {
        return address < 0 ? dock[-address - 1] : pages.get(address / PAGE_SIZE)[address % PAGE_SIZE];
    }
    public void put(int address, String value) {
        if (address < 0) dock[-address - 1] = value;
        else pages.get(address / PAGE_SIZE)[address % PAGE_SIZE] = value;
    }
    public boolean empty(int page) {
        for (String value : pages.get(page)) if (!value.isEmpty()) return false;
        return true;
    }
    public int freeOnPage(int page) {
        for (int n = 0; n < capacity(page); n++) if (pages.get(page)[n].isEmpty()) return page * PAGE_SIZE + n;
        return Integer.MIN_VALUE;
    }
    public int addApp(String id) {
        int free = freeOnPage(currentPage);
        if (free != Integer.MIN_VALUE) { put(free, id); return currentPage; }
        for (int p = 0; p < pages.size(); p++) {
            free = freeOnPage(p);
            if (free != Integer.MIN_VALUE) { put(free, id); return p; }
        }
        int p = addPage(); put(p * PAGE_SIZE, id); return p;
    }
    public void swap(int from, int to) {
        String old = get(to); put(to, get(from)); put(from, old);
    }
    public void merge(int from, int to) {
        if (from == to) return;
        put(to, get(to).isEmpty() ? get(from) : get(to) + "|" + get(from));
        put(from, "");
    }
    public boolean removeEmptyCurrentPage() {
        if (currentPage == 0 || !empty(currentPage)) return false;
        pages.remove(currentPage); currentPage = Math.min(currentPage, pages.size() - 1); return true;
    }
}
