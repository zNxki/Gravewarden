package dev.znxki.gravewarden.config.updater;

import dev.znxki.gravewarden.Gravewarden;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class ConfigUpdater {
    public static void updateConfig() {
        Gravewarden plugin = Gravewarden.getInstance();
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        InputStream is = plugin.getResource("config.yml");
        if (is == null) return;

        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is, StandardCharsets.UTF_8));
        boolean changed = false;

        for (String key : defaultConfig.getKeys(true)) {
            if (!config.contains(key)) {
                config.set(key, defaultConfig.get(key));
                changed = true;
            }
        }

        if (changed) {
            try {
                config.save(configFile);
                plugin.reloadConfig();
                plugin.getLogger().log(Level.INFO, "Config.yml updated.");
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error saving config", e);
            }
        }
    }
}