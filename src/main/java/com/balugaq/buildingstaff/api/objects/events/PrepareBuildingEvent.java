package com.balugaq.buildingstaff.api.objects.events;

import com.balugaq.buildingstaff.api.items.BuildingStaff;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PrepareBuildingEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final BuildingStaff buildingStaff;
    private final Block lookingAtBlock;

    public PrepareBuildingEvent(Player player, BuildingStaff buildingStaff, Block lookingAtBlock) {
        this.player = player;
        this.buildingStaff = buildingStaff;
        this.lookingAtBlock = lookingAtBlock;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
