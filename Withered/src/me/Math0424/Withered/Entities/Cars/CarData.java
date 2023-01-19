package me.Math0424.Withered.Entities.Cars;

import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Gameplay.Cars.CarSerializable;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CarData implements ConfigurationSerializable {

    private String uuid;
    private String name;
    private Material material;
    private int materialId;
    private Inventory inventory;
    private double health;
    private double acceleration;
    private double maxSpeed;
    private double maxSpeedOffRoad;

    public CarData(CarSerializable car) {
        this.name = car.getName();
        this.material = car.getMaterial();
        this.materialId = car.getMaterialId();
        this.inventory = car.hasInventory() ? Bukkit.createInventory(null, 9, name) : null;
        this.health = car.getHealth();
        this.acceleration = car.getAcceleration();
        this.maxSpeed = car.getMaxSpeed();
        this.maxSpeedOffRoad = car.getMaxSpeedOffRoad();
    }

    public CarData(String name, Material material, int materialId, double health, double acceleration, double maxSpeed, double maxSpeedOffRoad) {
        this.name = name;
        this.material = material;
        this.materialId = materialId;
        this.inventory = Bukkit.createInventory(null, 9, name);
        this.health = health;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.maxSpeedOffRoad = maxSpeedOffRoad;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean hasInventory() {
        return inventory != null;
    }

    public String getUuid() {
        return uuid;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMaterialId() {
        return materialId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getMaxSpeedOffRoad() {
        return maxSpeedOffRoad;
    }


    public CarData(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.uuid = (String) map.get("uuid");
            this.material = Material.valueOf((String) map.get("material"));
            this.materialId = (int) map.get("materialId");
            this.health = (double) map.get("health");
            this.acceleration = (double) map.get("acceleration");
            this.maxSpeed = (double) map.get("maxSpeed");
            this.maxSpeedOffRoad = (double) map.get("maxSpeedOffRoad");

            this.inventory = map.get("inventory") != null ? InventoryUtil.fromString((String) map.get("inventory")) : null;

            WitheredUtil.debug("Successfully loaded carData with uuid of " + uuid);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load carData with uuid of " + uuid);
        }
    }

    public static CarData deserialize(Map<String, Object> map) {
        CarData c = new CarData(map);
        if (c.getUuid() != null) {
            MobHandler.getCars().put(c.uuid, c);
        }
        return c;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("uuid", uuid);
        map.put("material", material.name());
        map.put("materialId", materialId);
        map.put("health", health);
        map.put("acceleration", acceleration);
        map.put("maxSpeed", maxSpeed);
        map.put("maxSpeedOffRoad", maxSpeedOffRoad);

        if (inventory != null) {
            map.put("inventory", InventoryUtil.toString(inventory));
        }

        return map;
    }


}
