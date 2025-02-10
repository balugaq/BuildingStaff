package com.balugaq.buildingstaff.implementation;

import com.balugaq.buildingstaff.core.managers.CommandManager;
import com.balugaq.buildingstaff.core.managers.ConfigManager;
import com.balugaq.buildingstaff.core.managers.DisplayManager;
import com.balugaq.buildingstaff.core.managers.ListenerManager;
import com.balugaq.buildingstaff.core.managers.StaffSetup;
import com.balugaq.buildingstaff.utils.Debug;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import net.guizhanss.guizhanlibplugin.bstats.bukkit.Metrics;
import net.guizhanss.guizhanlibplugin.bstats.charts.SimplePie;
import net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

@SuppressWarnings({"unused", "deprecation"})
public class BuildingStaffPlugin extends JavaPlugin implements SlimefunAddon {
    private static BuildingStaffPlugin instance;
    private @Getter CommandManager commandManager;
    private @Getter ConfigManager configManager;
    private @Getter DisplayManager displayManager;
    private @Getter ListenerManager listenerManager;
    private @Getter StaffSetup staffSetup;
    private String username;
    private String repo;
    private String branch;

    public static BuildingStaffPlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Debug.log("Enabling BuildingStaff...");
        this.username = "balugaq";
        this.repo = "BuildingStaff";
        this.branch = "master";

        Debug.log("Loading config...");
        configManager = new ConfigManager(this);
        configManager.setup();

        Debug.log("Loading display manager...");
        displayManager = new DisplayManager(this);
        displayManager.setup();

        Debug.log("Loading listener manager...");
        listenerManager = new ListenerManager(this);
        listenerManager.setup();

        Debug.log("Loading command manager...");
        commandManager = new CommandManager(this);
        commandManager.setup();

        /* CN version updater
        if (getServer().getPluginManager().isPluginEnabled("GuizhanLibPlugin")) {
            Debug.log("Trying to update...");
            tryUpdate();
        }

         */

        Debug.log("Registering BuildingStaff Items...");
        staffSetup = new StaffSetup(this);
        staffSetup.setup();

        Debug.log("BuildingStaff enabled!");
    }

    public void reload() {
        onDisable();
        onEnable();
    }

    @Override
    public void onDisable() {
        Debug.log("正在卸载 BuildingStaff...");
        staffSetup.shutdown();
        displayManager.shutdown();
        listenerManager.shutdown();
        commandManager.shutdown();
        configManager.shutdown();
        Debug.log("BuildingStaff 已卸载!");
    }

    /* CN version updater
    public void tryUpdate() {
        try {
            if (configManager.isAutoUpdate() && getDescription().getVersion().startsWith("Build")) {
                GuizhanUpdater.start(this, getFile(), username, repo, branch);
            }
        } catch (NoClassDefFoundError | NullPointerException | UnsupportedClassVersionError e) {
            Debug.log("Failed to update: " + e.getMessage());
            Debug.log(e);
        }
    }

     */

    @Override
    @NotNull
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return MessageFormat.format("https://github.com/{0}/{1}/issues", username, repo);
    }
}