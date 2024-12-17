package com.balugaq.buildingstaff.core.listeners;

import com.balugaq.buildingstaff.api.items.Staff;
import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * When a player hold a staff in his main hand and hold a block in his offhand,
 * the event is cancelled to prevent the player from placing blocks with the staff.
 * I don't know why {@link BlockPlaceEvent} or {@link Block#setType(Material)}
 * will make player interact with main hand again.
 * So this listener is important for playing building staff.
 *
 * @author balugaq
 */
public class PlayerInteractListener implements Listener {
    private static final boolean DEBUG = BuildingStaffPlugin.getInstance().getConfigManager().isDebug();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
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
