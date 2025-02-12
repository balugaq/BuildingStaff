package com.balugaq.buildingstaff.core.listeners;

import com.balugaq.buildingstaff.api.items.Staff;
import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import com.balugaq.buildingstaff.utils.Lang;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class StaffModeSwitchListener implements Listener {
    @EventHandler
    public void onStaffModeSwitch(@NotNull PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInOffHand = event.getOffHandItem();
        SlimefunItem staffLike = SlimefunItem.getByItem(itemInOffHand);
        if (staffLike instanceof Staff staff) {
            Axis axis = staff.getAxis(itemInOffHand);
            Axis nextAxis;
            if (axis == null) {
                nextAxis = Axis.X;
            } else {
                switch (axis) {
                    case X -> nextAxis = Axis.Y;
                    case Y -> nextAxis = Axis.Z;
                    case Z -> nextAxis = null;
                    default -> nextAxis = null;
                }
            }

            staff.setAxis(itemInOffHand, nextAxis);
            ItemMeta meta = itemInOffHand.getItemMeta();
            if (meta == null) {
                return;
            }

            List<String> defaultLore = staffLike.getItem().getItemMeta().getLore();
            if (defaultLore == null) {
                return;
            }

            List<String> lore = new ArrayList<>(defaultLore);
            lore.add(Lang.getMessage("axis-strict-lore", "axis", nextAxis == null ? "None" : nextAxis.name()));
            meta.setLore(lore);
            itemInOffHand.setItemMeta(meta);

            player.getInventory().setItemInMainHand(itemInOffHand);
            event.setCancelled(true);
            BuildingStaffPlugin.getInstance().getDisplayManager().killDisplays(player.getUniqueId());
            player.sendMessage(Lang.getMessage("axis-strict-message", "axis", nextAxis == null ? "None" : nextAxis.name()));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        }
    }
}