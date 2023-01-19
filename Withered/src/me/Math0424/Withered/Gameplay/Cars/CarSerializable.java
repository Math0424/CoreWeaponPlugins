package me.Math0424.Withered.Gameplay.Cars;

import me.Math0424.Withered.Util.WitheredUtil;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class CarSerializable implements ConfigurationSerializable {

    public static ArrayList<CarSerializable> carTypes = new ArrayList<>();

    private String name;
    private Material material;
    private boolean hasInventory;
    private int materialId;
    private double health;
    private double acceleration;
    private double maxSpeed;
    private double maxSpeedOffRoad;
    private int level;
    private double chance;

    public CarSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.material = Material.valueOf((String) map.get("material"));
            this.materialId = (int) map.get("materialId");
            this.health = (double) map.get("health");
            this.acceleration = (double) map.get("acceleration");
            this.maxSpeed = (double) map.get("maxSpeed");
            this.maxSpeedOffRoad = (double) map.get("maxSpeedOffRoad");
            this.hasInventory = (boolean) map.get("hasInventory");

            this.level = (int) map.get("level");
            this.chance = (double) map.get("chanceOfSpawning");

            WitheredUtil.debug("Successfully loaded carConfig " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load carConfig " + ((String) map.get("name")).replace("&", "§"));
        }
    }

    public static CarSerializable deserialize(Map<String, Object> map) {
        CarSerializable c = new CarSerializable(map);
        carTypes.add(c);
        return c;
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static ArrayList<CarSerializable> getCarTypes() {
        return carTypes;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean hasInventory() {
        return hasInventory;
    }

    public int getMaterialId() {
        return materialId;
    }

    public double getHealth() {
        return health;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public int getLevel() {
        return level;
    }

    public double getMaxSpeedOffRoad() {
        return maxSpeedOffRoad;
    }

    public double getChance() {
        return chance;
    }
}
