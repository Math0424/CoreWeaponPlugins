package me.Math0424.GunCreator.GUI.Page;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ActionSignEventPredicate {

    void run(Player p, String[] output);

}
