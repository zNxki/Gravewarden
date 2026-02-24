package dev.znxki.gravewarden.events;

import dev.znxki.gravewarden.Gravewarden;
import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.manager.GraveManager;
import dev.znxki.gravewarden.objects.Grave;
import dev.znxki.gravewarden.utils.ExperienceUtil;
import dev.znxki.gravewarden.utils.HologramUtils;
import dev.znxki.gravewarden.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        Location deathLocation = LocationUtils.getSafeLocation(event.getEntity().getLocation());

        ItemStack[] contents = event.getEntity().getInventory().getContents();
        List<ItemStack> items = Arrays.asList(contents.clone());
        event.getDrops().clear();

        int totalExperience = ExperienceUtil.getTotalExperience(event.getEntity());
        event.setDroppedExp(0);

        NamespacedKey graveIdKey = new NamespacedKey(Gravewarden.getInstance(), "grave_id");
        String uniqueGraveId = event.getEntity().getUniqueId() + "_" + System.currentTimeMillis();

        Block block = deathLocation.getBlock();
        block.setType(Material.PLAYER_HEAD);

        if (block.getState() instanceof Skull skull) {
            skull.setOwningPlayer(event.getEntity());
            skull.getPersistentDataContainer().set(graveIdKey, PersistentDataType.STRING, uniqueGraveId);
            skull.update();
        }

        Grave grave = new Grave(uniqueGraveId, event.getEntity(), deathLocation, LocalTime.now().withNano(0), totalExperience, items);
        GraveManager.addGrave(event.getEntity().getUniqueId(), grave);

        event.getEntity().sendMessage(ConfigManager.MESSAGES_DEATH.getStringFormatted()
                .replace("{x}", String.valueOf(deathLocation.getBlockX()))
                .replace("{y}", String.valueOf(deathLocation.getBlockY()))
                .replace("{z}", String.valueOf(deathLocation.getBlockZ()))
        );

        HologramUtils.createHologram(
                deathLocation.clone().add(0.5, 0, 0.5),
                event.getEntity(),
                LocalTime.now().withNano(0),
                uniqueGraveId
        );
    }
}
