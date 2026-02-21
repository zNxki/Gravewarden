package dev.znxki.gravewarden.utils;

import org.bukkit.entity.Player;

public class ExperienceUtil {
    public static int getTotalExperience(Player player) {
        int level = player.getLevel(), res = 0;
        float exp = player.getExp();

        for (int i = 0; i < level; i++) {
            if (i >= 30) {
                res += 112 + (i - 30) * 9;
                continue;
            }

            if (i >= 15) {
                res += 37 + (i - 15) * 9;
                continue;
            }

            res += 7 + i * 2;
        }

        res += (int) (exp * player.getExpToLevel());
        return res;
    }
}
