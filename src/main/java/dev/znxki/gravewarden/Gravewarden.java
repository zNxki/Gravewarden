package dev.znxki.gravewarden;

import dev.znxki.gravewarden.config.ConfigUpdater;
import dev.znxki.gravewarden.events.DeathListener;
import dev.znxki.gravewarden.events.GraveInteractListener;
import dev.znxki.gravewarden.events.RespawnListener;
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

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigUpdater.updateConfig();

        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new GraveInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);

        if (getConfig().getBoolean("update-checker", true))
            new UpdateChecker("CvD7zFzg", getDescription().getVersion()).check();
    }

    @Override
    public void onDisable() {
        LocalStorage.getGraves().forEach((uuid, graves) -> graves.forEach(grave -> GraveManager.removeGrave(grave.getPlayer(), grave)));
        LocalStorage.getHolograms().forEach(Entity::remove);
    }
}
