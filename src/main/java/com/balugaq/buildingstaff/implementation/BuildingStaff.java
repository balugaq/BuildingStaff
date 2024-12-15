package com.balugaq.buildingstaff.implementation;

import com.balugaq.buildingstaff.core.managers.DisplayManager;
import com.balugaq.buildingstaff.core.managers.ListenerManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public class BuildingStaff extends JavaPlugin implements SlimefunAddon {
    private static BuildingStaff instance;
    private @Getter DisplayManager displayManager;
    private @Getter ListenerManager listenerManager;

    public static BuildingStaff getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading BuildingStaff...");

        displayManager = new DisplayManager(this);
        displayManager.startShowBlockTask();

        listenerManager = new ListenerManager(this);
        listenerManager.setup();

        BuildingStaffSetup.setup(this);
        getLogger().info("BuildingStaff has been enabled.");
    }

    public void reload() {
        onDisable();
        onEnable();
    }

    @Override
    public void onDisable() {
        displayManager.stopShowBlockTask();
        BuildingStaffSetup.unregister(this);
        getLogger().info("BuildingStaff has been disabled.");
    }

    @Override
    @NotNull
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/balugaq/BuildingStaff/issues";
    }
}