package dev.znxki.gravewarden.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.List;

public class GraveTabCompletion implements TabCompleter {
    private static final List<String> SUB_COMMANDS = Arrays.asList("list", "remove", "remove-all", "help");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (!(sender instanceof Player player)) return List.of();
        ImmutableList.Builder<String> completions = ImmutableList.builder();

        if (args.length == 1) {
            SUB_COMMANDS.forEach(sub -> {
                if (sub.toLowerCase().startsWith(args[0].toLowerCase()))
                    completions.add(sub);
            });

            return completions.build();
        }

        if (args.length == 2) {
            Bukkit.getOnlinePlayers().forEach(other -> {
                if (other.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    completions.add(other.getName());
            });

            return completions.build();
        }

        return List.of();
    }
}
