package com.balugaq.buildingstaff.api.items;

import com.balugaq.buildingstaff.implementation.BuildingStaff;
import com.balugaq.buildingstaff.utils.StaffUtil;
import com.balugaq.buildingstaff.utils.WorldUtils;
import com.destroystokyo.paper.MaterialTags;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class Staff extends SlimefunItem {
    private final int limitBlocks;

    public Staff(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int limitBlocks) {
        super(itemGroup, item, recipeType, recipe);
        this.limitBlocks = limitBlocks;
    }

    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler((ItemUseHandler) playerRightClickEvent -> {
            if (playerRightClickEvent.getInteractEvent().getHand() != EquipmentSlot.HAND) {
                return;
            }

            Player player = playerRightClickEvent.getPlayer();
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

            int playerHas = 0;
            if (player.getGameMode() == GameMode.CREATIVE) {
                playerHas = 4096;
            } else {
                ItemStack target = new ItemStack(material, 1);
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack == null || itemStack.getType() == Material.AIR) {
                        continue;
                    }

                    if (SlimefunUtils.isItemSimilar(itemStack, target, true, false)) {
                        int count = itemStack.getAmount();
                        playerHas += count;
                    }

                    if (playerHas >= limitBlocks) {
                        break;
                    }
                }
            }

            if (playerHas <= 0) {
                return;
            }

            BlockFace originalFacing = player.getTargetBlockFace(6, FluidCollisionMode.NEVER);
            if (originalFacing == null) {
                return;
            }

            BlockFace lookingFacing = getBlockFaceAsCartesian(originalFacing);

            ItemStack itemInHand = new ItemStack(material, 1);
            Set<Location> buildingLocations = StaffUtil.getBuildingLocations(player, Math.min(limitBlocks, playerHas));

            int consumed = 0;

            Set<Block> blocks = new HashSet<>();
            for (Location location : buildingLocations) {
                Block block = location.getBlock();
                if (block.getType() == Material.AIR || block.getType() == Material.WATER || block.getType() == Material.LAVA) {
                    BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(
                            block,
                            block.getState(),
                            block.getRelative(lookingFacing.getOppositeFace()),
                            itemInHand,
                            player,
                            Slimefun.getProtectionManager().hasPermission(player, block, Interaction.PLACE_BLOCK),
                            EquipmentSlot.HAND
                    );
                    Bukkit.getPluginManager().callEvent(blockPlaceEvent);
                    if (!blockPlaceEvent.isCancelled()) {
                        blocks.add(block);
                    }
                    consumed += 1;
                }
            }

            Bukkit.getScheduler().runTaskLater(BuildingStaff.getInstance(), () -> {
                for (Block block : blocks) {
                    if (copyStateAble(material)) {
                        WorldUtils.copyBlockState(lookingAtBlock.getState(), block);
                    } else {
                        block.setType(material);
                    }
                    block.getState().update(true, true);
                }
            }, 1);

            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            if (consumed > 0) {
                player.getInventory().removeItem(new ItemStack(material, consumed));
            }
        });
    }

    @NotNull
    private static BlockFace getBlockFaceAsCartesian(BlockFace originalFacing) {
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

    public boolean copyStateAble(Material material) {
        return // Items that be allowed to copy state
                MaterialTags.FENCE_GATES.isTagged(material)
                || material.name().endsWith("_SLAB")
                || material.name().endsWith("_STAIRS")
                || material.name().endsWith("_TRAPDOOR")
                || material.name().endsWith("_BUTTON")
                || material.name().endsWith("_BANNER")
                || material.name().endsWith("_RAIL")
                || material.name().endsWith("_HEAD")
                || material.name().endsWith("_CORAL")
                || material.name().endsWith("_CORAL_FAN")
                || material.name().endsWith("_LOG")
                || material == Material.STRING
                || material == Material.END_ROD
                || material == Material.LIGHTNING_ROD
                || material == Material.CHAIN
                || material == Material.LEVER
                || material == Material.TORCH
                || material == Material.REDSTONE_TORCH
                || material == Material.SOUL_TORCH
                || material == Material.LANTERN
                || material == Material.SOUL_LANTERN
                || material == Material.LADDER
                || material == Material.REPEATER
                || material == Material.COMPARATOR
                || material == Material.PISTON
                || material == Material.STICKY_PISTON
                || material == Material.VINE
                || material == Material.GLOW_LICHEN;
    }

    public boolean isDisabledMaterial(Material material) {
        if (
                // Items that can store items
                MaterialTags.SHULKER_BOXES.isTagged(material)
                || material == Material.CHEST
                || material == Material.TRAPPED_CHEST
                || material == Material.BARREL
                || material == Material.LECTERN
                || material == Material.DISPENSER
                || material == Material.DROPPER
                || material == Material.HOPPER
                || material == materialValueOf("VAULT")

                // Items that will take two blocks
                || MaterialTags.BEDS.isTagged(material)
                || MaterialTags.DOORS.isTagged(material)
                || material == Material.TALL_GRASS
                || material == Material.LARGE_FERN
                || material == Material.TALL_SEAGRASS
                || material == Material.SUNFLOWER
                || material == Material.LILAC
                || material == Material.ROSE_BUSH
                || material == Material.PEONY
                || material == Material.PITCHER_PLANT

                // Items that can place much same block in a location
                || material == Material.CANDLE
                || material.name().endsWith("_CANDLE")
                || material == Material.SEA_PICKLE

                // Items that can be placed in a location
                || material.isAir()
                || !material.isBlock()

                // Items that is invalid
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
                || material == Material.CHORUS_FLOWER
                || material == Material.NETHER_WART

                // Items that has gui
                || material == Material.CRAFTING_TABLE
                || material == Material.STONECUTTER
                || material == Material.CARTOGRAPHY_TABLE
                || material == Material.FLETCHING_TABLE
                || material == Material.SMITHING_TABLE
                || material == Material.GRINDSTONE
                || material == Material.LOOM
                || material == Material.FURNACE
                || material == Material.SMOKER
                || material == Material.BLAST_FURNACE
                || material == Material.CAMPFIRE
                || material == Material.SOUL_CAMPFIRE
                || material == Material.ANVIL
                || material == Material.CHIPPED_ANVIL
                || material == Material.DAMAGED_ANVIL
                || material == Material.COMPOSTER
                || material == Material.JUKEBOX
                || material == Material.ENCHANTING_TABLE
                || material == Material.BREWING_STAND
                || material == Material.CAULDRON
                || material == Material.BEACON
                || material == Material.BEE_NEST
                || material == Material.BEEHIVE
                || material == Material.FLOWER_POT
                || material == Material.DECORATED_POT
                || material == Material.CHISELED_BOOKSHELF
                || MaterialTags.SIGNS.isTagged(material)

                // Items that have different types
                || material == Material.PLAYER_HEAD
                || material == Material.PLAYER_WALL_HEAD
                || material == Material.CAKE
                || material.name().endsWith("_CAKE")
                || material == Material.POINTED_DRIPSTONE
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
