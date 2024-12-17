package com.balugaq.buildingstaff.utils;

import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Debug {
    private static final JavaPlugin plugin = BuildingStaffPlugin.getInstance();
    private static final String debugPrefix = "[Debug] ";

    public static void debug(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objects) {
            if (obj == null) {
                sb.append("null");
            } else {
                sb.append(obj);
            }
        }
        debug(sb.toString());
    }

    public static void debug(Object object) {
        debug(object.toString());
    }

    public static void debug(String... messages) {
        for (String message : messages) {
            debug(message);
        }
    }

    public static void debug(String message) {
        if (BuildingStaffPlugin.getInstance().getConfigManager().isDebug()) {
            log(debugPrefix + message);
        }
    }

    public static void sendMessage(Player player, Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objects) {
            if (obj == null) {
                sb.append("null");
            } else {
                sb.append(obj);
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
            Debug.log(e);
        }
    }

    public static void log(Object... object) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : object) {
            if (obj == null) {
                sb.append("null");
            } else {
                sb.append(obj);
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

    public static void log(Throwable e) {
        e.printStackTrace();
    }

    public static void log() {
        log("");
    }
}
