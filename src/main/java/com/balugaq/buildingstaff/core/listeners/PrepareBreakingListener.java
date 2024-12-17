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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PrepareBreakingListener implements Listener {
    private static final boolean DEBUG = BuildingStaffPlugin.getInstance().getConfigManager().isDebug();
    private static final boolean DISPLAY_PROJECTION = BuildingStaffPlugin.getInstance().getConfigManager().isDisplayProjection();

    private static final ModelCuboid border = new ModelCuboid()
            .material(Material.RED_STAINED_GLASS)
            .size(0.9F, 0.9F, 0.9F);

    @EventHandler
    public void onPrepareBreaking(PrepareBreakingEvent event) {
        if (!DISPLAY_PROJECTION) {
            return;
        }

        Player player = event.getPlayer();
        if (DEBUG) {
            player.sendMessage("§cPreparing breaking blocks...");
        }
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

        Location lookingLocation = lookingAtBlock.getLocation();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        BlockFace lookingFacing = StaffUtil.getLookingFacing(originalFacing);

        Set<Location> rawLocations = StaffUtil.getRawLocations(
                lookingAtBlock,
                lookingFacing,
                limitBlocks,
                breakingStaff.getAxis(itemInMainHand),
                breakingStaff.isBlockStrict(),
                true
        );

        World world = lookingLocation.getWorld();
        Map<Location, Double> distances = new HashMap<>();
        for (Location location : rawLocations) {
            if (world.getWorldBorder().isInside(location)) {
                double distance = location.distance(lookingLocation);
                distances.put(location, distance);
            }
        }

        // sort by shortest distance
        Set<Location> locations = new HashSet<>(distances.keySet());
        List<Location> sortedLocations = locations
                .stream()
                .sorted(Comparator.comparingDouble(distances::get))
                .limit(limitBlocks)
                .toList();

        Vector vector = lookingFacing.getOppositeFace().getDirection().multiply(0.6).add(new Vector(0.5F, 0.5F, 0.5F));
        DisplayGroup displayGroup = new DisplayGroup(player.getLocation(), 0.0F, 0.0F);
        for (Location location : sortedLocations) {
            String ls = location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
            Location displayLocation = location.clone().add(vector);
            displayGroup.addDisplay("b" + ls, border.build(displayLocation));
        }

        UUID uuid = player.getUniqueId();

        BuildingStaffPlugin.getInstance().getDisplayManager().registerDisplayGroup(uuid, displayGroup);
    }
}
