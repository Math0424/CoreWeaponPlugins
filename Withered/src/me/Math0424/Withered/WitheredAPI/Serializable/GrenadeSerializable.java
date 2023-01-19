package me.Math0424.Withered.WitheredAPI.Serializable;

import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.GrenadeType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class GrenadeSerializable implements ConfigurationSerializable {

    private static final ArrayList<GrenadeSerializable> grenades = new ArrayList<>();

    public Grenade baseClass;

    private String name;
    private Integer modelId;
    private Material material;
    private Double throwMultiplier;

    private Integer explodeTime;
    private Integer maxStackSize;

    private GrenadeType grenadeType;

    private Sound throwSound;
    private float throwPitch;
    private Integer throwVolume;
    private float explosiveYield;

    //not grenade
    private Integer level;
    private Double chanceOfSpawning;

    public GrenadeSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.modelId = (Integer) map.get("modelId");
            this.material = Material.valueOf((String) map.get("material"));
            this.throwMultiplier = (Double) map.get("throwMultiplier");
            this.explodeTime = (Integer) map.get("explodeTime");
            this.grenadeType = GrenadeType.valueOf((String) map.get("grenadeType"));
            this.throwSound = Sound.valueOf((String) map.get("throwSound"));
            this.throwPitch = Float.valueOf(String.valueOf(map.get("throwPitch")));
            this.throwVolume = (Integer) map.get("throwVolume");
            this.maxStackSize = (Integer) map.get("maxStackSize");
            this.explosiveYield = Float.valueOf(String.valueOf(map.get("explosiveYield")));

            this.chanceOfSpawning = (Double) map.get("chanceOfSpawning");
            this.level = (Integer) map.get("level");

            WitheredUtil.debug("Successfully loaded grenade " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load grenade " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static GrenadeSerializable deserialize(Map<String, Object> map) {
        GrenadeSerializable i = new GrenadeSerializable(map);
        grenades.add(i);
        i.baseClass = new Grenade(i.name, i.grenadeType, i.modelId, i.material, i.throwMultiplier, i.explodeTime, i.maxStackSize, i.throwSound, i.throwPitch, i.throwVolume, i.explosiveYield);
        LootItem.getLootItems().add(new LootItem(i.baseClass.getItemStack(), 1, i.level, i.chanceOfSpawning));
        return i;
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static GrenadeSerializable getByName(String name) {
        for (GrenadeSerializable x : grenades) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(name)) {
                return x;
            }
        }
        return null;
    }

    public static ArrayList<GrenadeSerializable> getGrenades() {
        return grenades;
    }

}
