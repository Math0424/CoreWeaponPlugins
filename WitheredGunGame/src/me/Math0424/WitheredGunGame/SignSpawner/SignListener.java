package me.Math0424.WitheredGunGame.SignSpawner;

import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.WitheredGunGame.Arenas.Arena;
import me.Math0424.WitheredGunGame.Data.SaveFiles;
import me.Math0424.WitheredGunGame.Util.WitheredUtil;
import me.Math0424.WitheredGunGame.WitheredGunGame;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class SignListener {
	
	public void signChangeEvent(SignChangeEvent e) {
		String[] line = e.getLines();
		if (line[0].equalsIgnoreCase("[WitheredGG]")) {
			if (line[1] != null && line[2] != null) {
				
				String name = line[1];
				String option = line[2].toLowerCase();
				if(Arena.getArenaByName(name) == null) {
					e.getPlayer().sendMessage(ChatColor.GREEN + "Arena not found");
					return;
				}
				if (option.equals("join") || option.equals("leave")) {
					SignData.signData.add(new SignData(e.getBlock().getLocation(), name, option));
					e.getPlayer().sendMessage(ChatColor.GREEN + "Created sign!");
					
					e.setLine(0, ChatColor.BLUE + "[Withered:GG]");
					e.setLine(1, ChatColor.AQUA + name);
					if (option.equals("join")) {
						e.setLine(2, ChatColor.GREEN + option);
					} else {
						e.setLine(2, ChatColor.RED + option);
					}
					SaveFiles.saveSignData();
				} else {
					e.getPlayer().sendMessage(ChatColor.GREEN + "join or leave");
					return;
				}
			}
		}
	}
	
	public void playerInteractEvent(PlayerInteractEvent e) {
		if (e.getClickedBlock() != null && Tag.SIGNS.getValues().contains(e.getClickedBlock().getType())) {
			Location loc = e.getClickedBlock().getLocation();
			for (SignData s : SignData.signData) {
				if (MyUtil.isSameBlockLocation(s.getSignLoc(), loc)) {
					Player p = e.getPlayer();
					
					Arena a = Arena.getArenaByName(s.getName());
					if (a != null) {
						if (s.getOption().equals("join") && Arena.getArena(p) == null) {
							if (a.getLobbyLoc() == null) {
								p.sendMessage("No lobby");
								return;
							}
							if (a.getPlayerSpawns().size() < 2) {
								p.sendMessage("No spawnpoints!");
								return;
							}
							a.addPlayer(p, false);
						} else if (s.getOption().equals("leave") && Arena.getArena(p) != null) {
							a.removePlayer(p, false);
						}
					}
				}
			}
		}
	}
	
	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (SignData s : SignData.signData) {
					try {
						
						Arena a = Arena.getArenaByName(s.getName());
						Sign sign = (Sign) s.getSignLoc().getBlock().getState();

						sign.setLine(0, ChatColor.BLUE + "[Withered:GG]");
						sign.setLine(1, ChatColor.AQUA + a.getName());
						if (s.getOption().equals("join")) {
							sign.setLine(2, ChatColor.GREEN + "join");
							sign.setLine(3, ChatColor.GREEN + String.valueOf(a.getInGame().size()) + "/" + a.getMaxPlayers() + " - " + a.getType().toString());
							sign.update();
						} else {
							sign.setLine(2, ChatColor.RED + "leave");
						}
						sign.update();
						
					} catch (Exception e) {
						try {
							WitheredUtil.log(Level.INFO, "Error while finding sign at " + s.getSignLoc().getX() + " " + s.getSignLoc().getY() + " " + s.getSignLoc().getZ());
							WitheredUtil.log(Level.INFO, "Place a sign at that location to fix this error");
							WitheredUtil.log(Level.INFO, "If you want to break it shift left click after placing sign");
						} catch (Exception f) {
							WitheredUtil.log(Level.INFO, "Error while loading error report for a sign...    lol");
							f.printStackTrace();
						}
					}
	
				}
			}
		}.runTaskTimer(WitheredGunGame.getPlugin(), 20*5, 20*5);
	}
	
	
}
