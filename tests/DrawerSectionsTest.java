import dev.pixelair.launcher.DrawerSections;
import java.util.*;

public class DrawerSectionsTest {
    record App(String id, String category) {}
    static void check(boolean value) { if (!value) throw new AssertionError(); }
    static DrawerSections.Section<App> section(List<DrawerSections.Section<App>> sections, String key) {
        return sections.stream().filter(s -> s.key.equals(key)).findFirst().orElseThrow();
    }
    public static void main(String[] args) {
        List<App> input = new ArrayList<>();
        for (int n = 0; n < 8; n++) input.add(new App("frequent-" + n, "games"));
        for (int n = 0; n < 9; n++) input.add(new App("game-" + n, "games"));
        for (int n = 0; n < 4; n++) input.add(new App("tool-" + n, "tools"));
        for (int n = 0; n < 6; n++) input.add(new App("media-" + n, "media"));
        for (int n = 0; n < 6; n++) input.add(new App("work-" + n, "productivity"));
        input.add(new App("unknown", "future-category"));
        input.add(new App("frequent-0", "games"));
        List<DrawerSections.Section<App>> sections = DrawerSections.build(input, 8, true, App::id, App::category);
        check(section(sections, "frequent").apps.size() == 8);
        check(section(sections, "games").apps.size() == 9 && section(sections, "games").rowCount(4) == 4);
        check(section(sections, "media").apps.size() == 6 && section(sections, "media").rowCount(4) == 3);
        check(section(sections, "other").apps.get(0).id().equals("unknown"));
        Set<String> ids = new HashSet<>();
        for (DrawerSections.Section<App> s : sections) for (App app : s.apps) check(ids.add(app.id()));
        check(ids.size() == input.size() - 1);
        check(sections.stream().noneMatch(s -> s.apps.isEmpty() || s.key.equals("social")));
        Map<String, Integer> positions = DrawerSections.headerPositions(sections, 4);
        check(positions.get("frequent") == 0 && positions.get("games") == 3);
        check(positions.get("tools") == 7 && positions.get("media") == 9 && positions.get("productivity") == 12);
        check(sections.stream().mapToInt(s -> s.apps.size()).sum() == ids.size()); // Jump metadata doesn't filter any app.
        List<DrawerSections.Section<App>> plain = DrawerSections.build(input, 0, false, App::id, App::category);
        check(plain.size() == 1 && plain.get(0).key.equals("all") && plain.get(0).apps.size() == ids.size());
        check(DrawerSections.build(input, 99, false, App::id, App::category).get(0).apps.size() == 8);
        check(DrawerSections.build(List.<App>of(), 8, true, App::id, App::category).isEmpty());
        System.out.println("PASS expanded multi-row sections, all-app coverage, frequent deduplication, unknown categories and jump row positions");
    }
}
