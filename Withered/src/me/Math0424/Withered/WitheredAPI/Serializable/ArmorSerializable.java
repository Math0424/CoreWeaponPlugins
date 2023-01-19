package me.Math0424.Withered.WitheredAPI.Serializable;

import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Armor.Type.ArmorType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class ArmorSerializable implements ConfigurationSerializable {

    private static final ArrayList<ArmorSerializable> armor = new ArrayList<>();

    public Armor baseClass;

    private String name;
    private Double maxSpeed;
    private Double acceleration;
    private Material material;
    private Integer maxHeight;
    private Integer ammoId;
    private Integer modelId;

    private Integer uses;
    private Integer fixTime;
    private Integer usesFixedPerReload;

    private ArmorType armorType;

    //not grenade
    private Integer level;
    private Double chanceOfSpawning;

    public ArmorSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.modelId = (Integer) map.get("modelId");
            this.maxSpeed = (Double) map.get("maxSpeed");
            this.acceleration = (Double) map.get("acceleration");
            this.maxHeight = (Integer) map.get("maxHeight");
            this.ammoId = (Integer) map.get("ammoId");
            this.material = Material.valueOf((String) map.get("material"));
            this.uses = (Integer) map.get("uses");
            this.fixTime = (Integer) map.get("fixTime");
            this.usesFixedPerReload = (Integer) map.get("usesFixedPerReload");
            this.armorType = ArmorType.valueOf((String) map.get("armorType"));

            this.chanceOfSpawning = (Double) map.get("chanceOfSpawning");
            this.level = (Integer) map.get("level");

            WitheredUtil.debug("Successfully loaded armor " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load armor " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static ArmorSerializable deserialize(Map<String, Object> map) {
        ArmorSerializable i = new ArmorSerializable(map);
        armor.add(i);
        Armor a = new Armor(i.name, i.armorType, i.material, i.modelId, i.maxSpeed, i.acceleration, i.maxHeight, AmmoSerializable.getById(i.ammoId).baseClass.getId(), i.uses, i.fixTime, i.usesFixedPerReload);
        i.baseClass = a;
        LootItem.getLootItems().add(new LootItem(a.getItemStack(), 1, i.level, i.chanceOfSpawning));
        return i;
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static ArmorSerializable getByName(String name) {
        for (ArmorSerializable x : armor) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(name)) {
                return x;
            }
        }
        return null;
    }

}
