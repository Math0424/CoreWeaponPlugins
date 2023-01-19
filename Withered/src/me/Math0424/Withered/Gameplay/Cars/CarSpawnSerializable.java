package me.Math0424.Withered.Gameplay.Cars;

import me.Math0424.Withered.Util.WitheredUtil;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CarSpawnSerializable implements ConfigurationSerializable {

    public static ArrayList<CarSpawnSerializable> carSpawns = new ArrayList<>();

    private Location location;
    private Integer level;
    private Integer radius;

    public CarSpawnSerializable(Location loc, Integer level, Integer radius) {
        this.level = level;
        this.location = loc;
        this.radius = radius;
        carSpawns.add(this);
    }

    public CarSpawnSerializable(Map<String, Object> map) {
        try {
            this.location = (Location) map.get("location");
            this.radius = (int) map.get("radius");
            this.level = (Integer) map.get("level");

            WitheredUtil.debug("Successfully loaded carSpawn at at " + location);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load carSpawn " + map.get(location));
        }
    }

    public static CarSpawnSerializable deserialize(Map<String, Object> map) {
        CarSpawnSerializable c = new CarSpawnSerializable(map);
        carSpawns.add(c);
        return c;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("level", level);
        map.put("radius", radius);
        return map;
    }

    public Location getLocation() {
        return location;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getRadius() {
        return radius;
    }
}
