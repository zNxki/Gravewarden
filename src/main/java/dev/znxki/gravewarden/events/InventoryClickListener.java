package dev.znxki.gravewarden.events;

import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.manager.SoulCompassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (!SoulCompassManager.isSoulCompass(item)) return;

        Inventory targetInv = event.getClickedInventory();
        if (targetInv == null) return;

        if (!targetInv.getType().equals(InventoryType.PLAYER)) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            player.sendMessage(ConfigManager.MESSAGES_COMPASS_RESTRICTED.getStringFormatted());
            return;
        }

        if (!event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) return;

        Inventory topInv = event.getView().getTopInventory();
        if (topInv.getType().equals(InventoryType.PLAYER) || topInv.getType().equals(InventoryType.CRAFTING)) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        player.sendMessage(ConfigManager.MESSAGES_COMPASS_RESTRICTED.getStringFormatted());
    }
}
