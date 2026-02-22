package dev.znxki.gravewarden.utils;

import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.storage.LocalStorage;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HologramUtils {
    public static List<ArmorStand> createHologram(Location location, Player player, LocalTime deathTime, String graveId) {
        List<ArmorStand> stands = new ArrayList<>();

        List<String> lines = ConfigManager.HOLOGRAM_LINES.getFormattedStringList();
        double offsetY = ConfigManager.HOLOGRAM_OFFSET_Y.getDouble();
        double spacing = ConfigManager.HOLOGRAM_LINE_SPACING.getDouble();

        Location holoLoc = location.clone().add(0, offsetY, 0);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            line = line.replace("{player}", player.getName());
            line = line.replace("{time}", deathTime.format(DateTimeFormatter.ofPattern("HH:mm")));

            ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(
                    holoLoc.clone().add(0, spacing * (lines.size() - i), 0),
                    EntityType.ARMOR_STAND
            );

            stand.addScoreboardTag("grave_id_" + graveId);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setMarker(true);
            stand.setSmall(true);
            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setInvulnerable(true);

            stands.add(stand);
        }

        LocalStorage.getHolograms().addAll(stands);
        return stands;
    }

    public static void removeHologram(Location location, String graveId) {
        double radius = 5;

        location.getWorld().getNearbyEntities(location, radius, radius, radius).forEach(entity -> {
            if (!(entity instanceof ArmorStand)) return;
            if (!entity.getScoreboardTags().contains("grave_id_" + graveId)) return;

            entity.remove();
            LocalStorage.getHolograms().remove((ArmorStand) entity);
        });
    }
}
