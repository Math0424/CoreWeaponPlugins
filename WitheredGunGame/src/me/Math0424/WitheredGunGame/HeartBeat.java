package me.Math0424.WitheredGunGame;

import me.Math0424.CoreWeapons.Data.PlayerData;
import me.Math0424.WitheredGunGame.Arenas.Arena;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HeartBeat {
	
	public HeartBeat() {

		new BukkitRunnable() {
			int tick;
			public void run() {

				tick++;

				try {
					for (Arena a : Arena.getArenas()) {
						for (Player p : a.getDead().keySet()) {
							a.getDead().put(p, a.getDead().get(p) - 1);
							if (a.getDead().get(p) == 0) {
								a.getDead().remove(p);
								a.playerRespawned(p);
							}
						}
					}
					
				} catch(Exception ignored) {}

				if (tick % 6000 == 0) {
					PlayerData.saveAllPlayerData();
				}

			}
		}.runTaskTimer(WitheredGunGame.getPlugin(), 1, 1);
		
	}
	
}
