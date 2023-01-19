package me.Math0424.Withered.WitheredAPI.Serializable;

import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Deployables.Deployable;
import me.Math0424.CoreWeapons.Deployables.Types.DeployableType;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class DeployableSerializable implements ConfigurationSerializable {

    private static final ArrayList<DeployableSerializable> deployables = new ArrayList<>();

    public Deployable baseClass;

    private String name;
    private Double maxHealth;
    private Integer modelId;
    private Double range;
    private DeployableType type;

    private Sound deploySound;
    private Float deployPitch;
    private Integer deployVolume;

    //not deployable
    private Integer level;
    private Double chanceOfSpawning;

    public DeployableSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.modelId = (Integer) map.get("modelId");
            this.maxHealth = (Double) map.get("maxHealth");
            this.range = (Double) map.get("range");
            this.type = DeployableType.valueOf((String) map.get("type"));
            this.deployPitch = Float.valueOf(String.valueOf(map.get("deployPitch")));
            this.deploySound = Sound.valueOf((String) map.get("deploySound"));
            this.deployVolume = (Integer) map.get("deployVolume");

            this.chanceOfSpawning = (Double) map.get("chanceOfSpawning");
            this.level = (Integer) map.get("level");

            WitheredUtil.debug("Successfully loaded deployable " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load deployable " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static DeployableSerializable deserialize(Map<String, Object> map) {
        DeployableSerializable i = new DeployableSerializable(map);
        deployables.add(i);
        Deployable d = new Deployable(i.name, i.maxHealth, i.range, i.type, i.modelId, i.deploySound, i.deployPitch, i.deployVolume);
        i.baseClass = d;
        LootItem.getLootItems().add(new LootItem(d.getDeployableItemstack(), 1, i.level, i.chanceOfSpawning));
        return i;
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static DeployableSerializable getByName(String name) {
        for (DeployableSerializable x : deployables) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(name)) {
                return x;
            }
        }
        return null;
    }

}
