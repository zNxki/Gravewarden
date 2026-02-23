package dev.znxki.gravewarden.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.storage.LocalStorage;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GraveListGui {
    private final Player player;
    private Player target;

    public GraveListGui(@NotNull Player player, @Nullable Player target) {
        this.player = player;

        if (target == null) this.target = player;
        this.target = target;
    }

    public void open() {
        PaginatedGui gui = Gui.paginated().rows(4)
                .title(Component.text(ConfigManager.GUI_TITLE.getStringFormatted())).create();

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        gui.getFiller().fillBottom(ItemBuilder.from(ConfigManager.GUI_FILLER_MATERIAL.getMaterial())
                .setName(ConfigManager.GUI_FILLER_NAME.getStringFormatted())
                .model(ConfigManager.GUI_FILLER_MODEL.getInteger())
                .setLore(ConfigManager.GUI_FILLER_LORE.getFormattedStringList()).asGuiItem());


        gui.setItem(4, 1, ItemBuilder.from(ConfigManager.GUI_ITEMS_PREVIOUS_MATERIAL.getMaterial())
                .setName(ConfigManager.GUI_ITEMS_PREVIOUS_NAME.getStringFormatted())
                .model(ConfigManager.GUI_ITEMS_PREVIOUS_MODEL.getInteger())
                .setLore(ConfigManager.GUI_ITEMS_PREVIOUS_LORE.getFormattedStringList())
                .asGuiItem(event -> gui.previous()));

        gui.setItem(4, 9, ItemBuilder.from(ConfigManager.GUI_ITEMS_NEXT_MATERIAL.getMaterial())
                .setName(ConfigManager.GUI_ITEMS_NEXT_NAME.getStringFormatted())
                .model(ConfigManager.GUI_ITEMS_NEXT_MODEL.getInteger())
                .setLore(ConfigManager.GUI_ITEMS_NEXT_LORE.getFormattedStringList())
                .asGuiItem(event -> gui.next()));

        if (!LocalStorage.getGraves().containsKey(target.getUniqueId()) ||
                LocalStorage.getGraves().get(target.getUniqueId()).isEmpty()) {
            gui.setItem(2, 5, ItemBuilder.from(ConfigManager.GUI_ITEMS_EMPTY_MATERIAL.getMaterial())
                    .setName(ConfigManager.GUI_ITEMS_EMPTY_NAME.getStringFormatted())
                    .setLore(ConfigManager.GUI_ITEMS_EMPTY_LORE.getFormattedStringList())
                    .asGuiItem(event -> event.getWhoClicked().closeInventory()));
            gui.open(player);
            return;
        }

        AtomicInteger id = new AtomicInteger(1);
        LocalStorage.getGraves().get(target.getUniqueId()).forEach(grave -> {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(target);
            skull.setItemMeta(meta);

            GuiItem graveItem = ItemBuilder.from(skull)
                    .setName(ConfigManager.GUI_ITEMS_GRAVE_NAME.getStringFormatted()
                            .replace("{id}", String.valueOf(id.getAndIncrement())))
                    .setLore(ConfigManager.GUI_ITEMS_GRAVE_LORE.getFormattedStringList()
                            .stream().map(line -> line
                                    .replace("{world}", grave.getLocation().getWorld().getName())
                                    .replace("{x}", String.valueOf(grave.getLocation().getBlockX()))
                                    .replace("{y}", String.valueOf(grave.getLocation().getBlockY()))
                                    .replace("{z}", String.valueOf(grave.getLocation().getBlockZ()))
                                    .replace("{time}", grave.getDeathTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                            )
                            .collect(Collectors.toList())
                    )
                    .asGuiItem(event -> {
                        if (!player.hasPermission("grave.teleport")) {
                            event.setCancelled(true);
                            return;
                        }

                        player.teleport(grave.getLocation());
                    });

            gui.addItem(graveItem);
        });

        gui.open(player);
    }
}
