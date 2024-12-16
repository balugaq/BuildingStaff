package com.balugaq.buildingstaff.utils;

import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Debug {
    private static final JavaPlugin plugin = BuildingStaffPlugin.getInstance();

    public static void sendMessage(Player player, Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objects) {
            if (obj == null) {
                sb.append("null");
            } else {
                sb.append(obj.toString());
            }
        }
        sendMessage(player, sb.toString());
    }
    public static void sendMessage(Player player, Object object) {
        if (object == null) {
            sendMessage(player, "null");
            return;
        }
        sendMessage(player, object.toString());
    }

    public static void sendMessages(Player player, String... messages) {
        for (String message : messages) {
            sendMessage(player, message);
        }
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage("[" + plugin.getLogger().getName() + "]" + message);
    }

    public static void stackTraceManually() {
        try {
            throw new Error();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void log(Object... object) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : object) {
            if (obj == null) {
                sb.append("null");
            } else {
                sb.append(obj.toString());
            }
        }

        log(sb.toString());
    }

    public static void log(Object object) {
        log(object.toString());
    }

    public static void log(String... messages) {
        for (String message : messages) {
            log(message);
        }
    }

    public static void log(String message) {
        plugin.getLogger().info(message);
    }
}
