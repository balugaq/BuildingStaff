package com.balugaq.buildingstaff.core.managers;

import com.balugaq.buildingstaff.core.listeners.PlayerInteractListener;
import com.balugaq.buildingstaff.core.listeners.PrepareBreakingListener;
import com.balugaq.buildingstaff.core.listeners.PrepareBuildingListener;
import com.balugaq.buildingstaff.core.listeners.StaffModeSwitchListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {
    private final JavaPlugin plugin;
    private final List<Listener> listeners = new ArrayList<>();

    public ListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;
        listeners.add(new PrepareBuildingListener());
        listeners.add(new PrepareBreakingListener());
        listeners.add(new PlayerInteractListener());
        listeners.add(new StaffModeSwitchListener());
    }

    public void setup() {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
