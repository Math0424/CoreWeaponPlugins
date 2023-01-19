package me.Math0424.WitheredGunGame.Commands;

import me.Math0424.WitheredGunGame.Arenas.*;
import me.Math0424.WitheredGunGame.Data.PlayerData;
import me.Math0424.WitheredGunGame.Data.SaveFiles;
import me.Math0424.WitheredGunGame.SignSpawner.SignData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GunGameCommands implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length <= 0) {
				return false;
			}
			switch (args[0].toLowerCase()) {
			case "arena":
				if (checkPerms(p, "witheredgg.arenaAdmin")) {
					if (args.length >= 3) {
						switch(args[1].toLowerCase()) {
						case "create":
							if (args.length >= 6) {
								try {

									ArenaType type = ArenaType.valueOf(args[2]);
									String name = args[3];
									int minPlayers = Integer.parseInt(args[4]);
									int maxPlayers = Integer.parseInt(args[5]);

									minPlayers = Math.min(minPlayers, maxPlayers);

									for (Arena ar : Arena.getArenas()) {
										if (ar.getName().equals(name)) {
											p.sendMessage(ChatColor.RED + "arena name is use!");
											return false;
										}
									}
									if (type == ArenaType.FFA) {
										new ArenaFFA(name, type, minPlayers, maxPlayers);
									} else if(type == ArenaType.POINTS) {
										new ArenaPOINT(name, type, minPlayers, maxPlayers);
									} else if(type == ArenaType.TEAMS) {
										new ArenaTEAM(name, type, minPlayers, maxPlayers);
									}
									p.sendMessage(ChatColor.GREEN + "Created arena");
									p.sendMessage(ChatColor.GREEN + "finish up arena setup with");
									p.sendMessage(ChatColor.YELLOW + "/witheredgg arena setup [name] setSpawnPoint");
									p.sendMessage(ChatColor.YELLOW + "/witheredgg arena setup [name] setLobby");

								} catch (Exception e) {
									p.sendMessage(ChatColor.RED + "name minPlayers maxPlayers");
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
												a.getPlayerSpawns().add(p.getLocation());
												p.sendMessage(ChatColor.YELLOW + "Saved a spawn point at players location (" + a.getPlayerSpawns().size() + ")");
											} else if(a.getType() == ArenaType.POINTS || a.getType() == ArenaType.TEAMS && args.length >= 5) {
												if (args[4].toLowerCase().equals("red")) {
												    if (a.getPlayerSpawns().size() == 0) {
                                                        a.getPlayerSpawns().add(p.getLocation());
                                                    } else {
                                                        a.getPlayerSpawns().set(0, p.getLocation());
                                                    }
													p.sendMessage(ChatColor.RED + "Saved red spawn");
												} else if(args[4].toLowerCase().equals("blue")) {
                                                    if (a.getPlayerSpawns().size() <= 1) {
                                                        a.getPlayerSpawns().add(p.getLocation());
                                                    } else {
                                                        a.getPlayerSpawns().set(1, p.getLocation());
                                                    }
													p.sendMessage(ChatColor.BLUE + "Saved blue spawn");
												}
											}
											SaveFiles.saveArena();
											break;
										case "setlobby":
											a.setLobbyLoc(p.getLocation());
											p.sendMessage(ChatColor.YELLOW + "Set lobby to players location");
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
					if (PlayerData.getPlayerData(Bukkit.getPlayer(args[1])) != null) {
						printStats(p, PlayerData.getPlayerData(Bukkit.getPlayer(args[1])));
						break;
					}
					p.sendMessage(ChatColor.RED + "No playerData found!");
				} else {
					printStats(p, PlayerData.getPlayerData(p));
				}
				break;
			case "forcestart":
				if (checkPerms(p, "witheredgg.arenaAdmin")) {
					Arena a = Arena.getArena(p);
					if (a != null) {
						if(a.getPhase() == Arena.Phase.STARTING) {
							a.setStartingSeconds(0);
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
		p.sendMessage(ChatColor.GOLD + "Showing player data for " + ChatColor.GREEN + p.getName());
		p.sendMessage(ChatColor.AQUA + "K/D: " + Double.valueOf(data.getKills()) / Double.valueOf(data.getDeaths()));
		p.sendMessage(ChatColor.AQUA + "Deaths: " + data.getDeaths());
		p.sendMessage(ChatColor.AQUA + "Kills: " + data.getKills());
		p.sendMessage(ChatColor.AQUA + "BulletsFired: " + data.getBulletsFired());
		p.sendMessage(ChatColor.AQUA + "Games won: " + data.getWins());
		p.sendMessage(ChatColor.AQUA + "Games lost: " + data.getLosses());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			String currentArg = args[args.length-1];
			List<String> possible = new ArrayList<>();
			if (checkPerms(p, "witheredgg.admin")) {
				if (args.length == 1)  {
					possible.add("arena");
					possible.add("stats");
					possible.add("forcestart");
				} else if (args.length == 2 && args[0].toLowerCase().equals("arena")) {
					possible.add("create");
					possible.add("remove");
					possible.add("setup");
				} else if (args.length == 3) {
					switch (args[1].toLowerCase()) {
						case "setup":
						case "remove":
							for (Arena a : Arena.getArenas()) {
								possible.add(a.getName());
							}
							break;
						case "create":
							possible.add("FFA");
							possible.add("POINTS");
							possible.add("TEAMS");
							break;
					}
				} else if(args.length == 4) {
					if (args[2].toUpperCase().equals("FFA") || args[2].toUpperCase().equals("POINTS") || args[2].toUpperCase().equals("TEAMS")) {
						possible.add("name minPlayers maxPlayers");
					} else if (args[1].toLowerCase().equals("setup")) {
						possible.add("setSpawnPoint");
						possible.add("setLobby");
					}
				} else if(args.length == 5) {
					Arena arena = Arena.getArenaByName(args[2]);
					if (args[3].toLowerCase().equals("setspawnpoint") && arena != null) {
						if (arena.getType() == ArenaType.POINTS || arena.getType() == ArenaType.TEAMS) {
							possible.add("red");
							possible.add("blue");
						}
					} else if (args[1].toLowerCase().equals("create")) {
						possible.add("minPlayers maxPlayers");
					}
				} else if(args.length == 6) {
					if (args[1].toLowerCase().equals("create")) {
						possible.add("maxPlayers");
					}
				}
				return finish(possible, currentArg);
			}
		}
		return null;
	}

	public static List<String> finish(List<String> possible, String args) {
		List<String> results = new ArrayList<String>();
		StringUtil.copyPartialMatches(args, possible, results);
		Collections.sort(results);
		return results;
	}

	public boolean checkPerms(Player p, String s) {
        return p.hasPermission(s);
    }

}