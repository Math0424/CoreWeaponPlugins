package me.Math0424.WitheredGunGame.Arenas;

import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.WitheredGunGame.Data.PlayerData;
import me.Math0424.WitheredGunGame.Guns.GunListeners;
import me.Math0424.WitheredGunGame.Lang;
import me.Math0424.WitheredGunGame.Util.WitheredUtil;
import me.Math0424.WitheredGunGame.WitheredGunGame;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ArenaTEAM extends Arena {

    private final Team teamBlue;
    private final Team teamRed;

    public ArenaTEAM(String name, ArenaType type, int maxPlayers, int minPlayers) {
        super(name, type, maxPlayers, minPlayers);
        teamBlue = createTeam("Team blue", ChatColor.BLUE, false);
        teamRed = createTeam("Team red", ChatColor.RED, false);
    }

    @Override
    public void addPlayer(Player p, boolean silent) {
        if (inGame.size() > maxPlayers) {
            p.sendMessage(Lang.ARENAFULL.toString());
            return;
        } else if(phase == Phase.ENDING) {
            p.sendMessage(Lang.ARENAENDING.toString());
            return;
        }
        inGame.put(p, 1);
        enterLocation.put(p, p.getLocation());
        p.getInventory().clear();
        p.setScoreboard(scoreboard);
        p.setGameMode(GameMode.ADVENTURE);
        setHelmet(p, Color.GRAY, ChatColor.GRAY+"Undecided");
        sendMessage(Lang.ARENAPlAYERJOIN.toString().replace("{player}", p.getName()));
        if (phase != Phase.RUNNING) {
            p.teleport(lobbyLoc);
            checkStartGame();
        } else {
            if (teamBlue.getEntries().size() <= teamRed.getEntries().size()) {
                teamBlue.addEntry(p.getName());
            } else {
                teamRed.addEntry(p.getName());
            }
            spawnPlayerInArena(p, false);
        }
    }

    @Override
    public void removePlayer(Player p, boolean silent) {
        teamRed.removeEntry(p.getName());
        teamBlue.removeEntry(p.getName());
        scoreboard.resetScores(p.getName());
        p.setHealth(20);
        p.setFoodLevel(20);
        p.teleport(enterLocation.get(p));

        p.getInventory().clear();
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        inGame.remove(p);
        for (Player o : inGame.keySet()) {
            o.showPlayer(WitheredGunGame.getPlugin(), p);
        }

        if (!silent) {
            sendMessage(Lang.ARENAPLAYERLEAVE.toString().replace("{player}", p.getName()), Sound.ENTITY_ARROW_HIT_PLAYER);
        }

        checkEndGame();
    }

    @Override
    public void playerDied(Player p) {
        p.getInventory().setItem(0, null);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1, 1);
        p.sendTitle(ChatColor.RED + "You have died", ChatColor.YELLOW +"respawning...", 10, 60, 10);
    }

    @Override
    public void playerRespawned(Player p) {
        updateGun(p);
        spawnPlayerInArena(p, false);
        for (Player o : inGame.keySet()) {
            o.showPlayer(WitheredGunGame.getPlugin(), p);
        }
    }

    //endGame stuff

    @Override
    public void startEndGame() {
        phase = Phase.ENDING;

        Player winner = null;
        for (Player p : inGame.keySet()) {
            p.setHealth(20);
            p.getInventory().setItem(0, null);
            if (inGame.get(p) > GunListeners.getGuns().size()) {
                winner = p;
            } else {
                PlayerData.getPlayerData(p).addToLosses();
            }
        }

        if (winner != null) {
            PlayerData.getPlayerData(winner).addToWins();
            String teamName = teamRed.hasEntry(winner.getName()) ? "red" : "blue";
            sendMessage(Lang.ARENAGAMEOVER.toString().replace("{winner}", "Team " + teamName));
            sendMessage(Lang.ARENAMPVWINNER.toString().replace("{player}", winner.getName()), Sound.ENTITY_ENDER_DRAGON_GROWL);
        }

        new BukkitRunnable() {
            public void run() {
                stopGame();
            }
        }.runTaskLater(WitheredGunGame.getPlugin(), 20*10);

    }

    @Override
    public void checkEndGame() {
        if (phase == Phase.RUNNING) {

            if(teamBlue.getEntries().size() == 0 || teamRed.getEntries().size() == 0) {
                sendMessage(Lang.ARENANOPLAYERS.toString());
                stopGame();
                return;
            }

            if (inGame.size() < minPlayers) {
                sendMessage(Lang.ARENANOPLAYERS.toString());
                stopGame();
                return;
            }

            for (Player p : inGame.keySet()) {
                if (inGame.get(p) > GunListeners.getGuns().size()) {
                    startEndGame();
                }
            }
        }
    }

    //startGame stuff
    @Override
    public void startStartGame() {
        if (phase == Phase.STARTING) {
            return;
        }
        phase = Phase.STARTING;
        startingSeconds = 60;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (inGame.size() < minPlayers) {
                    sendMessage(Lang.ARENANOPLAYERS.toString());
                    cancel();
                    return;
                }
                if (startingSeconds > 5) {
                    for (Player p : inGame.keySet()) {
                        p.getInventory().setItem(0, ItemStackUtil.createItemStack(Material.RED_WOOL, ChatColor.RED + "Join red"));
                        p.getInventory().setItem(1, ItemStackUtil.createItemStack( Material.BLUE_WOOL, ChatColor.BLUE + "Join blue"));
                    }
                }
                switch (startingSeconds) {
                    case 0:
                        phase = Phase.RUNNING;
                        for (Player p : inGame.keySet()) {
                            p.getInventory().setItem(0, null);
                            p.getInventory().setItem(1, null);
                            spawnPlayerInArena(p, false);
                        }
                        Objective obj = scoreboard.getObjective("lead");
                        if (obj == null) {
                            scoreboard.registerNewObjective("lead", "dummy", "§aLeaderboard:");
                            scoreboard.getObjective("lead").setDisplaySlot(DisplaySlot.SIDEBAR);
                        }
                        cancel();
                        break;
                    case 5:
                        for (Player p : inGame.keySet()) {
                            p.getInventory().setItem(0, null);
                            p.getInventory().setItem(1, null);
                        }
                        sendMessage(Lang.ARENACOUNTDOWN.toString().replace("{time}", String.valueOf(startingSeconds)));
                        break;
                    case 3:
                    case 10:
                    case 20:
                    case 30:
                    case 60:
                        sendMessage(Lang.ARENACOUNTDOWN.toString().replace("{time}", String.valueOf(startingSeconds)));
                        break;
                }
                startingSeconds--;
            }
        }.runTaskTimer(WitheredGunGame.getPlugin(), 0, 20);
    }

    @Override
    public void checkStartGame() {
        if (phase == Phase.LOBBY && inGame.size() >= minPlayers) {
            startStartGame();
        }
    }

    @Override
    public void stopGame() {
        while (inGame.keySet().iterator().hasNext()) {
            Player p = inGame.keySet().iterator().next();
            removePlayer(p, true);
        }
        phase = Phase.LOBBY;
    }

    @Override
    public void spawnPlayerInArena(Player p, boolean silent) {
        if (getTeam(p) == null) {
            if (teamBlue.getEntries().size() <= teamRed.getEntries().size()) {
                setBlueTeam(p, true);
            } else {
                setRedTeam(p, true);
            }
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }

        p.setHealth(20);
        p.setFoodLevel(20);

        updateGun(p);
        if (teamRed.hasEntry(p.getName())) {
            setHelmet(p, Color.RED, ChatColor.RED + "Red");
            p.teleport(playerSpawns.get(0));
        } else {
            setHelmet(p, Color.BLUE, ChatColor.BLUE + "Blue");
            p.teleport(playerSpawns.get(1));
        }

        if (!silent) {
            p.sendMessage(ChatColor.GREEN + "Respawned!");
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }
    }

    @Override
    public void addToKills(Player p) {
        PlayerData data = PlayerData.getPlayerData(p);
        data.addToKills();
        if (data.getKillStreak() % 5 == 0) {
            sendMessage(Lang.KILLSTREAK.toString().replace("{player}", p.getName()).replace("{kills}", ""+data.getKillStreak()));
        }
        inGame.put(p, inGame.get(p) + 1);
        if (inGame.get(p)-2 == GunListeners.getGuns().size()) {
            sendMessage(Lang.ARENAPLAYERFINALKILL.toString().replace("{player}", p.getName()), Sound.ENTITY_ARROW_HIT_PLAYER);
        }
        Score s = scoreboard.getObjective("lead").getScore(p);
        s.setScore(s.getScore() + 1);
        updateGun(p);
        checkEndGame();
    }

    //other
    public void setBlueTeam(Player p, boolean force) {
        if (!teamBlue.getEntries().contains(p.getName())) {
            if (force || Math.ceil(inGame.size()/2.0) > teamBlue.getEntries().size()) {
                teamBlue.addEntry(p.getName());
                teamRed.removeEntry(p.getName());
                p.sendMessage(ChatColor.BLUE + "You have joined blue team!");
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                setHelmet(p, Color.BLUE, ChatColor.BLUE + "Blue");
            } else {
                p.sendMessage(ChatColor.RED + "Team is full!");
            }
        }
    }

    public void setRedTeam(Player p, boolean force) {
        if (!teamRed.getEntries().contains(p.getName())) {
            if (force || Math.ceil(inGame.size()/2.0) > teamRed.getEntries().size()) {
                teamRed.addEntry(p.getName());
                teamBlue.removeEntry(p.getName());
                p.sendMessage(ChatColor.RED + "You have joined red team!");
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                setHelmet(p, Color.RED, ChatColor.RED + "Red");
            } else {
                p.sendMessage(ChatColor.RED + "Team is full!");
            }
        }
    }

    public ArenaTEAM(Map<String, Object> map) {
        Arena.getArenas().add(this);
        teamBlue = createTeam("Team blue", ChatColor.BLUE, false);
        teamRed = createTeam("Team red", ChatColor.RED, false);
        try {
            lobbyLoc = (Location) map.get("lobbyLoc");
            name = (String) map.get("name");
            type = ArenaType.valueOf((String) map.get("type"));
            playerSpawns = (List<Location>) map.get("playerSpawns");
            maxPlayers = (Integer) map.get("maxPlayers");
            minPlayers = (Integer) map.get("minPlayers");
            WitheredUtil.log(Level.INFO, ChatColor.GREEN + "loaded arena " + name);
        } catch (Exception e) {
            WitheredUtil.log(Level.INFO, ChatColor.RED + "Failed to load Arena");
            e.printStackTrace();
        }
    }

    public static ArenaTEAM deserialize(Map<String, Object> map) {
        return new ArenaTEAM(map);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("lobbyLoc", lobbyLoc);
        map.put("name", name);
        map.put("type", type.toString());
        map.put("playerSpawns", playerSpawns);
        map.put("maxPlayers", maxPlayers);
        map.put("minPlayers", minPlayers);
        return map;
    }

}
