package me.Math0424.WitheredControl;

import me.Math0424.WitheredControl.Arenas.Arena;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HeartBeat {
	
	public HeartBeat() {
		
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					
					for (Arena a : Arena.getArenas()) {
						for (Player p : a.getSpectatorsInGame().keySet()) {
							p.setGameMode(GameMode.SPECTATOR);
							p.setSpectatorTarget(a.getSpectatorsInGame().get(p));
						}
					}
					
					for (Arena a : Arena.getArenas()) {
						for (Player p : a.getSpectatorTime().keySet()) {
							a.getSpectatorTime().put(p, a.getSpectatorTime().get(p) - 1);
							if (a.getSpectatorTime().get(p) == 0) {
								a.getSpectatorsInGame().remove(p);
								a.getSpectatorTime().remove(p);
								p.setGameMode(GameMode.SURVIVAL);
								a.spawnPlayerInArena(p, false);
							}
						}
					}
					
				} catch(Exception e) {
					
				}
			}
		}.runTaskTimer(WitheredControl.getPlugin(), 1, 1);
		
	}
	
}
