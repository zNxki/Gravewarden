package dev.znxki.gravewarden.events;

import dev.znxki.gravewarden.Gravewarden;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Iterator;

public class GraveProtectionListener implements Listener {
    private final NamespacedKey graveIdKey = new NamespacedKey(Gravewarden.getInstance(), "grave_id");

    private void handleExplosion(@NonNull Iterator<Block> iterator) {
        while (iterator.hasNext()) {
            Block block = iterator.next();

            if (!block.getType().equals(Material.PLAYER_HEAD)) continue;
            if (!(block.getState() instanceof Skull skull)) continue;
            if (!skull.getPersistentDataContainer().has(graveIdKey, PersistentDataType.STRING)) continue;

            iterator.next();
        }
    }

    private boolean isGrave(@NonNull Block block) {
        if (!block.getType().equals(Material.PLAYER_HEAD)) return false;
        if (!(block.getState() instanceof Skull skull)) return false;

        return skull.getPersistentDataContainer().has(graveIdKey, PersistentDataType.STRING);
    }

    @EventHandler
    public void onEntityExplode(@NonNull EntityExplodeEvent event) {
        handleExplosion(event.blockList().iterator());
    }

    @EventHandler
    public void onBlockExplode(@NonNull BlockExplodeEvent event) {
        handleExplosion(event.blockList().iterator());
    }

    @EventHandler
    public void onPistonExtend(@NonNull BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (!isGrave(block)) continue;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonRetract(@NonNull BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (!isGrave(block)) continue;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(@NonNull BlockBurnEvent event) {
        if (!isGrave(event.getBlock())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(@NonNull BlockIgniteEvent event) {
        if (!isGrave(event.getBlock())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onLiquidFlow(@NonNull BlockFromToEvent event) {
        if(!isGrave(event.getToBlock())) return;

        event.setCancelled(true);
    }
}
