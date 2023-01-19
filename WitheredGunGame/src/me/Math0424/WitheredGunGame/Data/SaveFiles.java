package me.Math0424.WitheredGunGame.Data;

import me.Math0424.WitheredGunGame.Arenas.Arena;
import me.Math0424.WitheredGunGame.SignSpawner.SignData;

public class SaveFiles {
	
	public SaveFiles() {
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
	public static void saveArena() {
		LoadFiles.arenaData.set("arenas", Arena.getArenas());
		try {
			LoadFiles.arenaData.save(LoadFiles.arenaDataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
