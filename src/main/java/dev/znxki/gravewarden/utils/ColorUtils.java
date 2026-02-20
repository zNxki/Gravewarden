package dev.znxki.gravewarden.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern BUKKIT_HEX_PATTERN = Pattern.compile("&x(&[A-Fa-f0-9]){6}");

    public static String colorize(final String message) {
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(message));
    }

    private static String translateHexColorCodes(final String message) {
        final char colorChar = ChatColor.COLOR_CHAR;

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            final String group = matcher.group(1);
            matcher.appendReplacement(buffer, colorChar + "x"
                    + colorChar + group.charAt(0) + colorChar + group.charAt(1)
                    + colorChar + group.charAt(2) + colorChar + group.charAt(3)
                    + colorChar + group.charAt(4) + colorChar + group.charAt(5));
        }
        String result = matcher.appendTail(buffer).toString();

        matcher = BUKKIT_HEX_PATTERN.matcher(result);
        buffer = new StringBuffer(result.length());
        while (matcher.find()) {
            matcher.appendReplacement(buffer, matcher.group().replace('&', colorChar));
        }

        return matcher.appendTail(buffer).toString();
    }
}
