package dev.znxki.gravewarden.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    private static final Pattern HEX_PATTERN =
            Pattern.compile("(?i)(?:&#|#)([0-9A-F]{6})");

    public static String colorize(String text) {
        if (text == null || text.isEmpty()) return "";

        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");

            for (char c : hex.toCharArray()) {
                replacement.append('§').append(c);
            }

            matcher.appendReplacement(buffer, replacement.toString());
        }

        matcher.appendTail(buffer);
        return buffer.toString().replace("&", "§");
    }
}