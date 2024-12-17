package com.balugaq.buildingstaff.core.listeners;

import com.balugaq.buildingstaff.api.items.Staff;
import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    private static final boolean DEBUG = BuildingStaffPlugin.getInstance().getConfigManager().isDebug();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (DEBUG) {
            player.sendMessage("onPlayerInteract");
        }
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        SlimefunItem sfItem = SlimefunItem.getByItem(itemInMainHand);
        if (sfItem instanceof Staff) {
            ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
            if (itemInOffHand != null && itemInOffHand.getType() != Material.AIR && itemInOffHand.getType().isBlock()) {
                event.setCancelled(true);
            }
        }
    }
}
