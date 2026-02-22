package dev.znxki.gravewarden.config;

import dev.znxki.gravewarden.Gravewarden;
import dev.znxki.gravewarden.utils.ColorUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum ConfigManager {
    UPDATE_CHECKER("update-checker", true),

    // Messages
    MESSAGES_DEATH("messages.death-message", "&6&lGraveWarden &8» &7Your soul has been preserved at &e{x}, {y}, {z}&7."),
    MESSAGES_GRAVE_TAKE("messages.grave-take", "&6&lGraveWarden &8» &aInventory restored! Your belongings have been returned."),
    MESSAGES_NOT_YOUR_GRAVE("messages.not-your-grave", "&c&l! &8» &7This grave does not belong to you. It marks the rest of &e{player}&7."),
    MESSAGES_SOUL_COMPASS_POINTING("messages.soul-compass-pointing", "&#7df9ff&l» &7Your &fSoul Compass &7is pointing to your grave."),
    MESSAGES_COMPASS_RESTRICTED("messages.soul-compass-restricted", "&cYou cannot put the Soul Compass in a container!"),
    MESSAGES_COMPASS_REMOVED("messages.soul-compass-removed", "&cThe Soul Compass has been destroyed because you tried to drop it!"),
    MESSAGES_ALL_GRAVES_REMOVED("messages.all-graves-removed", "&eAll restless souls have been purged from the world."),
    MESSAGES_PLAYER_GRAVES_REMOVED("messages.player-graves-removed", "&eAll souls belonging to {player} have been laid to rest."),
    MESSAGES_HAS_NOT_GRAVE("messages.player-has-not-grave", "&cNo active graves found for this player."),
    MESSAGES_PLAYER_NOT_FOUND("messages.player-not-found", "&cThis player isn't online."),
    MESSAGES_NO_PERMS("messages.no-perms", "&cYou don't have permission to do that."),
    MESSAGES_WRONG_CMD_USAGE("messages.wrong-command-usage", "&cThat's not quite right. Try /grave help."),

    // GUI
    GUI_TITLE("gui.title", "&8» &cActive Graves"),

    GUI_ITEMS_GRAVE_MATERIAL("gui.items.grave.material", "PLAYER_HEAD"),
    GUI_ITEMS_GRAVE_NAME("gui.items.grave.name", "&eGrave &7# {id}"),
    GUI_ITEMS_GRAVE_LORE("gui.items.grave.lore",
            Arrays.asList("", "&7Location:", " &8» &f{world} &7| &f{x} {y} {z}", "&7Time:", " &8» &f{time}", "")),

    GUI_ITEMS_EMPTY_MATERIAL("gui.items.empty.material", "BARRIER"),
    GUI_ITEMS_EMPTY_NAME("gui.items.empty.name", "&cNo graves found!"),
    GUI_ITEMS_EMPTY_LORE("gui.items.empty.lore",
            Arrays.asList("&7You don't have any", "&7restless souls yet.")),

    GUI_FILLER_MATERIAL("gui.filler.material", "GRAY_STAINED_GLASS_PANE"),
    GUI_FILLER_MODEL("gui.filler.model", 0),
    GUI_FILLER_NAME("gui.filler.name", "&r"),
    GUI_FILLER_LORE("gui.filler.lore", Arrays.asList()),

    GUI_ITEMS_NEXT_MATERIAL("gui.items.next-page.material", "ARROW"),
    GUI_ITEMS_NEXT_NAME("gui.items.next-page.name", "&aNext Page »"),
    GUI_ITEMS_NEXT_MODEL("gui.items.next-page.model", 0),
    GUI_ITEMS_NEXT_LORE("gui.items.next-page.lore", Arrays.asList()),

    GUI_ITEMS_PREVIOUS_MATERIAL("gui.items.previous-page.material", "ARROW"),
    GUI_ITEMS_PREVIOUS_NAME("gui.items.previous-page.name", "&c« Previous Page"),
    GUI_ITEMS_PREVIOUS_MODEL("gui.items.previous-page.model", 0),
    GUI_ITEMS_PREVIOUS_LORE("gui.items.previous-page.lore", Arrays.asList()),

    // Soul Compass
    ITEMS_SOUL_COMPASS_ENABLED("items.soul-compass.enabled", true),
    ITEMS_SOUL_COMPASS_NAME("items.soul-compass.settings.name", "&#7df9ff&lSoul Compass"),
    ITEMS_SOUL_COMPASS_MODEL("items.soul-compass.settings.model", 0),
    ITEMS_SOUL_COMPASS_LORE("items.soul-compass.settings.lore",
            Arrays.asList("&7It points toward your soul at:", "&b{x} {y} {z}", "", "&8&oWill be consumed upon retrieval.")),

    // Hologram
    HOLOGRAM_OFFSET_Y("hologram.offset-y", 1.2),
    HOLOGRAM_LINE_SPACING("hologram.line-spacing", 0.25),
    HOLOGRAM_LINES("hologram.lines",
            Arrays.asList("&c&l☠ &c%player%", "&7Death Time: &f{time}", "&eClick to open"));

    private final String path;
    private final Object def;

    public String getString() {
        return Gravewarden.getInstance().getConfig().getString(path, (String) def);
    }

    public @NonNull String getStringFormatted() {
        return ColorUtils.colorize(getString());
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

    public Material getMaterial() {
        return Material.valueOf(Gravewarden.getInstance().getConfig().getString(path, (String) def).toUpperCase());
    }

    public @NonNull List<String> getStringList() {
        return Gravewarden.getInstance().getConfig().getStringList(path);
    }

    public List<String> getFormattedStringList() {
        return getStringList()
                .stream()
                .map(ColorUtils::colorize)
                .collect(Collectors.toList());
    }
}