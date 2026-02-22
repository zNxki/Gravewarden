package dev.znxki.gravewarden.commands;

import dev.znxki.gravewarden.Gravewarden;
import dev.znxki.gravewarden.config.ConfigManager;
import dev.znxki.gravewarden.gui.GraveListGui;
import dev.znxki.gravewarden.manager.GraveManager;
import dev.znxki.gravewarden.objects.Grave;
import dev.znxki.gravewarden.storage.LocalStorage;
import dev.znxki.gravewarden.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class GraveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (args.length < 1) {
            if (!sender.hasPermission("gravewarden.admin")) {
                Stream.of(
                                "&#17B5BD[&lGravewarden&#17B5BD] §7Running on version &#17B5BD" + Gravewarden.getInstance().getDescription().getVersion(),
                                "&#17B5BD[&lGravewarden&#17B5BD] §7Use &#17B5BD/grave help§7 to see all commands."
                        )
                        .map(ColorUtils::colorize)
                        .toList()
                        .forEach(sender::sendMessage);
                return true;
            }

            Gravewarden.getInstance().getUpdateChecker().isLatest()
                    .thenAccept(isLatest -> Bukkit.getScheduler().runTask(Gravewarden.getInstance(), () -> Stream.of(
                                    "&#17B5BD[&lGravewarden&#17B5BD] §7Running on version &#17B5BD" +
                                            Gravewarden.getInstance().getDescription().getVersion() + " " + (isLatest ? "&2(&2&l✓ &2Latest)" : "&4(&4&l✕ §4Outdated)"),
                                    "&#17B5BD[&lGravewarden&#17B5BD] §7Use &#17B5BD/grave help§7 to see all commands."
                            )
                            .map(ColorUtils::colorize)
                            .toList()
                            .forEach(sender::sendMessage)));
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            Stream.of(
                            "&#17B5BD&lGravewarden &8• &7Commands",
                            "", " &8• &#17B5BD/grave list [Player]", " &8• &#17B5BD/grave remove (Player)", " &8• &#17B5BD/grave remove-all"
                    )
                    .map(ColorUtils::colorize)
                    .toList()
                    .forEach(sender::sendMessage);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (!(sender instanceof Player player)) return true;

            if (args.length < 2) {
                if (!sender.hasPermission("gravewarden.list")) {
                    sender.sendMessage(ConfigManager.MESSAGES_NO_PERMS.getStringFormatted());
                    return true;
                }

                new GraveListGui(player, player).open();
                return true;
            }

            if (!sender.hasPermission("gravewarden.list.others")) {
                sender.sendMessage(ConfigManager.MESSAGES_NO_PERMS.getStringFormatted());
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(ConfigManager.MESSAGES_PLAYER_NOT_FOUND.getStringFormatted());
                return true;
            }

            new GraveListGui(player, target).open();
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (!sender.hasPermission("gravewarden.remove")) {
                sender.sendMessage(ConfigManager.MESSAGES_NO_PERMS.getStringFormatted());
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(ConfigManager.MESSAGES_WRONG_CMD_USAGE.getStringFormatted());
                return true;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            if (!player.hasPlayedBefore()) {
                sender.sendMessage(ConfigManager.MESSAGES_PLAYER_NOT_FOUND.getStringFormatted());
                return true;
            }

            if (!LocalStorage.getGraves().containsKey(player.getUniqueId()) ||
                    LocalStorage.getGraves().get(player.getUniqueId()).isEmpty()) {
                sender.sendMessage(ConfigManager.MESSAGES_HAS_NOT_GRAVE.getStringFormatted()
                        .replace("{player}", Objects.requireNonNull(player.getName())));
                return true;
            }

            List<Grave> playerGraves = LocalStorage.getGraves().get(player.getUniqueId());
            new ArrayList<>(playerGraves).forEach(grave ->
                    GraveManager.removeGrave(grave.getPlayer(), grave)
            );
            LocalStorage.getGraves().remove(player.getUniqueId());

            sender.sendMessage(ConfigManager.MESSAGES_PLAYER_GRAVES_REMOVED.getStringFormatted()
                    .replace("{player}", Objects.requireNonNull(player.getName())));

            return true;
        }

        if (args[0].equalsIgnoreCase("remove-all")) {
            if (!sender.hasPermission("gravewarden.remove.all")) {
                sender.sendMessage(ConfigManager.MESSAGES_NO_PERMS.getStringFormatted());
                return true;
            }

            LocalStorage.getHolograms().forEach(Entity::remove);
            LocalStorage.getHolograms().clear();

            new HashMap<>(LocalStorage.getGraves()).forEach((uuid, graves) -> {
                new ArrayList<>(graves).forEach(grave -> {
                    GraveManager.removeGrave(grave.getPlayer(), grave);
                });
            });
            LocalStorage.getGraves().clear();

            sender.sendMessage(ColorUtils.colorize("&#17B5BD[&lGravewarden&#17B5BD] &aAll graves and holograms have been purged."));
            return true;
        }

        if (!sender.hasPermission("gravewarden.admin")) {
            Stream.of(
                            "&#17B5BD[&lGravewarden&#17B5BD] §7Running on version &#17B5BD" + Gravewarden.getInstance().getDescription().getVersion(),
                            "&#17B5BD[&lGravewarden&#17B5BD] §7Use &#17B5BD/grave help§7 to see all commands."
                    )
                    .map(ColorUtils::colorize)
                    .toList()
                    .forEach(sender::sendMessage);
            return true;
        }

        Gravewarden.getInstance().getUpdateChecker().isLatest()
                .thenAccept(isLatest -> Bukkit.getScheduler().runTask(Gravewarden.getInstance(), () -> Stream.of(
                                "&#17B5BD[&lGravewarden&#17B5BD] §7Running on version &#17B5BD" +
                                        Gravewarden.getInstance().getDescription().getVersion() + " " + (isLatest ? "&2(&2&l✓ &2Latest)" : "&4(&4&l✕ §4Outdated)"),
                                "&#17B5BD[&lGravewarden&#17B5BD] §7Use &#17B5BD/grave help§7 to see all commands."
                        )
                        .map(ColorUtils::colorize)
                        .toList()
                        .forEach(sender::sendMessage)));
        return false;
    }
}
