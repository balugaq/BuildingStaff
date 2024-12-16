package com.balugaq.buildingstaff.api.items;

import com.balugaq.buildingstaff.utils.Debug;
import com.balugaq.buildingstaff.utils.KeyUtil;
import com.balugaq.buildingstaff.utils.PersistentUtil;
import org.bukkit.Axis;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public interface Staff {
    @Nullable
    default Axis getAxis(ItemStack item) {
        String axis = PersistentUtil.getOrDefault(item, PersistentDataType.STRING, KeyUtil.AXIS, null);
        if (axis == null) {
            return null;
        }

        try {
            return Axis.valueOf(axis);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    default void setAxis(ItemStack item, @Nullable Axis axis) {
        if (axis == null) {
            PersistentUtil.set(item, PersistentDataType.STRING, KeyUtil.AXIS, "null");
        } else {
            PersistentUtil.set(item, PersistentDataType.STRING, KeyUtil.AXIS, axis.name());
        }
    }

    default boolean isBlockStrict() {
        return true;
    }
}
