package me.Math0424.WitheredGunGame.Data;

import me.Math0424.WitheredGunGame.WitheredGunGame;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LoadFiles {
    
    public static File signDataFile;
    public static FileConfiguration signData;
    
    public static File arenaDataFile;
    public static FileConfiguration arenaData;
    
    public static void load() {
    	
    	arenaDataFile = new File(WitheredGunGame.getPlugin().getDataFolder(), "Data/arenaData.yml");
    	if (!arenaDataFile.exists()) {
    		arenaDataFile.getParentFile().mkdirs();
            WitheredGunGame.getPlugin().saveResource("Data/arenaData.yml", false);
    	}
    	arenaData = new YamlConfiguration();
    	try {
    		arenaData.load(arenaDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    	
    	signDataFile = new File(WitheredGunGame.getPlugin().getDataFolder(), "Data/signData.yml");
    	if (!signDataFile.exists()) {
    		signDataFile.getParentFile().mkdirs();
            WitheredGunGame.getPlugin().saveResource("Data/signData.yml", false);
    	}
    	signData = new YamlConfiguration();
    	try {
    		signData.load(signDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    	
    	
    }
    
}
