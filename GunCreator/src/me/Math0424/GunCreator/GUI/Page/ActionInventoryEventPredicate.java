package me.Math0424.GunCreator.GUI.Page;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ActionInventoryEventPredicate {

    void run(int slot, Player p);

}
