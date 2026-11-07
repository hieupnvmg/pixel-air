package dev.pixelair.launcher;

import java.text.Normalizer;
import java.util.Locale;

/** Accent- and case-insensitive matching, so "cai dat" finds "Cài đặt" and "pin" finds "Pin". */
public final class SearchText {
    private SearchText() {}

    public static String fold(CharSequence text) {
        if (text == null) return "";
        String decomposed = Normalizer.normalize(text.toString(), Normalizer.Form.NFD);
        StringBuilder out = new StringBuilder(decomposed.length());
        for (int n = 0; n < decomposed.length(); n++) {
            char c = decomposed.charAt(n);
            if (Character.getType(c) == Character.NON_SPACING_MARK) continue;
            if (c == 'đ' || c == 'Đ') c = 'd';
            out.append(c);
        }
        return out.toString().toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");
    }

    /** True when {@code folded} (already folded) occurs in {@code text}, also ignoring spaces and punctuation ("wifi" finds "Wi‑Fi"). */
    public static boolean matches(CharSequence text, String folded) {
        if (folded.isEmpty()) return false;
        String label = fold(text);
        if (label.contains(folded)) return true;
        String compactQuery = compact(folded);
        return !compactQuery.isEmpty() && compact(label).contains(compactQuery);
    }

    private static String compact(String folded) {
        StringBuilder out = new StringBuilder(folded.length());
        for (int n = 0; n < folded.length(); n++) { char c = folded.charAt(n); if (Character.isLetterOrDigit(c)) out.append(c); }
        return out.toString();
    }
}
