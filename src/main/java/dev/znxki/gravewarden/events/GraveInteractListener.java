package dev.znxki.gravewarden.events;

import dev.znxki.gravewarden.Gravewarden;
import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.manager.GraveManager;
import dev.znxki.gravewarden.manager.InventoryManager;
import dev.znxki.gravewarden.manager.SoulCompassManager;
import dev.znxki.gravewarden.objects.Grave;
import dev.znxki.gravewarden.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class GraveInteractListener implements Listener {
    @EventHandler
    public void onGraveInteract(@NotNull PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Block block = event.getClickedBlock();
        if (block == null || !block.getType().equals(Material.PLAYER_HEAD)) return;

        if (!(block.getState() instanceof Skull skull)) return;

        NamespacedKey graveIdKey = new NamespacedKey(Gravewarden.getInstance(), "grave_id");
        String graveId = skull.getPersistentDataContainer().get(graveIdKey, PersistentDataType.STRING);
        if (graveId == null) return;

        event.setCancelled(true);
        Player player = event.getPlayer();
        Grave grave = GraveManager.graveById(player.getUniqueId(), graveId);

        if (grave == null)
            return;

        if (!grave.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ColorUtils.colorize(Gravewarden.getInstance().getConfig().getString("not-your-grave").replace("{player}", grave.getPlayer().getName())));
            return;
        }

        Location loc = block.getLocation().clone().add(0.5, 0.5, 0.5);

        player.getWorld().spawnParticle(Particle.SOUL, loc, 30, 0.3, 0.5, 0.3, 0.1);
        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 20, 0.2, 0.2, 0.2, 0.05);
        player.getWorld().spawnParticle(Particle.ENCHANT, player.getLocation().add(0, 1, 0), 50, 0.5, 0.8, 0.5, 0.1);

        for (double i = 0; i < Math.PI * 2; i += Math.PI / 8) {
            double x = Math.cos(i) * 0.5;
            double z = Math.sin(i) * 0.5;

            player.getWorld().spawnParticle(Particle.WITCH, loc.clone().add(x, 0, z), 1, 0, 0.1, 0, 0);
        }

        SoulCompassManager.removeSoulCompass(player);
        InventoryManager.restoreInventory(player, grave);

        block.setType(Material.AIR);
        GraveManager.removeGrave(player, grave);

        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 0.8f);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0.5f);

        player.sendMessage(ConfigManager.MESSAGES_GRAVE_TAKE.getStringFormatted());
    }
}
