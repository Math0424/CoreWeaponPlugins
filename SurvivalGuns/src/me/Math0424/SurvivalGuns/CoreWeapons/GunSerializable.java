package me.Math0424.SurvivalGuns.CoreWeapons;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.Gun.GunType;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.SurvivalGuns.CoreWeapons.Crafting.Craftable;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class GunSerializable extends Craftable implements ConfigurationSerializable {

    private static final ArrayList<GunSerializable> guns = new ArrayList<>();

    private int modelId;
    private Gun baseClass;

    private GunSerializable(Map<String, Object> map) {
        try {
            this.name = ChatColor.translateAlternateColorCodes('&', (String) map.get("name"));
            this.modelId = (int) map.get("modelID");

            shape = (ArrayList<String>) map.get("shape");
            ingredients = (Map<Character, String>) map.get("ingredients");

            //random values needed for the gun because bukkit serialize sucks
            map.put("uuid", UUID.randomUUID());
            map.put("creationDate", System.currentTimeMillis());

            map.put("gunType", GunType.valueOf((String) map.get("gunType")));
            map.put("bulletType", BulletType.valueOf((String) map.get("bulletType")));

            map.put("firePitch", ((Double)map.get("firePitch")).floatValue());
            map.put("bulletPower", ((Double)map.get("bulletPower")).floatValue());
            //end nonsense

            baseClass = new Gun();
            baseClass.deSerialize(map);

            loadRecipe();
            guns.add(this);
        } catch (Exception e) {
            e.printStackTrace();
            SurvivalGuns.log(Level.SEVERE, "Failed to load gun " + ChatColor.translateAlternateColorCodes('&',(String) map.get("name")));
        }
    }

    public static GunSerializable deserialize(Map<String, Object> map) {
        return new GunSerializable(map);
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static GunSerializable getByName(String name) {
        for (GunSerializable x : guns) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(name)) {
                return x;
            }
        }
        return null;
    }

    public static ArrayList<GunSerializable> getRegistered() {
        return guns;
    }

    @Override
    public ItemStack getItem() {
        return Container.applyToItem(ItemStackUtil.createItemStack(Material.DIAMOND_PICKAXE, name, 1, modelId), baseClass);
    }

}
