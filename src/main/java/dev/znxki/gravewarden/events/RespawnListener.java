package dev.znxki.gravewarden.events;

import dev.znxki.gravewarden.Gravewarden;
import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.manager.GraveManager;
import dev.znxki.gravewarden.manager.SoulCompassManager;
import dev.znxki.gravewarden.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RespawnListener implements Listener {
    @EventHandler
    public void onRespawn(@NotNull PlayerRespawnEvent event) {
        if (!Gravewarden.getInstance().getConfig().getBoolean("items.soul-compass.enabled", true)) return;

        Player player = event.getPlayer();
        Location graveLoc = GraveManager.getLatestGraveLocation(event.getPlayer().getUniqueId());
        if (graveLoc == null) return;

        Bukkit.getScheduler().runTaskLater(Gravewarden.getInstance(), () -> {
            player.getInventory().addItem(SoulCompassManager.createSoulCompass(graveLoc));
            player.sendMessage(ConfigManager.MESSAGES_SOUL_COMPASS_POINTING.getStringFormatted());
        }, 20L);
    }
}
