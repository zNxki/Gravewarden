package dev.znxki.gravewarden.config;

import dev.znxki.gravewarden.Gravewarden;
import dev.znxki.gravewarden.utils.ColorUtils;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum ConfigManager {
    UPDATE_CHECKER("update-checker", true),

    MESSAGES_DEATH("messages.death-message", "&6&lGraveWarden &8» &7Your soul has been preserved at &e{x}, {y}, {z}&7."),
    MESSAGES_GRAVE_TAKE("messages.grave-take", "&6&lGraveWarden &8» &aInventory restored! Your belongings have been returned."),
    MESSAGES_NOT_YOUR_GRAVE("messages.not-your-grave", "&c&l! &8» &7This grave does not belong to you. It marks the rest of &e{player}&7."),
    MESSAGES_SOUL_COMPASS_POINTING("messages.soul-compass-pointing", "&#7df9ff&l» &7Your &fSoul Compass &7is pointing to your grave."),
    MESSAGES_COMPASS_RESTRICTED("messages.soul-compass-restricted", "&cYou cannot put the Soul Compass in a container!"),
    MESSAGES_COMPASS_REMOVED("messages.soul-compass-removed", "&cThe Soul Compass has been destroyed because you tried to drop it!"),

    ITEMS_SOUL_COMPASS_ENABLED("items.soul-compass.enabled", true),
    ITEMS_SOUL_COMPASS_NAME("items.soul-compass.settings.name", "&#7df9ff&lSoul Compass"),
    ITEMS_SOUL_COMPASS_MODEL("items.soul-compass.settings.model", 0),
    ITEMS_SOUL_COMPASS_LORE("items.soul-compass.settings.lore", Arrays.asList("&7Death point: &b{x} {y} {z}")),

    HOLOGRAM_OFFSET_Y("hologram.offset-y", 1.2),
    HOLOGRAM_LINE_SPACING("hologram.line-spacing", 0.25),
    HOLOGRAM_LINES("hologram.lines", Arrays.asList("&c&l☠ &c%player%", "&7Death Time: &f{time}", "&eClick to open"));

    private final String path;
    private final Object def;

    public String getString() {
        return Gravewarden.getInstance().getConfig().getString(path, (String) def);
    }

    public String getStringFormatted() {
        return ColorUtils.colorize(Gravewarden.getInstance().getConfig().getString(path, (String) def));
    }

    public boolean getBoolean() {
        return Gravewarden.getInstance().getConfig().getBoolean(path, (boolean) def);
    }

    public int getInteger() {
        return Gravewarden.getInstance().getConfig().getInt(path, (int) def);
    }

    public double getDouble() {
        return Gravewarden.getInstance().getConfig().getDouble(path, (double) def);
    }

    public List<String> getStringList() {
        return Gravewarden.getInstance().getConfig().getStringList(path);
    }

    public List<String> getFormattedStringList() {
        return Gravewarden.getInstance().getConfig().getStringList(path)
                .stream()
                .map(ColorUtils::colorize)
                .collect(Collectors.toList());
    }
}
