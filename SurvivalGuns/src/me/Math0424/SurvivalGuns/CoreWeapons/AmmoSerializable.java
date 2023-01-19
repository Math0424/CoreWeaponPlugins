package me.Math0424.SurvivalGuns.CoreWeapons;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Ammo.Ammo;
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

public class AmmoSerializable extends Craftable implements ConfigurationSerializable {

    private static final ArrayList<AmmoSerializable> ammo = new ArrayList<>();

    private int modelId;
    private Ammo baseClass;

    public AmmoSerializable(Map<String, Object> map) {
        try {
            this.name = ChatColor.translateAlternateColorCodes('&', (String) map.get("name"));
            this.modelId = (int) map.get("modelId");

            shape = (ArrayList<String>) map.get("shape");
            ingredients = (Map<Character, String>) map.get("ingredients");

            map.put("ammoId", name);
            map.put("maxBulletCount", map.get("bulletCount"));

            baseClass = new Ammo();
            baseClass.deSerialize(map);

            loadRecipe();
            ammo.add(this);
        } catch (Exception e) {
            e.printStackTrace();
            SurvivalGuns.log(Level.SEVERE, "Failed to load ammo " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static AmmoSerializable deserialize(Map<String, Object> map) {
        return new AmmoSerializable(map);
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static ArrayList<AmmoSerializable> getRegistered() {
        return ammo;
    }

    @Override
    public ItemStack getItem() {
        return Container.applyToItem(ItemStackUtil.createItemStack(Material.STONE_SHOVEL, name, 1, modelId), baseClass);
    }
}
