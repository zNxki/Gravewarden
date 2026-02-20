package dev.znxki.gravewarden.manager;

import dev.znxki.gravewarden.objects.Grave;
import dev.znxki.gravewarden.storage.LocalStorage;
import dev.znxki.gravewarden.utils.HologramUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraveManager {
    public static void addGrave(UUID uuid, Grave grave) {
        LocalStorage.getGraves().computeIfAbsent(uuid,
                k -> new ArrayList<>()).add(grave);
    }

    public static @Nullable Grave graveById(UUID uuid, String graveId) {
        List<Grave> graves = LocalStorage.getGraves().get(uuid);
        if (graves == null) return null;

        return graves.stream()
                .filter(grave -> grave.getGraveId().equals(graveId))
                .findFirst()
                .orElse(null);
    }

    public static void removeGrave(@NotNull Player player, Grave grave) {
        List<Grave> playerGraves = LocalStorage.getGraves().get(player.getUniqueId());

        if (playerGraves == null) return;
        HologramUtils.removeHologram(grave.getLocation(), grave.getGraveId());
        playerGraves.remove(grave);

        if (!playerGraves.isEmpty()) return;
        LocalStorage.getGraves().remove(player.getUniqueId());
    }
}
