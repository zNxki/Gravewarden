package dev.znxki.gravewarden.manager;

import dev.znxki.gravewarden.Gravewarden;
import dev.znxki.gravewarden.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SoulCompassManager {
    private static final NamespacedKey COMPASS_KEY = new NamespacedKey(Gravewarden.getInstance(), "soul-compass");

    public static ItemStack createSoulCompass(Location deathLocation) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();

        FileConfiguration config = Gravewarden.getInstance().getConfig();

        meta.setLodestoneTracked(false);
        meta.setLodestone(deathLocation);

        meta.getPersistentDataContainer().set(COMPASS_KEY, PersistentDataType.BYTE, (byte) 1);

        meta.setDisplayName(ColorUtils.colorize(
                config.getString("items.soul-compass.settings.name"))
        );
        meta.setCustomModelData(config.getInt("items.soul-compass.settings.model", 0));
        meta.setLore(config.getStringList("items.soul-compass.settings.lore")
                .stream()
                .map(line -> ColorUtils.colorize(line
                                .replace("{x}", String.valueOf(deathLocation.getBlockX()))
                                .replace("{y}", String.valueOf(deathLocation.getBlockY()))
                                .replace("{z}", String.valueOf(deathLocation.getBlockZ()))
                        )
                )
                .collect(Collectors.toList())
        );

        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_STORED_ENCHANTS);

        compass.setItemMeta(meta);
        compass.setAmount(1);

        return compass;
    }

    public static void removeSoulCompass(Player player) {
        Arrays.asList(player.getInventory().getContents()).forEach(item -> {
            if (item == null || !item.getType().equals(Material.COMPASS) || !isSoulCompass(item)) return;

            item.setAmount(0);
        });
    }

    public static boolean isSoulCompass(ItemStack item) {
        return item != null && item.getType().equals(Material.COMPASS) &&
                item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(COMPASS_KEY, PersistentDataType.BYTE);
    }
}

