package com.balugaq.buildingstaff.utils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
@UtilityClass
public class SlimefunItemUtil {
    @CanIgnoreReturnValue
    public static @NotNull SlimefunItem registerItem(@NotNull SlimefunItem item, @NotNull SlimefunAddon addon) {
        item.register(addon);
        return item;
    }

    public static void unregisterItems(@NotNull SlimefunAddon addon) {
        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            if (item.getAddon().equals(addon)) {
                unregisterItem(item);
            }
        }
    }

    public static void unregisterItem(@Nullable SlimefunItem item) {
        if (item == null) {
            return;
        }

        if (item instanceof Radioactive) {
            Slimefun.getRegistry().getRadioactiveItems().remove(item);
        }

        if (item instanceof GEOResource geor) {
            Slimefun.getRegistry().getGEOResources().remove(geor.getKey());
        }

        Slimefun.getRegistry().getTickerBlocks().remove(item.getId());
        Slimefun.getRegistry().getEnabledSlimefunItems().remove(item);

        Slimefun.getRegistry().getSlimefunItemIds().remove(item.getId());
        Slimefun.getRegistry().getAllSlimefunItems().remove(item);
        Slimefun.getRegistry().getMenuPresets().remove(item.getId());
        Slimefun.getRegistry().getBarteringDrops().remove(item.getItem());
    }

    public static void unregisterItemGroups(@NotNull SlimefunAddon addon) {
        for (ItemGroup itemGroup : Slimefun.getRegistry().getAllItemGroups()) {
            if (Objects.equals(itemGroup.getAddon(), addon)) {
                unregisterItemGroup(itemGroup);
            }
        }
    }

    public static void unregisterItemGroup(@Nullable ItemGroup itemGroup) {
        if (itemGroup == null) {
            return;
        }

        Slimefun.getRegistry().getAllItemGroups().remove(itemGroup);
    }
}
