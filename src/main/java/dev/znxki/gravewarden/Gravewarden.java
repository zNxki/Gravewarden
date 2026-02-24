package dev.znxki.gravewarden;

import dev.znxki.gravewarden.commands.GraveCommand;
import dev.znxki.gravewarden.commands.GraveTabCompletion;
import dev.znxki.gravewarden.config.updater.ConfigUpdater;
import dev.znxki.gravewarden.events.*;
import dev.znxki.gravewarden.manager.GraveManager;
import dev.znxki.gravewarden.request.UpdateChecker;
import dev.znxki.gravewarden.storage.LocalStorage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public final class Gravewarden extends JavaPlugin {
    @Getter
    private static Gravewarden instance;

    @Getter
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        ConfigUpdater.updateConfig();
        reloadConfig();

        updateChecker = new UpdateChecker("CvD7zFzg", getDescription().getVersion());

        getCommand("gravewarden").setExecutor(new GraveCommand());
        getCommand("gravewarden").setTabCompleter(new GraveTabCompletion());

        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new GraveInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new DropCompassListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new GraveProtectionListener(), this);

        if (getConfig().getBoolean("update-checker", true))
            updateChecker.check();
    }

    @Override
    public void onDisable() {
        LocalStorage.getGraves().forEach((uuid, graves) -> graves.forEach(grave -> GraveManager.removeGrave(grave.getPlayer(), grave)));
        LocalStorage.getHolograms().forEach(Entity::remove);
    }
}
