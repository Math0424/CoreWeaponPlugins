package me.Math0424.WitheredControl;

import me.Math0424.WitheredControl.Util.WitheredUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public enum Config {
	
	DEBUG("showDebug", false),
	RESOURCEPACKFORCE("forceResourcePack", true),
	//certutil -hashfile C:\Users\[user]\AppData\Roaming\.minecraft\resourcepacks\Withered.zip SHA1
	RESOURCEPACK("resourcePack", "https://www.dropbox.com/s/lpguq2rsgm091fr/Withered.zip?dl=1"),
	RESOURCEPACKHASH("resourcePackHash", "d57f78fd01c6227e5fa0213bfe9243589e738c21");
	
	private String location;
	private Object value;
	private static FileConfiguration LANG;
	
	Config(String location, Object val) {
		this.location = location;
		this.value = val;
	}
	
	public String getStrVal() {
		return LANG.getObject(this.getLocation(), String.class);
	}
	
	public int getIntVal() {
		return LANG.getObject(this.getLocation(), Integer.class);
	}
	
	public Boolean getBoolVal() {
		return LANG.getObject(this.getLocation(), Boolean.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getStringArrayVal() {
		return LANG.getObject(this.getLocation(), List.class);
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	private static File configFile;
    private static FileConfiguration config;
    
    public static void load() {
    	configFile = new File(WitheredControl.getPlugin().getDataFolder(), "Config/config.yml");
    	if (!configFile.exists()) {
    		configFile.getParentFile().mkdirs();
    		 WitheredControl.getPlugin().saveResource("Config/config.yml", false);
    	}
    	config = new YamlConfiguration();
    	try {
    		config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
		for(Config lang : Config.values()) {
			if (config.getString(lang.getLocation()) == null) {
	        	config.set(lang.getLocation(), lang.getValue());
	        	WitheredUtil.info(ChatColor.RED + "Missing config option '" + lang.getLocation() + "' setting to default option: " + lang.getValue());
	        }
	    }
		LANG = config;
	}
	
}
