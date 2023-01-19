package me.Math0424.WitheredControl.SignSpawner;

import me.Math0424.WitheredControl.Arenas.Arena;
import me.Math0424.WitheredControl.Data.SaveFiles;
import me.Math0424.WitheredControl.Util.WitheredUtil;
import me.Math0424.WitheredControl.WitheredControl;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
				if (WitheredUtil.isSameBlockLocation(s.getSignLoc(), loc)) {
					Player p = e.getPlayer();
					
					Arena a = Arena.getArenaByName(s.getName());
					if (a != null) {
						if (s.getOption().equals("join") && Arena.getArena(p) == null) {
							if (a.getLobbySpawn() == null) {
								p.sendMessage("No lobby");
								return;
							}
							if (a.getSpawnLocations().size() == 0) {
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
	
	public void startSignTimer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (SignData s : SignData.signData) {
					try {
						
						Arena a = Arena.getArenaByName(s.getName());
						Sign sign = (Sign) s.getSignLoc().getBlock().getState();
						if (sign == null) {
							s.getSignLoc().getBlock().setType(Material.OAK_SIGN);
							sign = (Sign) s.getSignLoc().getBlock().getState();
						}
						
						sign.setLine(0, ChatColor.BLUE + "[Withered:GG]");
						sign.setLine(1, ChatColor.AQUA + a.getName());
						if (s.getOption().equals("join")) {
							sign.setLine(2, ChatColor.GREEN + "join");
							sign.setLine(3, ChatColor.GREEN + String.valueOf(a.getPlayersInGame().size()) + "/" + a.getMaxPlayers());
							sign.setLine(3, ChatColor.GREEN + String.valueOf(a.getPlayersInGame().size()) + "/" + a.getMaxPlayers() + " - " + a.getType().toString());
							sign.update();
						} else {
							sign.setLine(2, ChatColor.RED + "leave");
						}
						sign.update();
						
					} catch (Exception e) {
						try {
							WitheredUtil.info("Error while finding sign at " + s.getSignLoc().getX() + " " + s.getSignLoc().getY() + " " + s.getSignLoc().getZ());
							WitheredUtil.info("Place a sign at that location to fix this error");
							WitheredUtil.info("If you want to break it shift left click after placing sign");
						} catch (Exception f) {
							WitheredUtil.info("Error while loading error report for a sign...    lol");
							f.printStackTrace();
						}
					}
	
				}
			}
		}.runTaskTimer(WitheredControl.getPlugin(), 20*5, 20*5);
	}
	
	
}
