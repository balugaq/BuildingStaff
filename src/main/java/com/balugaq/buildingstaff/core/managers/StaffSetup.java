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
import com.balugaq.buildingstaff.utils.ItemStackUtil;
import com.balugaq.buildingstaff.utils.KeyUtil;
import com.balugaq.buildingstaff.utils.SlimefunItemUtil;
import com.balugaq.buildingstaff.utils.compatibility.Converter;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StaffSetup implements IManager {
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
        ItemStack cc = ItemStackUtil.getCleanItem(SlimefunItems.COMPRESSED_CARBON);
        mainGroup = new ItemGroup(KeyUtil.newKey("building_staff"), Converter.getItem(
                Material.BLAZE_ROD,
                "&a建筑魔杖"
        ));

        mainGroup.register(plugin);

        buildingStaff9 = new BuildingStaff9(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_9",
                        new ItemStack(Material.IRON_SWORD),
                        "&a建筑魔杖 | &99格",
                        "&7右键以放置方块",
                        "&a最大范围: 9格",
                        "&a选中平面可由任意方块组成"
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        cc, cc, SlimefunItems.DAMASCUS_STEEL_INGOT,
                        cc, stick, cc,
                        stick, cc, cc
                }
        );

        buildingStaff9.register(plugin);

        buildingStaff64 = new BuildingStaff64(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_64",
                        new ItemStack(Material.GOLDEN_SWORD),
                        "&a建筑魔杖 | &664格",
                        "&7右键以放置方块",
                        "&e最大范围: 64格",
                        "&a选中平面可由任意方块组成"
                ),
                RecipeType.ANCIENT_ALTAR,
                new ItemStack[]{
                        buildingStaff9.getItem(), buildingStaff9.getItem(), buildingStaff9.getItem(),
                        buildingStaff9.getItem(), SlimefunItems.STAFF_ELEMENTAL, buildingStaff9.getItem(),
                        buildingStaff9.getItem(), buildingStaff9.getItem(), buildingStaff9.getItem()
                }
        );

        buildingStaff64.register(plugin);

        buildingStaff4096 = new BuildingStaff4096(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_4096",
                        new ItemStack(Material.DIAMOND_SWORD),
                        "&a建筑魔杖 | &e4096格",
                        "&7右键以放置方块",
                        "&c最大范围: 4096格",
                        "&a选中平面可由任意方块组成",
                        "&c仅限管理员使用"
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
                        "&a建筑魔杖 | &99格",
                        "&7右键以放置方块",
                        "&a最大范围: 9格",
                        "&c选中平面只能由一种方块组成"
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        cc, cc, SlimefunItems.BRASS_INGOT,
                        cc, stick, cc,
                        stick, cc, cc
                }
        );

        blockStrictBuildingStaff9.register(plugin);

        blockStrictBuildingStaff64 = new BlockStrictBuildingStaff64(
                mainGroup,
                new SlimefunItemStack(
                        "BLOCK_STRICT_BUILDING_STAFF_64",
                        new ItemStack(Material.GOLDEN_SWORD),
                        "&a建筑魔杖 | &664格",
                        "&7右键以放置方块",
                        "&e最大范围: 64格",
                        "&c选中平面只能由一种方块组成"
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        blockStrictBuildingStaff9.getItem(), blockStrictBuildingStaff9.getItem(), blockStrictBuildingStaff9.getItem(),
                        blockStrictBuildingStaff9.getItem(), SlimefunItems.STAFF_ELEMENTAL, blockStrictBuildingStaff9.getItem(),
                        blockStrictBuildingStaff9.getItem(), blockStrictBuildingStaff9.getItem(), blockStrictBuildingStaff9.getItem()
                }
        );

        blockStrictBuildingStaff64.register(plugin);

        blockStrictBuildingStaff4096 = new BlockStrictBuildingStaff4096(
                mainGroup,
                new SlimefunItemStack(
                        "BLOCK_STRICT_BUILDING_STAFF_4096",
                        new ItemStack(Material.DIAMOND_SWORD),
                        "&a建筑魔杖 | &e4096格",
                        "&7右键以放置方块",
                        "&c最大范围: 4096格",
                        "&c选中平面只能由一种方块组成",
                        "&c仅限管理员使用"
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
                        "&c破坏魔杖 | &99格",
                        "&7右键以破坏方块",
                        "&a最大范围: 9格",
                        "&c选中平面只能由一种方块组成"
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        cc, cc, SlimefunItems.BRONZE_INGOT,
                        cc, stick, cc,
                        stick, cc, cc
                }
        );

        breakingStaff9.register(plugin);

        breakingStaff64 = new BreakingStaff64(
                mainGroup,
                new SlimefunItemStack(
                        "BREAKING_STAFF_64",
                        new ItemStack(Material.GOLDEN_SWORD),
                        "&c破坏魔杖 | &664格",
                        "&7右键以破坏方块",
                        "&e最大范围: 64格",
                        "&c选中平面只能由一种方块组成"
                ),
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        breakingStaff9.getItem(), breakingStaff9.getItem(), breakingStaff9.getItem(),
                        breakingStaff9.getItem(), SlimefunItems.STAFF_ELEMENTAL, breakingStaff9.getItem(),
                        breakingStaff9.getItem(), breakingStaff9.getItem(), breakingStaff9.getItem()
                }
        );

        breakingStaff64.register(plugin);

        breakingStaff4096 = new BreakingStaff4096(
                mainGroup,
                new SlimefunItemStack(
                        "BREAKING_STAFF_4096",
                        new ItemStack(Material.DIAMOND_SWORD),
                        "&c破坏魔杖 | &e4096格",
                        "&7右键以破坏方块",
                        "&c最大范围: 4096格",
                        "&c选中平面只能由一种方块组成",
                        "&c仅限管理员使用"
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
