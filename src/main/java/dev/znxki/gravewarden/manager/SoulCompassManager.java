package dev.znxki.gravewarden.manager;

import dev.znxki.gravewarden.Gravewarden;
import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.utils.ColorUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SoulCompassManager {
    private static final NamespacedKey COMPASS_KEY = new NamespacedKey(Gravewarden.getInstance(), "soul-compass");

    public static ItemStack createSoulCompass(Location deathLocation) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();

        meta.setLodestoneTracked(false);
        meta.setLodestone(deathLocation);

        meta.getPersistentDataContainer().set(COMPASS_KEY, PersistentDataType.BYTE, (byte) 1);

        meta.setDisplayName(ConfigManager.ITEMS_SOUL_COMPASS_NAME.getStringFormatted());
        meta.setCustomModelData(ConfigManager.ITEMS_SOUL_COMPASS_MODEL.getInteger());
        meta.setLore(ConfigManager.ITEMS_SOUL_COMPASS_LORE.getFormattedStringList()
                .stream()
                .map(line -> line
                        .replace("{x}", String.valueOf(deathLocation.getBlockX()))
                        .replace("{y}", String.valueOf(deathLocation.getBlockY()))
                        .replace("{z}", String.valueOf(deathLocation.getBlockZ()))
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

    public static void startActionBarTracker(Player player, Location graveLoc) {
        if (!ConfigManager.ACTIONBAR_TRACKER_ENABLED.getBoolean()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || GraveManager.getLatestGraveLocation(player.getUniqueId()) == null) {
                    cancel();
                    return;
                }

                if (!player.getWorld().equals(graveLoc.getWorld())) return;
                double distance = player.getLocation().distance(graveLoc);

                if (distance < 2) {
                    sendActionBar(player, ConfigManager.ACTIONBAR_TRACKER_ARRIVED.getStringFormatted());

                    player.playSound(player.getLocation(),
                            Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
                            1f, 1f);

                    cancel();
                    return;
                }

                sendActionBar(player, ConfigManager.ACTIONBAR_TRACKER_DISTANCE.getStringFormatted()
                        .replace("{blocks}", String.valueOf((int) distance)));
            }
        }.runTaskTimer(Gravewarden.getInstance(), 0L, 10L);
    }

    private static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent(message));
    }
}

