package com.balugaq.buildingstaff.core.commands.list;

import com.balugaq.buildingstaff.core.commands.SubCommand;
import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import com.balugaq.buildingstaff.utils.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SubCommand {
    public static final String IDENTIFIER = "reload";

    public ReloadCommand(BuildingStaffPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        BuildingStaffPlugin plugin = getPlugin();
        if (plugin.isEnabled()) {
            plugin.reload();
            sender.sendMessage(Lang.getCommandMessage("reload", "success"));
        }

        return true;
    }
}
