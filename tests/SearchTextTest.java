import dev.pixelair.launcher.SearchText;

public final class SearchTextTest {
    public static void main(String[] args) {
        check(SearchText.fold("Cài đặt").equals("cai dat"), "Vietnamese marks and đ fold");
        check(SearchText.fold("  Đồng   HỒ ").equals("dong ho"), "case, outer and inner spaces fold");
        check(SearchText.fold(null).isEmpty(), "null is empty");
        check(SearchText.matches("Cài đặt Wi‑Fi", SearchText.fold("cai")), "unaccented query matches accented label");
        check(SearchText.matches("Pin", SearchText.fold("pin")), "case-insensitive");
        check(!SearchText.matches("Bluetooth", SearchText.fold("wifi")), "non-match");
        check(SearchText.matches("Wi\u2011Fi", SearchText.fold("wifi")), "hyphens ignored");
        check(SearchText.matches("Date & time", SearchText.fold("datetime")), "spaces and symbols ignored");
        check(!SearchText.matches("Wi-Fi", SearchText.fold("--")), "punctuation-only query matches nothing extra");
        check(!SearchText.matches("Anything", ""), "empty query matches nothing");
        System.out.println("PASS SearchTextTest");
    }
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
}
