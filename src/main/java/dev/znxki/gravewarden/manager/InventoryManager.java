package dev.znxki.gravewarden.manager;

import dev.znxki.gravewarden.objects.Grave;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;

public class InventoryManager {
    public static void restoreInventory(Player player, Grave grave) {
        List<ItemStack> savedItems = grave.getItems();
        PlayerInventory playerInventory = player.getInventory();
        World world = player.getWorld();
        Location dropLocation = player.getLocation();

        for (int i = 0; i < savedItems.size(); i++) {
            ItemStack itemToRestore = savedItems.get(i);

            if (itemToRestore == null || itemToRestore.getType().equals(Material.AIR)) continue;
            if (i < playerInventory.getSize()) {
                ItemStack currentItem = playerInventory.getItem(i);

                if (currentItem == null || currentItem.getType().equals(Material.AIR)) {
                    playerInventory.setItem(i, itemToRestore);
                    continue;
                }

                Map<Integer, ItemStack> overflow = playerInventory.addItem(itemToRestore);
                if (overflow.isEmpty()) continue;

                overflow.values().forEach(item -> world.dropItemNaturally(dropLocation, item));
                continue;
            }

            world.dropItemNaturally(dropLocation, itemToRestore);
        }
    }
}
