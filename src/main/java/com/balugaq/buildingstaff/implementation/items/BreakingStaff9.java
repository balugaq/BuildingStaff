package com.balugaq.buildingstaff.implementation.items;

import com.balugaq.buildingstaff.api.items.BreakingStaff;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public class BreakingStaff9 extends BreakingStaff {
    public BreakingStaff9(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 9, true, false);
    }
}
