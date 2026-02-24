package dev.znxki.gravewarden.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LocationUtils {
    public static Location getSafeLocation(Location loc) {
        World world = loc.getWorld();
        if (world == null) return loc;

        if (loc.getY() < world.getMinHeight()) {
            loc.setY(world.getMinHeight() + 1);
        }

        Block block = loc.getBlock();
        if (block.isLiquid() || block.getType().isSolid()) {
            while ((loc.getBlock().isLiquid() || loc.getBlock().getType().isSolid())
                    && loc.getY() < world.getMaxHeight())
                loc.add(0, 1, 0);
        }

        return loc;
    }
}
