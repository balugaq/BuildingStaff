package com.balugaq.buildingstaff.core.listeners;

import com.balugaq.buildingstaff.api.items.BreakingStaff;
import com.balugaq.buildingstaff.api.items.BuildingStaff;
import com.balugaq.buildingstaff.api.objects.events.PrepareBreakingEvent;
import com.balugaq.buildingstaff.api.objects.events.PrepareBuildingEvent;
import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import com.balugaq.buildingstaff.utils.StaffUtil;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;

import java.util.Set;
import java.util.UUID;

public class PrepareBreakingListener implements Listener {
    private static final ModelCuboid blockBase = new ModelCuboid()
            .size(0.6F, 0.6F, 0.6F);
    private static final ModelCuboid border = new ModelCuboid()
            .material(Material.RED_STAINED_GLASS)
            .size(0.7F, 0.7F, 0.7F);

    @EventHandler
    public void onPrepareBuilding(PrepareBreakingEvent event) {
        Player player = event.getPlayer();
        BreakingStaff breakingStaff = event.getBreakingStaff();
        showBreakingBlocksFor(player, event.getLookingAtBlock(), breakingStaff.getLimitBlocks(), breakingStaff);
    }

    private void showBreakingBlocksFor(Player player, Block lookingAtBlock, int limitBlocks, BreakingStaff breakingStaff) {
        if (!Slimefun.getProtectionManager().hasPermission(player, lookingAtBlock, Interaction.BREAK_BLOCK)) {
            return;
        }

        BlockFace originalFacing = player.getTargetBlockFace(6, FluidCollisionMode.NEVER);
        if (originalFacing == null) {
            return;
        }

        Material material = lookingAtBlock.getType();
        if (breakingStaff.isDisabledMaterial(material)) {
            return;
        }

        Set<Location> locations = StaffUtil.getRawLocations(lookingAtBlock, StaffUtil.getLookingFacing(originalFacing), limitBlocks, breakingStaff.getAxis(player.getInventory().getItemInMainHand()), breakingStaff.isBlockStrict());
        DisplayGroup displayGroup = new DisplayGroup(player.getLocation(), 0.0F, 0.0F);
        for (Location location : locations) {
            String loc = location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
            displayGroup.addDisplay("m" + loc, blockBase.material(material).build(location.add(0.5F, 0.5F, 0.5F)));
            displayGroup.addDisplay("b" + loc, border.build(location.add(0.5F, 0.5F, 0.5F)));
        }

        UUID uuid = player.getUniqueId();

        BuildingStaffPlugin.getInstance().getDisplayManager().registerDisplayGroup(uuid, displayGroup);
    }
}
