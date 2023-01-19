package me.Math0424.WitheredControl.Commands;

import me.Math0424.WitheredControl.Arenas.*;
import me.Math0424.WitheredControl.Data.PlayerData;
import me.Math0424.WitheredControl.Data.SaveFiles;
import me.Math0424.WitheredControl.SignSpawner.SignData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlCommands implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			switch (args[0].toLowerCase()) {
			case "arena":
				if (checkPerms(p, "witheredgg.arenaAdmin")) {
					if (args[1] != null) {
						switch(args[1].toLowerCase()) {
						case "create":
							if (args.length == 2) {
								p.sendMessage(ChatColor.GREEN + "name type maxPlayers");
								break;
							}
							if (args[2] != null) {
								try {
									String name = args[2];
									ArenaType type = ArenaType.valueOf(args[3]);
									int maxPlayers = Integer.parseInt(args[4]);
									for (Arena ar : Arena.getArenas()) {
										if (ar.getName().equals(name)) {
											p.sendMessage(ChatColor.RED + "arena name is use!");
											return false;
										}
									}
									if (type == ArenaType.FFA) {
										new ArenaFFA(name, type, maxPlayers);
									} else if(type == ArenaType.POINTS) {
										new ArenaPOINT(name, type, maxPlayers);
									} else if(type == ArenaType.TEAMS) {
										new ArenaTEAM(name, type, maxPlayers);
									}
									p.sendMessage(ChatColor.GREEN + "Created arena");
									p.sendMessage(ChatColor.GREEN + "finish up arena setup");
									p.sendMessage(ChatColor.GREEN + "/witheredgg arena setup [name] setSpawnPoint");
									p.sendMessage(ChatColor.GREEN + "/witheredgg arena setup [name] setLobby");
								} catch (Exception e) {
									p.sendMessage(ChatColor.GREEN + "name teamMode maxPlayers");
								}
							}
							break;
						case "remove":
							if (Arena.getArenaByName(args[2]) != null) {
								Arena a = Arena.getArenaByName(args[2]);
								Arena.getArenas().remove(a);
								for (SignData sign : SignData.signData) {
									if (sign.getName().equals(a.getName())) {
										SignData.signData.remove(sign);
										break;
									}
								}
								SaveFiles.saveArena();
								SaveFiles.saveSignData();
								p.sendMessage(ChatColor.RED + "Removed arena " + a.getName());
							}
							break;
						case "setup":
							if (args[2] != null) {
								Arena a = Arena.getArenaByName(args[2]);
								if (a != null) {
									switch (args[3].toLowerCase()) {
									case "setspawnpoint":
										if (a.getType() == ArenaType.FFA) {
											a.getSpawnLocations().put(a.getSpawnLocations().size(), p.getLocation());
											p.sendMessage(ChatColor.GREEN + "saved a spawn point at players location");
										} else if(a.getType() == ArenaType.POINTS || a.getType() == ArenaType.TEAMS) {
											if (args[4] != null && args[4].toLowerCase().equals("red")) {
												a.getSpawnLocations().put(1, p.getLocation());
												p.sendMessage(ChatColor.GREEN + "saved red spawn");
											} else if(args[4] != null && args[4].toLowerCase().equals("blue")) {
												a.getSpawnLocations().put(2, p.getLocation());
												p.sendMessage(ChatColor.GREEN + "saved blue spawn");
											}
										}
										SaveFiles.saveArena();
										break;
									case "setlobby":
										a.setLobbySpawn(p.getLocation());
										p.sendMessage(ChatColor.GREEN + "set lobby to players location");
										SaveFiles.saveArena();
										break;
									}
								}
							}
							break;
						}
					}
				}
				break;
			case "stats":
				if (args.length >= 2) {
					if (PlayerData.getData(args[1]) != null) {
						printStats(p, PlayerData.getData(args[1]));
						break;
					}
					p.sendMessage(ChatColor.RED + "No playerData found!");
				} else {
					printStats(p, PlayerData.getData(p.getName()));
				}
				break;
			case "forcestart":
				if (checkPerms(p, "witheredgg.arenaAdmin")) {
					Arena a = Arena.getArena(p);
					if (a != null) {
						if(!a.isGameRunning() && !a.isGameEnding()) {
							a.startGame();
						} else {
							p.sendMessage(ChatColor.RED + "Arena is in progress");
						}
					} else {
						p.sendMessage(ChatColor.RED + "You are not in an arena");
					}
				}
				break;
			default:

			}

		}
		return false;
	}

	public void printStats(Player p, PlayerData data) {
		p.sendMessage(ChatColor.GOLD + "Showing player data for " + ChatColor.GREEN + data.getName());
		p.sendMessage(ChatColor.AQUA + "K/D: " + Double.valueOf(data.getKills()) / Double.valueOf(data.getDeaths()));
		p.sendMessage(ChatColor.AQUA + "Deaths: " + data.getDeaths());
		p.sendMessage(ChatColor.AQUA + "Kills: " + data.getKills());
		p.sendMessage(ChatColor.AQUA + "BulletsFired: " + data.getBulletsFired());
		p.sendMessage(ChatColor.AQUA + "Games won: " + data.getWins());
		return;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;
			List<String> possible = new ArrayList<>();
			if (args.length >= 5 && args[1].toLowerCase().equals("setup") && args[3].toLowerCase().equals("setspawnpoint")) {
				Arena a = Arena.getArenaByName(args[2]);
				if (a != null) {
					if(a.getType() == ArenaType.POINTS || a.getType() == ArenaType.TEAMS) {
						possible.add("red");
						possible.add("blue");
					}
				} else {
					possible.add("invalid arena!");
				}
				return finish(possible, args[4]);
			} else if (args.length >= 4 && args[1].toLowerCase().equals("setup")) {
				Arena a = Arena.getArenaByName(args[2]);
				if (a != null) {
					possible.add("setspawnpoint");
					possible.add("setlobby");
				} else {
					possible.add("invalid arena!");
				}
				return finish(possible, args[3]);
			} else if (args.length >= 3) {
				switch (args[1].toLowerCase()) {
				case "setup":
					case "remove":
						for (Arena a : Arena.getArenas()) {
						possible.add(a.getName());
					}
					break;
				case "create":
					if (args.length == 3) {
						possible.add("name arenaType maxPlayers");
					} else if (args.length == 4) {
						List<ArenaType> list = Arrays.asList(ArenaType.values());
						for (ArenaType t : list) {
							possible.add(t.toString());
						}
					} else if (args.length == 5) {
						possible.add("maxPlayers");
					}
					return possible;
				}
				return finish(possible, args[2]);
			} else if (args.length >= 2) {
				if (args[0].toLowerCase().equals("arena")) {
					possible.add("setup");
					possible.add("create");
					possible.add("remove");
				}
				return finish(possible, args[1]);
			} else if (args.length >= 1) {
				possible.add("stats");
				if (checkPerms(p, "witheredgg.arenaAdmin")) {
					possible.add("arena");
					possible.add("forcestart");
				}
				return finish(possible, args[0]);
			}
		}
		return null;
	}

	public List<String> finish(List<String> possible, String args) {
		List<String> results = new ArrayList<String>();
		if (args.contains("\\")) {
			return Arrays.asList("Unknown command.");
		}
		for(String string : possible)
		{
			Pattern pattern = Pattern.compile(args);
			Matcher matcher = pattern.matcher(string);	
			while(matcher.find())
			{
				results.add(string);
			}
		}
		if(!results.isEmpty()) return results;
		return Arrays.asList("Unknown command.");
	}

	public boolean checkPerms(Player p, String s) {
		if (p.hasPermission(s) || p.isOp() || p.hasPermission("withered.admin")) {
			return true;
		}
		return false;
	}

}