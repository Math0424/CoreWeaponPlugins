package me.Math0424.WitheredControl.Data;


import me.Math0424.WitheredControl.Arenas.Arena;
import me.Math0424.WitheredControl.SignSpawner.SignData;
import me.Math0424.WitheredControl.WitheredControl;

public class SaveFiles {
	
	public SaveFiles() {
		
		savePlayerData();
		saveSignData();
		saveArena();
		
	}
	
	public static void saveSignData() {
		LoadFiles.signData.set("spawns", SignData.signData);
		try {
			LoadFiles.signData.save(LoadFiles.signDataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void savePlayerData() {
		LoadFiles.playerData.set("players", WitheredControl.getPlugin().playerData);
		try {
			LoadFiles.playerData.save(LoadFiles.playerDataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveArena() {
		LoadFiles.arenaData.set("arenas", Arena.getArenas());
		try {
			LoadFiles.arenaData.save(LoadFiles.arenaDataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
