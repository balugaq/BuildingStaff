package com.balugaq.buildingstaff.core.managers;

import com.balugaq.buildingstaff.api.interfaces.IManager;
import com.balugaq.buildingstaff.core.listeners.PlayerInteractListener;
import com.balugaq.buildingstaff.core.listeners.PrepareBreakingListener;
import com.balugaq.buildingstaff.core.listeners.PrepareBuildingListener;
import com.balugaq.buildingstaff.core.listeners.StaffModeSwitchListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager implements IManager {
    private final JavaPlugin plugin;
    private final List<Listener> listeners = new ArrayList<>();

    public ListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setup() {
        listeners.add(new PrepareBuildingListener());
        listeners.add(new PrepareBreakingListener());
        listeners.add(new PlayerInteractListener());
        listeners.add(new StaffModeSwitchListener());
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    @Override
    public void shutdown() {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }

        listeners.clear();
    }
}
