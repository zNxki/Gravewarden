package dev.znxki.gravewarden.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
@Getter
public class Grave {
    private final String graveId;
    private final Player player;
    private final Location location;
    private final List<ItemStack> items;
}
