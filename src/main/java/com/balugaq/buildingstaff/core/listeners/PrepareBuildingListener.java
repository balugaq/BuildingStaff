package com.balugaq.buildingstaff.core.listeners;

import com.balugaq.buildingstaff.api.items.BuildingStaff;
import com.balugaq.buildingstaff.api.objects.events.PrepareBuildingEvent;
import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import com.balugaq.buildingstaff.utils.StaffUtil;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;

import java.util.Set;
import java.util.UUID;

public class PrepareBuildingListener implements Listener {
    private static final ModelCuboid blockBase = new ModelCuboid()
            .size(0.6F, 0.6F, 0.6F);
    private static final ModelCuboid border = new ModelCuboid()
            .material(Material.LIGHT_GRAY_STAINED_GLASS)
            .size(0.7F, 0.7F, 0.7F);

    @EventHandler
    public void onPrepareBuilding(PrepareBuildingEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("§aPreparing building blocks...");
        BuildingStaff buildingStaff = event.getBuildingStaff();
        showBuildingBlocksFor(player, event.getLookingAtBlock(), buildingStaff.getLimitBlocks(), event.getBuildingStaff());
    }

    private void showBuildingBlocksFor(Player player, Block lookingAtBlock, int limitBlocks, BuildingStaff buildingStaff) {
        if (!Slimefun.getProtectionManager().hasPermission(player, lookingAtBlock, Interaction.PLACE_BLOCK)) {
            return;
        }
        Material material = lookingAtBlock.getType();
        int playerHas = 0;
        if (player.getGameMode() == GameMode.CREATIVE) {
            playerHas = 4096;
        } else {
            for (ItemStack itemStack : player.getInventory().getStorageContents()) {
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    continue;
                }

                if (itemStack.getType() == material) {
                    int count = itemStack.getAmount();
                    playerHas += count;
                }

                if (playerHas >= limitBlocks) {
                    break;
                }
            }
        }

        Set<Location> showingBlocks = StaffUtil.getBuildingLocations(player, Math.min(limitBlocks, playerHas), buildingStaff.getAxis(player.getInventory().getItemInMainHand()), buildingStaff.isBlockStrict());
        DisplayGroup displayGroup = new DisplayGroup(player.getLocation(), 0.0F, 0.0F);
        for (Location location : showingBlocks) {
            String ls = location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
            Location displayLocation = location.clone().add(0.5, 0.5, 0.5);
            displayGroup.addDisplay("m" + ls, blockBase.material(material).build(displayLocation));
            displayGroup.addDisplay("b" + ls, border.build(displayLocation));
        }


        UUID uuid = player.getUniqueId();

        BuildingStaffPlugin.getInstance().getDisplayManager().registerDisplayGroup(uuid, displayGroup);
    }
}
