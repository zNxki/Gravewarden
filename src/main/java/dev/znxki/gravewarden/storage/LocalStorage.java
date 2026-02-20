package dev.znxki.gravewarden.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.znxki.gravewarden.objects.Grave;
import lombok.Getter;
import org.bukkit.entity.ArmorStand;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LocalStorage {
    @Getter
    private static final List<ArmorStand> holograms = Lists.newArrayList();

    @Getter
    private static final Map<UUID, List<Grave>> graves = Maps.newHashMap();
}
