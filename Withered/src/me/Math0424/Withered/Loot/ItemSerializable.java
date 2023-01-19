package me.Math0424.Withered.Loot;

import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ItemSerializable implements ConfigurationSerializable {

    private static final ArrayList<ItemSerializable> items = new ArrayList<>();

    private String name;
    private Material material;
    private List<String> lore;

    private Integer maxAmount;
    private Integer maxStackSize;

    private Integer level;
    private Double chanceOfSpawning;

    public ItemStack getItemStack() {
        return ItemStackUtil.createItemStack(material, name, lore);
    }

    public static ArrayList<ItemSerializable> getItems() {
        return items;
    }

    public ItemSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.material = Material.valueOf((String) map.get("material"));
            this.maxAmount = (Integer) map.get("maxAmount");
            this.maxStackSize = (Integer) map.get("maxStackSize");
            this.chanceOfSpawning = (Double) map.get("chanceOfSpawning");
            this.level = (Integer) map.get("level");

            this.lore = map.get("lore") != null ? (List<String>) map.get("lore") : null;

            WitheredUtil.debug("Successfully loaded item " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load item " + ((String) map.get("name")).replaceAll("&", "?"));
        }
    }

    public static ItemSerializable deserialize(Map<String, Object> map) {
        ItemSerializable i = new ItemSerializable(map);
        items.add(i);
        LootItem.getLootItems().add(new LootItem(i.getItemStack(), i.maxAmount, i.level, i.chanceOfSpawning));
        WitheredUtil.setMaxStackSize(i.getItemStack(), i.maxStackSize);
        return i;
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static ItemSerializable getByName(String name) {
        for (ItemSerializable x : items) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(name)) {
                return x;
            }
        }
        return null;
    }

}
