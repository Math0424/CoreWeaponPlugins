package me.Math0424.Withered.Structures;

import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class StructureSerializable implements ConfigurationSerializable {

    public static ArrayList<StructureSerializable> structures = new ArrayList<>();

    private String name;
    private Integer level;
    private Double chanceOfSpawning;

    public ItemStack getItemStack() {
        return StructureManager.createStructureItemStack(name);
    }

    public static ArrayList<StructureSerializable> getStructures() {
        return structures;
    }

    public StructureSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.chanceOfSpawning = (Double) map.get("chanceOfSpawning");
            this.level = (Integer) map.get("level");

            WitheredUtil.debug("Successfully loaded structure " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load structure " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static StructureSerializable deserialize(Map<String, Object> map) {
        StructureSerializable s = new StructureSerializable(map);
        structures.add(s);
        LootItem.getLootItems().add(new LootItem(s.getItemStack(), 1, s.level, s.chanceOfSpawning));
        return s;
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static StructureSerializable getByName(String name) {
        for (StructureSerializable x : structures) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(name)) {
                return x;
            }
        }
        return null;
    }

}
