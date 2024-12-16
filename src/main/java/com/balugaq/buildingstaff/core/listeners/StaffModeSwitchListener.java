package com.balugaq.buildingstaff.core.listeners;

import com.balugaq.buildingstaff.api.items.BuildingStaff;
import com.balugaq.buildingstaff.api.items.Staff;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Axis;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class StaffModeSwitchListener implements Listener {
    @EventHandler
    public void onStaffModeSwitch(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInMainHand = event.getMainHandItem();
        if (SlimefunItem.getByItem(itemInMainHand) instanceof Staff staff) {
            Axis axis = staff.getAxis(itemInMainHand);
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

            staff.setAxis(itemInMainHand, nextAxis);
            event.setCancelled(true);
            player.sendMessage("Staff in main hand has been switched to " + (nextAxis == null? "null" : nextAxis) + " mode.");
        }

        ItemStack itemInOffHand = event.getOffHandItem();
        if (SlimefunItem.getByItem(itemInOffHand) instanceof Staff staff) {
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
            event.setCancelled(true);
            player.sendMessage("Staff in off hand has been switched to " + (nextAxis == null? "null" : nextAxis) + " mode.");
        }
    }
}