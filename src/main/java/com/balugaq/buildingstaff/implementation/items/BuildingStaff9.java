package com.balugaq.buildingstaff.implementation.items;

import com.balugaq.buildingstaff.api.items.BuildingStaff;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public class BuildingStaff9 extends BuildingStaff {
    public BuildingStaff9(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 9, false);
    }
}
