package me.Math0424.Withered.Inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface InventoryClickedEventPredicate {

    void run(InventoryClickEvent e);

}
