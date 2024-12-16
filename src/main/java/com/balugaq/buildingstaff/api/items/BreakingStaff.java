package com.balugaq.buildingstaff.api.items;

import com.balugaq.buildingstaff.utils.KeyUtil;
import com.balugaq.buildingstaff.utils.PersistentUtil;
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
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class BreakingStaff extends SlimefunItem {
    private final int limitBlocks;
    private final boolean blockStrict;

    public BreakingStaff(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int limitBlocks, boolean blockStrict) {
        super(itemGroup, item, recipeType, recipe);
        this.limitBlocks = limitBlocks;
        this.blockStrict = blockStrict;
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

            BlockFace originalFacing = player.getTargetBlockFace(6, FluidCollisionMode.NEVER);
            if (originalFacing == null) {
                return;
            }

            BlockFace lookingFacing = getBlockFaceAsCartesian(originalFacing);
            ItemStack item = player.getInventory().getItemInMainHand();

            Set<Location> locations = StaffUtil.getRawLocations(lookingAtBlock, lookingFacing, limitBlocks, getAxis(item), blockStrict);

            for (Location location : locations) {
                if (!Slimefun.getProtectionManager().hasPermission(player, location, Interaction.BREAK_BLOCK)) {
                    continue;
                }

                BlockBreakEvent event = new BlockBreakEvent(location.getBlock(), player);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    location.getBlock().breakNaturally();
                }
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
                        || material == materialValueOf("CRAFTER")

                        // Items that have different types
                        || material == Material.PLAYER_HEAD
                        || material == Material.PLAYER_WALL_HEAD
                        || material == Material.CAKE
                        || material.name().endsWith("_CAKE")
                        || material == Material.POINTED_DRIPSTONE

                        // Haven't been divided into categories yet
                        || material == Material.LEVER
                        || material == Material.TORCH
                        || material == Material.REDSTONE_TORCH
                        || material == Material.SOUL_TORCH
                        || material == Material.LANTERN
                        || material == Material.SOUL_LANTERN
                        || material == Material.LADDER
                        || material == Material.REPEATER
                        || material == Material.COMPARATOR
                        || material == Material.VINE
                        || material == Material.GLOW_LICHEN
                        || material == Material.CAVE_VINES
                        || material == Material.CAVE_VINES_PLANT
                        || material == Material.SCULK_VEIN
                        || material.name().endsWith("_BUTTON")
                        || material.name().endsWith("_RAIL")
                        || material.name().endsWith("_CORAL")
                        || material.name().endsWith("_CORAL_FAN")
                        || material.name().endsWith("_CARPET")
                        || material == Material.TURTLE_EGG
                        || material == materialValueOf("FROGSPAWN")
                        || material == Material.HANGING_ROOTS
                        || material == Material.TRIPWIRE
                        || material == Material.TRIPWIRE_HOOK
                        || material == Material.DRAGON_EGG
                        || material == Material.BELL
                        || material == Material.BIG_DRIPLEAF_STEM
                        || material == Material.CHORUS_PLANT
                        || material == Material.REDSTONE_WIRE
                        || material.name().endsWith("_PRESSURE_PLATE")
                        || material == Material.MOSS_CARPET
                        || material == Material.SNOW
                        || material == Material.FROSTED_ICE
                        || material == Material.WATER_CAULDRON
                        || material == Material.LAVA_CAULDRON
                        || material == Material.POWDER_SNOW_CAULDRON
                        || material == Material.SMALL_AMETHYST_BUD
                        || material == Material.MEDIUM_AMETHYST_BUD
                        || material == Material.LARGE_AMETHYST_BUD
                        || material == Material.AMETHYST_CLUSTER
                        || material.name().endsWith("_SAPLING")
                        || material.name().startsWith("POTTED_")
                        || material == Material.AZALEA
                        || material == Material.FLOWERING_AZALEA
                        || material == Material.BROWN_MUSHROOM
                        || material == Material.RED_MUSHROOM
                        || material == Material.CRIMSON_FUNGUS
                        || material == Material.WARPED_FUNGUS
                        || material == materialValueOf("SHORT_GRASS")
                        || material == Material.FERN
                        || material == Material.DEAD_BUSH
                        || material == Material.DANDELION
                        || material == Material.POPPY
                        || material == Material.BLUE_ORCHID
                        || material == Material.ALLIUM
                        || material == Material.AZURE_BLUET
                        || material == Material.RED_TULIP
                        || material == Material.ORANGE_TULIP
                        || material == Material.WHITE_TULIP
                        || material == Material.PINK_TULIP
                        || material == Material.OXEYE_DAISY
                        || material == Material.CORNFLOWER
                        || material == Material.LILY_OF_THE_VALLEY
                        || material == Material.TORCHFLOWER
                        || material == Material.WITHER_ROSE
                        || material == Material.PINK_PETALS
                        || material == Material.SPORE_BLOSSOM
                        || material == Material.BAMBOO
                        || material == Material.SUGAR_CANE
                        || material == Material.CACTUS
                        || material == Material.CRIMSON_ROOTS
                        || material == Material.WARPED_ROOTS
                        || material == Material.NETHER_SPROUTS
                        || material == Material.WEEPING_VINES
                        || material == Material.TWISTING_VINES
                        || material == Material.WEEPING_VINES_PLANT
                        || material == Material.TWISTING_VINES_PLANT
                        || material == Material.COCOA
                        || material == Material.SWEET_BERRY_BUSH
                        || material == Material.TORCHFLOWER_CROP
                        || material == Material.WHEAT
                        || material == Material.MELON_STEM
                        || material == Material.PUMPKIN_STEM
                        || material == Material.POTATOES
                        || material == Material.CARROTS
                        || material == Material.BEETROOTS
                        || material == Material.KELP
                        || material == Material.KELP_PLANT
                        || material == Material.SEAGRASS
                        || material == Material.LILY_PAD
                        || material == materialValueOf("CREAKING_HEART")
                        || material == materialValueOf("OPEN_EYEBLOSSOM")
                        || material == materialValueOf("CLOSED_EYEBLOSSOM")
                        || material == materialValueOf("PALE_HANGING_MOSS")
                        || material == materialValueOf("RESIN_CLUMP")
                        || material == Material.FIRE
                        || material == Material.SOUL_FIRE
                        || material == Material.END_PORTAL
                        || material == Material.END_GATEWAY
                        || material == Material.NETHER_PORTAL
                        || material == Material.MUSHROOM_STEM
                        || material == Material.PISTON_HEAD
                        || material == Material.SUSPICIOUS_SAND
                        || material == Material.SUSPICIOUS_GRAVEL
                        || material == Material.BUBBLE_COLUMN
                        || material == Material.POWDER_SNOW
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

    @Nullable
    public Axis getAxis(ItemStack item) {
        byte axis = PersistentUtil.getOrDefault(item, PersistentDataType.BYTE, KeyUtil.AXIS, (byte) 15);
        return switch (axis) {
            case 0 -> Axis.X;
            case 1 -> Axis.Y;
            case 2 -> Axis.Z;
            default -> null;
        };
    }

    public void setAxis(ItemStack item, @Nullable Axis axis) {
        if (axis == null) {
            PersistentUtil.remove(item, KeyUtil.AXIS);
        } else {
            PersistentUtil.set(item, PersistentDataType.BYTE, KeyUtil.AXIS, (byte) (axis.ordinal()));
        }
    }
}
