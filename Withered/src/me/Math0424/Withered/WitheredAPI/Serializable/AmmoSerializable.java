package me.Math0424.Withered.WitheredAPI.Serializable;

import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Guns.Ammo.Ammo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class AmmoSerializable implements ConfigurationSerializable {

    private static final ArrayList<AmmoSerializable> ammo = new ArrayList<>();

    public Ammo baseClass;

    private String name;
    private Material material;
    private Integer id;
    private Integer modelId;
    private Integer maxStackSize;

    //not ammo
    private Integer level;
    private Integer maxAmount;
    private Double chanceOfSpawning;

    public AmmoSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.material = Material.valueOf((String) map.get("material"));
            this.id = (Integer) map.get("id");
            this.modelId = (Integer) map.get("modelId");
            this.maxStackSize = (Integer) map.get("maxStackSize");

            this.maxAmount = (Integer) map.get("maxAmount");
            this.chanceOfSpawning = (Double) map.get("chanceOfSpawning");
            this.level = (Integer) map.get("level");

            WitheredUtil.debug("Successfully loaded ammo " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load ammo " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static AmmoSerializable deserialize(Map<String, Object> map) {
        AmmoSerializable i = new AmmoSerializable(map);
        ammo.add(i);
        Ammo a = new Ammo(i.name, i.material, i.modelId, i.maxStackSize);
        i.baseClass = a;
        LootItem.getLootItems().add(new LootItem(a.getItemStack(), i.maxAmount, i.level, i.chanceOfSpawning));
        return i;
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static AmmoSerializable getByName(String name) {
        for (AmmoSerializable x : ammo) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(ChatColor.stripColor(name))) {
                return x;
            }
        }
        return null;
    }

    public static AmmoSerializable getById(int id) {
        for (AmmoSerializable x : ammo) {
            if (x.id == id) {
                return x;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<AmmoSerializable> getAmmo() {
        return ammo;
    }
}
