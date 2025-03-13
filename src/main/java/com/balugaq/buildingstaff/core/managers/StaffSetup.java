package com.balugaq.buildingstaff.core.managers;

import com.balugaq.buildingstaff.api.interfaces.IManager;
import com.balugaq.buildingstaff.implementation.BuildingStaffPlugin;
import com.balugaq.buildingstaff.implementation.items.BlockStrictBuildingStaff4096;
import com.balugaq.buildingstaff.implementation.items.BlockStrictBuildingStaff64;
import com.balugaq.buildingstaff.implementation.items.BlockStrictBuildingStaff9;
import com.balugaq.buildingstaff.implementation.items.BreakingStaff4096;
import com.balugaq.buildingstaff.implementation.items.BreakingStaff64;
import com.balugaq.buildingstaff.implementation.items.BreakingStaff9;
import com.balugaq.buildingstaff.implementation.items.BuildingStaff4096;
import com.balugaq.buildingstaff.implementation.items.BuildingStaff64;
import com.balugaq.buildingstaff.implementation.items.BuildingStaff9;
import com.balugaq.buildingstaff.utils.KeyUtil;
import com.balugaq.buildingstaff.utils.Lang;
import com.balugaq.buildingstaff.utils.SlimefunItemUtil;
import com.balugaq.buildingstaff.utils.compatibility.Converter;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StaffSetup implements IManager {
    public static final ItemStack COMPRESSED_CARBON = Converter.getItem(SlimefunItems.COMPRESSED_CARBON);
    public static final ItemStack STAFF_ELEMENTAL = Converter.getItem(SlimefunItems.STAFF_ELEMENTAL);
    public static final ItemStack DAMASCUS_STEEL_INGOT = Converter.getItem(SlimefunItems.DAMASCUS_STEEL_INGOT);
    public static final ItemStack BRASS_INGOT = Converter.getItem(SlimefunItems.BRASS_INGOT);;
    public static final ItemStack BRONZE_INGOT = Converter.getItem(SlimefunItems.BRONZE_INGOT);
    public static ItemGroup mainGroup;
    public static BuildingStaff9 buildingStaff9;
    public static BuildingStaff64 buildingStaff64;
    public static BuildingStaff4096 buildingStaff4096;
    public static BlockStrictBuildingStaff9 blockStrictBuildingStaff9;
    public static BlockStrictBuildingStaff64 blockStrictBuildingStaff64;
    public static BlockStrictBuildingStaff4096 blockStrictBuildingStaff4096;
    public static BreakingStaff9 breakingStaff9;
    public static BreakingStaff64 breakingStaff64;
    public static BreakingStaff4096 breakingStaff4096;
    private final BuildingStaffPlugin plugin;

    public StaffSetup(BuildingStaffPlugin plugin) {
        this.plugin = plugin;
    }

    public static void unregisterAll(@NotNull SlimefunAddon instance) {
        SlimefunItemUtil.unregisterItem(breakingStaff4096);
        SlimefunItemUtil.unregisterItem(breakingStaff64);
        SlimefunItemUtil.unregisterItem(breakingStaff9);
        SlimefunItemUtil.unregisterItem(blockStrictBuildingStaff4096);
        SlimefunItemUtil.unregisterItem(blockStrictBuildingStaff64);
        SlimefunItemUtil.unregisterItem(blockStrictBuildingStaff9);
        SlimefunItemUtil.unregisterItem(buildingStaff4096);
        SlimefunItemUtil.unregisterItem(buildingStaff64);
        SlimefunItemUtil.unregisterItem(buildingStaff9);
        SlimefunItemUtil.unregisterItemGroup(mainGroup);
        SlimefunItemUtil.unregisterItems(instance);
        SlimefunItemUtil.unregisterItemGroups(instance);
    }

    @Override
    public void setup() {
        ItemStack stick = new ItemStack(Material.STICK);

        mainGroup = new ItemGroup(KeyUtil.newKey("building_staff"), Converter.getItem(
                Material.BLAZE_ROD,
                "&aBuilding Staff"
        ));


        mainGroup.register(plugin);

        buildingStaff9 = new BuildingStaff9(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_9",
                        new ItemStack(Material.IRON_SWORD),
                        Lang.getMessage("staff.place9"),
                        Lang.getMessage("place-block"),
                        Lang.getMessage("max-range", "range", 9),
                        Lang.getMessage("any-block")
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        COMPRESSED_CARBON, COMPRESSED_CARBON, DAMASCUS_STEEL_INGOT,
                        COMPRESSED_CARBON, stick, COMPRESSED_CARBON,
                        stick, COMPRESSED_CARBON, COMPRESSED_CARBON
                }
        );

        buildingStaff9.register(plugin);

        buildingStaff64 = new BuildingStaff64(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_64",
                        new ItemStack(Material.GOLDEN_SWORD),
                        Lang.getMessage("staff.place64"),
                        Lang.getMessage("place-block"),
                        Lang.getMessage("max-range", "range", 64),
                        Lang.getMessage("any-block")
                ),
                RecipeType.ANCIENT_ALTAR,
                new ItemStack[]{
                        buildingStaff9.getItem(), buildingStaff9.getItem(), buildingStaff9.getItem(),
                        buildingStaff9.getItem(), STAFF_ELEMENTAL, buildingStaff9.getItem(),
                        buildingStaff9.getItem(), buildingStaff9.getItem(), buildingStaff9.getItem()
                }
        );

        buildingStaff64.register(plugin);

        buildingStaff4096 = new BuildingStaff4096(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_4096",
                        new ItemStack(Material.DIAMOND_SWORD),
                        Lang.getMessage("staff.place4096"),
                        Lang.getMessage("place-block"),
                        Lang.getMessage("max-range", "range", 4096),
                        Lang.getMessage("any-block"),
                        Lang.getMessage("op-only")
                ),
                RecipeType.NULL,
                new ItemStack[]{}
        );

        buildingStaff4096.register(plugin);

        blockStrictBuildingStaff9 = new BlockStrictBuildingStaff9(
                mainGroup,
                new SlimefunItemStack(
                        "BLOCK_STRICT_BUILDING_STAFF_9",
                        new ItemStack(Material.IRON_SWORD),
                        Lang.getMessage("staff.place9"),
                        Lang.getMessage("place-block"),
                        Lang.getMessage("max-range", "range", 9),
                        Lang.getMessage("one-kind-of-block")
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        COMPRESSED_CARBON, COMPRESSED_CARBON, BRASS_INGOT,
                        COMPRESSED_CARBON, stick, COMPRESSED_CARBON,
                        stick, COMPRESSED_CARBON, COMPRESSED_CARBON
                }
        );

        blockStrictBuildingStaff9.register(plugin);

        blockStrictBuildingStaff64 = new BlockStrictBuildingStaff64(
                mainGroup,
                new SlimefunItemStack(
                        "BLOCK_STRICT_BUILDING_STAFF_64",
                        new ItemStack(Material.GOLDEN_SWORD),
                        Lang.getMessage("staff.place64"),
                        Lang.getMessage("place-block"),
                        Lang.getMessage("max-range", "range", 64),
                        Lang.getMessage("one-kind-of-block")
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        blockStrictBuildingStaff9.getItem(), blockStrictBuildingStaff9.getItem(), blockStrictBuildingStaff9.getItem(),
                        blockStrictBuildingStaff9.getItem(), STAFF_ELEMENTAL, blockStrictBuildingStaff9.getItem(),
                        blockStrictBuildingStaff9.getItem(), blockStrictBuildingStaff9.getItem(), blockStrictBuildingStaff9.getItem()
                }
        );

        blockStrictBuildingStaff64.register(plugin);

        blockStrictBuildingStaff4096 = new BlockStrictBuildingStaff4096(
                mainGroup,
                new SlimefunItemStack(
                        "BLOCK_STRICT_BUILDING_STAFF_4096",
                        new ItemStack(Material.DIAMOND_SWORD),
                        Lang.getMessage("staff.place4096"),
                        Lang.getMessage("place-block"),
                        Lang.getMessage("max-range", "range", 4096),
                        Lang.getMessage("one-kind-of-block"),
                        Lang.getMessage("op-only")
                ),
                RecipeType.NULL,
                new ItemStack[]{}
        );

        blockStrictBuildingStaff4096.register(plugin);

        breakingStaff9 = new BreakingStaff9(
                mainGroup,
                new SlimefunItemStack(
                        "BREAKING_STAFF_9",
                        new ItemStack(Material.IRON_SWORD),
                        Lang.getMessage("staff.break9"),
                        Lang.getMessage("break-block"),
                        Lang.getMessage("max-range", "range", 9),
                        Lang.getMessage("one-kind-of-block")
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        COMPRESSED_CARBON, COMPRESSED_CARBON, BRONZE_INGOT,
                        COMPRESSED_CARBON, stick, COMPRESSED_CARBON,
                        stick, COMPRESSED_CARBON, COMPRESSED_CARBON
                }
        );

        breakingStaff9.register(plugin);

        breakingStaff64 = new BreakingStaff64(
                mainGroup,
                new SlimefunItemStack(
                        "BREAKING_STAFF_64",
                        new ItemStack(Material.GOLDEN_SWORD),
                        Lang.getMessage("staff.break64"),
                        Lang.getMessage("break-block"),
                        Lang.getMessage("max-range", "range", 64),
                        Lang.getMessage("one-kind-of-block")
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        breakingStaff9.getItem(), breakingStaff9.getItem(), breakingStaff9.getItem(),
                        breakingStaff9.getItem(), STAFF_ELEMENTAL, breakingStaff9.getItem(),
                        breakingStaff9.getItem(), breakingStaff9.getItem(), breakingStaff9.getItem()
                }
        );

        breakingStaff64.register(plugin);

        breakingStaff4096 = new BreakingStaff4096(
                mainGroup,
                new SlimefunItemStack(
                        "BREAKING_STAFF_4096",
                        new ItemStack(Material.DIAMOND_SWORD),
                        Lang.getMessage("staff.break4096"),
                        Lang.getMessage("break-block"),
                        Lang.getMessage("max-range", "range", 4096),
                        Lang.getMessage("one-kind-of-block"),
                        Lang.getMessage("op-only")
                ),
                RecipeType.NULL,
                new ItemStack[]{}
        );

        breakingStaff4096.register(plugin);
    }

    @Override
    public void shutdown() {
        unregisterAll(plugin);
    }
}
