package com.balugaq.buildingstaff.implementation;

import com.balugaq.buildingstaff.core.managers.ConfigManager;
import com.balugaq.buildingstaff.core.managers.DisplayManager;
import com.balugaq.buildingstaff.core.managers.ListenerManager;
import com.balugaq.buildingstaff.utils.Debug;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;


public class BuildingStaffPlugin extends JavaPlugin implements SlimefunAddon {
    private static BuildingStaffPlugin instance;
    private @Getter ConfigManager configManager;
    private @Getter DisplayManager displayManager;
    private @Getter ListenerManager listenerManager;
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
        Debug.log("正在启动 BuildingStaff...");
        this.username = "balugaq";
        this.repo = "BuildingStaff";
        this.branch = "master";

        configManager = new ConfigManager(this);
        configManager.loadConfig();

        displayManager = new DisplayManager(this);
        displayManager.startShowBlockTask();

        listenerManager = new ListenerManager(this);
        listenerManager.setup();

        if (getServer().getPluginManager().isPluginEnabled("GuizhanLibPlugin")) {
            tryUpdate();
        }

        StaffSetup.setup(this);
        Debug.log("BuildingStaff 启动成功!");
    }

    public void reload() {
        onDisable();
        onEnable();
    }

    @Override
    public void onDisable() {
        displayManager.stopShowBlockTask();
        StaffSetup.unregister(this);
        Debug.log("BuildingStaff 已停止工作!");
    }

    public void tryUpdate() {
        Debug.log("正在尝试自动更新...");
        try {
            if (configManager.isAutoUpdate() && getDescription().getVersion().startsWith("Build")) {
                GuizhanUpdater.start(this, getFile(), username, repo, branch);
            }
        } catch (NoClassDefFoundError | NullPointerException | UnsupportedClassVersionError e) {
            Debug.log("自动更新失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

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