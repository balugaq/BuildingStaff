package com.balugaq.buildingstaff.api.items;

import org.bukkit.Axis;
import org.bukkit.inventory.ItemStack;

public interface Staff {
    Axis getAxis(ItemStack itemStack);
    void setAxis(ItemStack itemStack, Axis axis);
    boolean isBlockStrict();
}
