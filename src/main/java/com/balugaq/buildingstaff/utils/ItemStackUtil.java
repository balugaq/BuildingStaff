package com.balugaq.buildingstaff.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

@UtilityClass
public class ItemStackUtil {
    @Nonnull
    public static ItemStack getCleanItem(ItemStack itemStack) {
        if (itemStack == null) {
            return new ItemStack(Material.AIR);
        }

        ItemStack clone = new ItemStack(itemStack.getType());
        clone.setAmount(itemStack.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            clone.setItemMeta(meta);
        }
        return clone;
    }
}
