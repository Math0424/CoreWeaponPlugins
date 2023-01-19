package me.Math0424.Withered.Loot;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class LootItem {

    public static ArrayList<LootItem> lootItems = new ArrayList<>();

    private final Integer level;
    private final Double chance;
    private final ItemStack item;
    private final Integer maxAmount;

    public LootItem(ItemStack item, Integer maxAmount, Integer level, Double chance) {
        this.item = item;
        this.level = level;
        this.chance = chance;
        this.maxAmount = maxAmount;
        lootItems.add(this);
    }

    public Integer getLevel() {
        return level;
    }

    public Double getChance() {
        return chance;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public static LootItem getByName(String s) {
        String name = ChatColor.stripColor(s);
        for (LootItem item : lootItems) {
            if (item.getItem().getItemMeta().hasDisplayName() && name.equals(ChatColor.stripColor(item.getItem().getItemMeta().getDisplayName()))) {
                return item;
            }
        }
        return null;
    }

    public static ArrayList<LootItem> getLootItems() {
        return lootItems;
    }
}
