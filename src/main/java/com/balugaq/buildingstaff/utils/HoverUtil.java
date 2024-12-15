package com.balugaq.buildingstaff.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
@UtilityClass
public class HoverUtil {
    public static void send(@NotNull CommandSender sender, String display, String hover) {
        TextComponent msg = new TextComponent(display);
        msg.setUnderlined(true);
        msg.setItalic(true);
        msg.setColor(ChatColor.GRAY);
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        sender.spigot().sendMessage(msg);
    }
}
