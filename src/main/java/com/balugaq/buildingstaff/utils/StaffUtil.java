package com.balugaq.buildingstaff.utils;

import lombok.Getter;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class StaffUtil {
    public static final Set<BlockFace> validFaces = new HashSet<>();

    static {
        validFaces.add(BlockFace.NORTH);
        validFaces.add(BlockFace.SOUTH);
        validFaces.add(BlockFace.EAST);
        validFaces.add(BlockFace.WEST);
        validFaces.add(BlockFace.UP);
        validFaces.add(BlockFace.DOWN);
    }

    public static Set<Location> getBuildingLocations(Player player, int limitBlocks) {
        if (limitBlocks <= 0) {
            return new HashSet<>();
        }

        Block lookingBlock = player.getTargetBlockExact(6, FluidCollisionMode.NEVER);
        if (lookingBlock == null || lookingBlock.getType().isAir()) {
            return new HashSet<>();
        }

        BlockFace originalFacing = player.getTargetBlockFace(6, FluidCollisionMode.NEVER);
        if (originalFacing == null) {
            return new HashSet<>();
        }
        BlockFace lookingFacing = originalFacing.getOppositeFace();
        if (!originalFacing.isCartesian()) {
            switch (originalFacing) {
                case NORTH_EAST, NORTH_WEST, NORTH_NORTH_EAST, NORTH_NORTH_WEST -> {
                    lookingFacing = BlockFace.NORTH;
                }
                case SOUTH_EAST, SOUTH_WEST, SOUTH_SOUTH_EAST, SOUTH_SOUTH_WEST -> {
                    lookingFacing = BlockFace.SOUTH;
                }
                case EAST_NORTH_EAST, EAST_SOUTH_EAST -> {
                    lookingFacing = BlockFace.EAST;
                }
                case WEST_NORTH_WEST, WEST_SOUTH_WEST -> {
                    lookingFacing = BlockFace.WEST;
                }
                default -> {
                    return new HashSet<>();
                }
            }
        }

        return getLocations(lookingBlock, lookingFacing, limitBlocks);
    }

    private static Set<Location> getLocations(Block lookingBlock, BlockFace lookingFacing, int limitBlocks) {
        Set<Location> rawLocations = getRawLocations(lookingBlock, lookingFacing, limitBlocks);
        Set<Location> outwardLocations = new HashSet<>();
        for (Location location : rawLocations) {
            Location outwardLocation = location.clone().add(lookingFacing.getOppositeFace().getDirection());
            Block outwardBlock = outwardLocation.getBlock();
            Material outwardType = outwardBlock.getType();
            if (outwardType == Material.AIR || outwardType == Material.WATER || outwardType == Material.LAVA) {
                outwardLocations.add(outwardLocation);
            }
        }
        Location lookingLocation = lookingBlock.getLocation();

        World world = lookingLocation.getWorld();
        Map<Location, Double> distances = new HashMap<>();
        for (Location location : outwardLocations) {
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

        return result;
    }

    private static Set<Location> getRawLocations(Block lookingBlock, BlockFace lookingFacing, int limitBlocks) {
        Set<Location> locations = new HashSet<>();
        Queue<Location> queue = new LinkedList<>();
        Location lookingLocation = lookingBlock.getLocation();
        queue.offer(lookingLocation);

        while (!queue.isEmpty() && limitBlocks > 0) {
            Block currentBlock = queue.poll().getBlock();
            Material type = currentBlock.getType();
            if (type.isAir()) {
                continue;
            }

            Location queuedLocation = currentBlock.getLocation();
            if (!locations.contains(queuedLocation)) {
                locations.add(queuedLocation);

                for (BlockFace face : validFaces) {
                    if (face == lookingFacing || face == lookingFacing.getOppositeFace()) {
                        continue;
                    }

                    Block block = currentBlock.getRelative(face);
                    if (block.getType() == type) {
                        Location location = block.getLocation();
                        if (!locations.contains(location)) {
                            Block outwardBlock = block.getRelative(lookingFacing.getOppositeFace());
                            Material outwardType = outwardBlock.getType();
                            if (outwardType == Material.AIR || outwardType == Material.WATER || outwardType == Material.LAVA) {
                                Location blockLocation = block.getLocation();
                                if (manhattanDistance(lookingLocation, blockLocation) < limitBlocks) {
                                    queue.offer(blockLocation);
                                }
                            }
                        }
                    }
                }
            }
        }

        return locations;
    }

    private static int manhattanDistance(Location a, Location b) {
        int dx = Math.abs(a.getBlockX() - b.getBlockX());
        int dy = Math.abs(a.getBlockY() - b.getBlockY());
        int dz = Math.abs(a.getBlockZ() - b.getBlockZ());
        return dx + dy + dz;
    }
}
