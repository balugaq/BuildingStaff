package com.balugaq.buildingstaff.api.objects.events;

import com.balugaq.buildingstaff.api.items.Staff;
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
    private final Staff staff;
    private final Block lookingAtBlock;

    public PrepareBuildingEvent(Player player, Staff staff, Block lookingAtBlock) {
        this.player = player;
        this.staff = staff;
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
