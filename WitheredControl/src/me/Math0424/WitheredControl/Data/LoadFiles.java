package me.Math0424.WitheredControl.Data;

import me.Math0424.WitheredControl.WitheredControl;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LoadFiles {
    
    public static File playerDataFile;
    public static FileConfiguration playerData;
    
    public static File signDataFile;
    public static FileConfiguration signData;
    
    public static File arenaDataFile;
    public static FileConfiguration arenaData;
    
    public LoadFiles() {
    	
    	arenaDataFile = new File(WitheredControl.getPlugin().getDataFolder(), "Data/arenaData.yml");
    	if (!arenaDataFile.exists()) {
    		arenaDataFile.getParentFile().mkdirs();
			WitheredControl.getPlugin().saveResource("Data/arenaData.yml", false);
    	}
    	arenaData = new YamlConfiguration();
    	try {
    		arenaData.load(arenaDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    	
    	playerDataFile = new File(WitheredControl.getPlugin().getDataFolder(), "Data/playerData.yml");
    	if (!playerDataFile.exists()) {
    		playerDataFile.getParentFile().mkdirs();
			WitheredControl.getPlugin().saveResource("Data/playerData.yml", false);
    	}
    	playerData = new YamlConfiguration();
    	try {
    		playerData.load(playerDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    	
    	signDataFile = new File(WitheredControl.getPlugin().getDataFolder(), "Data/signData.yml");
    	if (!signDataFile.exists()) {
    		signDataFile.getParentFile().mkdirs();
			WitheredControl.getPlugin().saveResource("Data/signData.yml", false);
    	}
    	signData = new YamlConfiguration();
    	try {
    		signData.load(signDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    	
    	
    }
    
}
