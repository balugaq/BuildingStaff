package com.balugaq.buildingstaff.core.managers;

import com.balugaq.buildingstaff.api.interfaces.IManager;
import com.balugaq.buildingstaff.core.commands.SubCommand;
import com.balugaq.buildingstaff.core.commands.list.ClearProjectileCommand;
import com.balugaq.buildingstaff.core.commands.list.ReloadCommand;
import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import com.balugaq.buildingstaff.utils.Lang;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandManager implements TabExecutor, IManager {
    private final BuildingStaffPlugin plugin;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(BuildingStaffPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setup() {
        subCommands.add(new ReloadCommand(plugin));
        subCommands.add(new ClearProjectileCommand(plugin));
        PluginCommand pluginCommand = plugin.getCommand("buildingstaff");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    @Override
    public void shutdown() {
        PluginCommand pluginCommand = plugin.getCommand("buildingstaff");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(null);
        }

        subCommands.clear();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Lang.getCommandMessage("no-permission"));
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(Lang.getCommandMessage("usage"));
            return false;
        }

        for (SubCommand subCommand : subCommands) {
            if (subCommand.getIdentifier().equalsIgnoreCase(args[0])) {
                if (subCommand.onCommand(sender, command, label, args)) {
                    return true;
                }
            }
        }

        sender.sendMessage(Lang.getCommandMessage("not-found"));
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            return null;
        }

        List<String> completions = new ArrayList<>();

        if (args.length == 0) {
            return completions;
        } else if (args.length == 1) {
            for (SubCommand subCommand : subCommands) {
                completions.add(subCommand.getIdentifier());
            }
        } else {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getIdentifier().equalsIgnoreCase(args[0])) {
                    completions.addAll(subCommand.onTabComplete(sender, command, label, args));
                }
            }
        }

        return completions;
    }
}
