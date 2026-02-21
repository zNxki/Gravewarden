package dev.znxki.gravewarden.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import dev.znxki.gravewarden.Gravewarden;
import lombok.AllArgsConstructor;

import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@AllArgsConstructor
public class UpdateChecker {
    private final String projectId;
    private final String currentVersion;

    public void check() {
        Bukkit.getScheduler().runTaskAsynchronously(Gravewarden.getInstance(), () -> {
            try {
                URL url = new URL("https://api.modrinth.com/v2/project/" + projectId + "/version");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Gravewarden-UpdateChecker/1.0");

                if (connection.getResponseCode() != 200) {
                    Gravewarden.getInstance().getLogger()
                            .log(Level.SEVERE, "[Update Checker] Could not check for updates.");
                    return;
                }

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonArray versions = JsonParser.parseReader(reader).getAsJsonArray();

                if (versions.isEmpty()) {
                    Gravewarden.getInstance().getLogger()
                            .log(Level.SEVERE, "[Update Checker] Could not check for updates.");
                    return;
                }

                String latestVersion = versions.get(0).getAsJsonObject().get("version_number").getAsString();
                if (latestVersion.equalsIgnoreCase(currentVersion)) {
                    Gravewarden.getInstance().getLogger()
                            .log(Level.INFO, "[Update Checker] You are running on latest version.");
                    return;
                }

                Gravewarden.getInstance().getLogger()
                        .log(Level.WARNING, "[Update Checker] - A NEW UPDATE IS AVAILABLE");
                Gravewarden.getInstance().getLogger()
                        .log(Level.WARNING, "Current Version: " + currentVersion);
                Gravewarden.getInstance().getLogger()
                        .log(Level.WARNING, "Latest Version: " + latestVersion);
                Gravewarden.getInstance().getLogger()
                        .log(Level.WARNING, "Download it at: https://modrinth.com/plugin/" + projectId);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
