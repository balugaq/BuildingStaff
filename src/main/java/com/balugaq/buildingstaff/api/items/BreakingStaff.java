package com.balugaq.buildingstaff.api.items;

import com.balugaq.buildingstaff.utils.StaffUtil;
import com.destroystokyo.paper.MaterialTags;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public abstract class BreakingStaff extends SlimefunItem implements Staff {
    private final int limitBlocks;
    private final boolean blockStrict;
    private final boolean opOnly;

    public BreakingStaff(@NotNull ItemGroup itemGroup, @NotNull SlimefunItemStack item, @NotNull RecipeType recipeType, ItemStack @NotNull [] recipe, int limitBlocks, boolean blockStrict, boolean opOnly) {
        super(itemGroup, item, recipeType, recipe);
        this.limitBlocks = limitBlocks;
        this.blockStrict = blockStrict;
        this.opOnly = opOnly;
    }

    @NotNull
    private static BlockFace getBlockFaceAsCartesian(@NotNull BlockFace originalFacing) {
        // Seems here's a bug, but it works fine...
        BlockFace lookingFacing = originalFacing.getOppositeFace();
        if (!originalFacing.isCartesian()) {
            switch (originalFacing) {
                case NORTH_EAST, NORTH_WEST, NORTH_NORTH_EAST, NORTH_NORTH_WEST -> lookingFacing = BlockFace.NORTH;
                case SOUTH_EAST, SOUTH_WEST, SOUTH_SOUTH_EAST, SOUTH_SOUTH_WEST -> lookingFacing = BlockFace.SOUTH;
                case EAST_NORTH_EAST, EAST_SOUTH_EAST -> lookingFacing = BlockFace.EAST;
                case WEST_NORTH_WEST, WEST_SOUTH_WEST -> lookingFacing = BlockFace.WEST;
                default -> {
                }
            }
        }
        return lookingFacing;
    }

    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler((ItemUseHandler) playerRightClickEvent -> {
            if (playerRightClickEvent.getInteractEvent().getHand() != EquipmentSlot.HAND) {
                return;
            }

            Player player = playerRightClickEvent.getPlayer();
            if (opOnly && !player.isOp()) {
                return;
            }

            if (player.getGameMode() == GameMode.SPECTATOR) {
                return;
            }

            if (isDisabledIn(player.getWorld())) {
                return;
            }

            Block lookingAtBlock = player.getTargetBlockExact(6, FluidCollisionMode.NEVER);
            if (lookingAtBlock == null || lookingAtBlock.getType() == Material.AIR) {
                return;
            }

            Material material = lookingAtBlock.getType();
            if (isDisabledMaterial(material)) {
                return;
            }

            BlockFace originalFacing = player.getTargetBlockFace(6, FluidCollisionMode.NEVER);
            if (originalFacing == null) {
                return;
            }

            Location lookingLocation = lookingAtBlock.getLocation();
            BlockFace lookingFacing = getBlockFaceAsCartesian(originalFacing);
            ItemStack item = player.getInventory().getItemInMainHand();

            Set<Location> rawLocations = StaffUtil.getRawLocations(lookingAtBlock, lookingFacing, limitBlocks, getAxis(item), blockStrict, true);

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
            List<Location> sortedLocations = locations.stream().sorted(Comparator.comparingDouble(distances::get)).toList();
            Set<Location> result = new HashSet<>();
            AtomicInteger count = new AtomicInteger(0);
            sortedLocations.forEach(location -> {
                if (count.incrementAndGet() > limitBlocks) {
                    return;
                }
                result.add(location);
            });

            Set<BlockBreakEvent> locationsToBreak = new HashSet<>();
            for (Location location : result) {
                if (!Slimefun.getProtectionManager().hasPermission(player, location, Interaction.BREAK_BLOCK)) {
                    continue;
                }

                BlockBreakEvent event = new BlockBreakEvent(location.getBlock(), player);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    locationsToBreak.add(event);
                }
            }

            // I don't know why, but it must be run later, or it will create PlayerInteractEvent AGAIN!
            Bukkit.getScheduler().runTaskLater(getAddon().getJavaPlugin(), () -> {
                for (BlockBreakEvent event : locationsToBreak) {
                    if (event.isCancelled()) {
                        continue;
                    }

                    Block block = event.getBlock();
                    if (block == null) {
                        continue;
                    }

                    if (event.isDropItems()) {
                        block.breakNaturally();
                    } else {
                        block.setType(Material.AIR);
                    }

                    BlockState state = block.getState();
                    state.update(true, true);
                }
            }, 1);
        });
    }

    public boolean isDisabledMaterial(@NotNull Material material) {
        if (
            material.isAir()
            || !material.isBlock()

            || material == Material.END_PORTAL_FRAME
            || material == Material.BEDROCK
            || material == Material.COMMAND_BLOCK
            || material == Material.CHAIN_COMMAND_BLOCK
            || material == Material.REPEATING_COMMAND_BLOCK
            || material == Material.STRUCTURE_VOID
            || material == Material.STRUCTURE_BLOCK
            || material == Material.JIGSAW
            || material == Material.BARRIER
            || material == Material.LIGHT
            || material == Material.SPAWNER
            || material == materialValueOf("TRIAL_SPAWNER")
            || material.getHardness() < 0
        ) {
            return true;
        }

        return false;
    }

    @NotNull
    private Material materialValueOf(String name) {
        try {
            return Material.valueOf(name);
        } catch (IllegalArgumentException | NullPointerException e) {
            return Material.AIR;
        }
    }
}
