package me.Math0424.SurvivalGuns.CoreWeapons;

import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.SurvivalGuns.CoreWeapons.Crafting.Craftable;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class ComponentSerializable extends Craftable implements ConfigurationSerializable {

    private static final ArrayList<ComponentSerializable> components = new ArrayList<>();

    private int id, count;

    public Map<String, Object> serialize() {
        return null;
    }

    public ComponentSerializable(Map<String, Object> map) {
        try {
            this.name = ChatColor.translateAlternateColorCodes('&', (String) map.get("name"));
            id = (Integer) map.get("modelId");
            count = (Integer) map.get("count");

            shape = (ArrayList<String>) map.get("shape");
            ingredients = (Map<Character, String>) map.get("ingredients");

            loadRecipe();
            components.add(this);
        } catch (Exception e) {
            e.printStackTrace();
            SurvivalGuns.log(Level.SEVERE, "Failed to load component " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static ComponentSerializable deserialize(Map<String, Object> map) {
        return new ComponentSerializable(map);
    }

    public static ArrayList<ComponentSerializable> getRegistered() {
        return components;
    }

    @Override
    public ItemStack getItem() {
        return ItemStackUtil.createItemStack(Material.IRON_NUGGET, name, count, id);
    }

}
