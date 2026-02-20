package dev.znxki.gravewarden;

import dev.znxki.gravewarden.events.DeathListener;
import dev.znxki.gravewarden.events.GraveInteractListener;
import dev.znxki.gravewarden.manager.GraveManager;
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
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new GraveInteractListener(), this);

    }

    @Override
    public void onDisable() {
        LocalStorage.getGraves().forEach((uuid, graves) -> graves.forEach(grave -> GraveManager.removeGrave(grave.getPlayer(), grave)));
        LocalStorage.getHolograms().forEach(Entity::remove);
    }
}
