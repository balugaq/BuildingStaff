package com.balugaq.buildingstaff.implementation;

import com.balugaq.buildingstaff.api.items.Staff;
import com.balugaq.buildingstaff.implementation.items.Staff4096;
import com.balugaq.buildingstaff.implementation.items.Staff64;
import com.balugaq.buildingstaff.implementation.items.Staff9;
import com.balugaq.buildingstaff.utils.KeyUtil;
import com.balugaq.buildingstaff.utils.SlimefunItemUtil;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BuildingStaffSetup {
    private static ItemGroup mainGroup;
    private static Staff9 staff9;
    private static Staff64 staff64;
    private static Staff4096 staff4096;


    public static void setup(SlimefunAddon instance) {
        mainGroup = new ItemGroup(KeyUtil.newKey("building_staff"), new ItemStack(new CustomItemStack(
                Material.BLAZE_ROD,
                "&a建筑魔杖",
                "&7右键以放置方块"
        )));

        mainGroup.register(instance);

        staff9 = new Staff9(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_9",
                        new ItemStack(Material.IRON_SWORD),
                        "&a建筑魔杖 | &99格",
                        "&7右键以放置方块",
                        "&a最大范围: 9格"
                ),
                RecipeType.ANCIENT_ALTAR,
                new ItemStack[]{
                        SlimefunItems.PROGRAMMABLE_ANDROID, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.PROGRAMMABLE_ANDROID,
                        SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.STAFF_ELEMENTAL, SlimefunItems.GPS_TRANSMITTER_2,
                        SlimefunItems.CARGO_MOTOR, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.CARGO_MOTOR
                }
        );

        staff9.register(instance);

        staff64 = new Staff64(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_64",
                        new ItemStack(Material.GOLDEN_SWORD),
                        "&a建筑魔杖 | &664格",
                        "&7右键以放置方块",
                        "&e最大范围: 64格"
                ),
                RecipeType.ANCIENT_ALTAR,
                new ItemStack[]{
                        staff9.getItem(), staff9.getItem(), staff9.getItem(),
                        staff9.getItem(), SlimefunItems.STAFF_ELEMENTAL, staff9.getItem(),
                        staff9.getItem(), staff9.getItem(), staff9.getItem()
                }
        );

        staff64.register(instance);

        staff4096 = new Staff4096(
                mainGroup,
                new SlimefunItemStack(
                        "BUILDING_STAFF_4096",
                        new ItemStack(Material.DIAMOND_SWORD),
                        "&a建筑魔杖 | &e4096格",
                        "&7右键以放置方块",
                        "&c最大范围: 4096格"
                ),
                RecipeType.NULL,
                new ItemStack[]{}
        );

        staff4096.register(instance);
    }

    public static void unregister(SlimefunAddon instance) {
        SlimefunItemUtil.unregisterItem(staff4096);
        SlimefunItemUtil.unregisterItem(staff64);
        SlimefunItemUtil.unregisterItem(staff9);
        SlimefunItemUtil.unregisterItemGroup(mainGroup);
        SlimefunItemUtil.unregisterItems(instance);
        SlimefunItemUtil.unregisterItemGroups(instance);
    }
}
