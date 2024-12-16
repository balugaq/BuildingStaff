package com.balugaq.buildingstaff.implementation.items;

import com.balugaq.buildingstaff.api.items.BuildingStaff;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public class BlockStrictBuildingStaff64 extends BuildingStaff {
    public BlockStrictBuildingStaff64(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 64, true);
    }
}
