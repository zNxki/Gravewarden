package dev.znxki.gravewarden.events;

import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.manager.SoulCompassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DropCompassListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (!SoulCompassManager.isSoulCompass(event.getItemDrop().getItemStack())) return;

        event.setCancelled(true);
        SoulCompassManager.removeSoulCompass(event.getPlayer());
        event.getPlayer().sendMessage(ConfigManager.MESSAGES_COMPASS_REMOVED.getStringFormatted());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryDragOut(InventoryClickEvent event) {
        if (!event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;

        ItemStack item = event.getCursor();
        if (!SoulCompassManager.isSoulCompass(item)) return;

        event.setCancelled(true);
        event.setCursor(null);

        Player player = (Player) event.getWhoClicked();
        player.sendMessage(ConfigManager.MESSAGES_COMPASS_REMOVED.getStringFormatted());
    }
}
