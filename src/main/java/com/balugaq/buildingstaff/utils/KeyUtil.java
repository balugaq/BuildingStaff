package com.balugaq.buildingstaff.utils;

import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@UtilityClass
public class KeyUtil {
    public static NamespacedKey AXIS = newKey("axis");
    public static @NotNull NamespacedKey newKey(@NotNull String key) {
        return new NamespacedKey(BuildingStaffPlugin.getInstance(), key);
    }

    public static @NotNull NamespacedKey newKey(@NotNull String pluginName, @NotNull String key) {
        return new NamespacedKey(pluginName, key);
    }

    public static @NotNull NamespacedKey newKey(@NotNull Plugin plugin, @NotNull String key) {
        return new NamespacedKey(plugin, key);
    }
}
