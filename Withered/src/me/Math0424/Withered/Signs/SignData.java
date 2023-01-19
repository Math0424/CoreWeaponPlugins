package me.Math0424.Withered.Signs;

import me.Math0424.Withered.Util.WitheredUtil;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class SignData implements ConfigurationSerializable {

    public static ArrayList<SignData> signData = new ArrayList<SignData>();

    private Location spawnLocation;
    private Location signLoc;
    private Integer spread;
    private String townName;

    public SignData(Location signLoc, Location spawnLocation, Integer spread, String townName) {
        this.spawnLocation = spawnLocation;
        this.spread = spread;
        this.townName = townName;
        this.signLoc = signLoc;
        signData.add(this);
    }

    public SignData(Map<String, Object> map) {
        try {
            this.townName = (String) map.get("townName");
            this.spread = (Integer) map.get("spread");
            this.spawnLocation = (Location) map.get("spawnLocation");
            this.signLoc = (Location) map.get("signLoc");
        } catch (Exception e) {
            WitheredUtil.log(Level.SEVERE, "Failed to load SignData");
            e.printStackTrace();
        }

    }

    public static SignData deserialize(Map<String, Object> map) {
        SignData a = new SignData(map);
        signData.add(a);
        return a;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("townName", townName);
        map.put("spread", spread);
        map.put("spawnLocation", spawnLocation);
        map.put("signLoc", signLoc);

        return map;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getSignLoc() {
        return signLoc;
    }

    public Integer getSpread() {
        return spread;
    }

    public String getTownName() {
        return townName;
    }
}
