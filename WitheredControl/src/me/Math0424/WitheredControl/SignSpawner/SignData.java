package me.Math0424.WitheredControl.SignSpawner;

import me.Math0424.WitheredControl.Util.WitheredUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignData implements ConfigurationSerializable {
	
	public static ArrayList<SignData> signData = new ArrayList<SignData>();
	
	private Location signLoc;
	private String name;
	private String option;
	
	public SignData(Location signLoc, String name, String option) {
		this.name = name;
		this.signLoc = signLoc;
		this.option = option;
	}
	public String getName() {
		return name;
	}
	public Location getSignLoc() {
		return signLoc;
	}
	public String getOption() {
		return option;
	}
	
	public SignData(Map<String, Object> map) {
		try {
			this.name = (String) map.get("name");
			this.option = (String) map.get("option");
			this.signLoc = (Location) map.get("signLoc");
		} catch (Exception e) {
			WitheredUtil.info(ChatColor.RED + "Failed to load SignData");
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
		map.put("name", name);
		map.put("signLoc", signLoc);
		map.put("option", option);
		return map;
	}
	
	
}
