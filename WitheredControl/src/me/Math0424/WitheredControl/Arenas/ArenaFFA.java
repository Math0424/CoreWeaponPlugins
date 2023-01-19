package me.Math0424.WitheredControl.Arenas;

import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.WitheredControl.Data.PlayerData;
import me.Math0424.WitheredControl.Data.SaveFiles;
import me.Math0424.WitheredControl.Lang;
import me.Math0424.WitheredControl.Util.WitheredUtil;
import me.Math0424.WitheredControl.WitheredControl;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class ArenaFFA extends Arena {

    private Team main;

    public ArenaFFA(String name, ArenaType type, int maxPlayers) {
        super(name, type, maxPlayers);
        Arena.getArenas().add(this);
        main = createTeam("FFA", ChatColor.GRAY, true);
    }

    @Override
    public void addPlayer(Player p, boolean silent) {
        if(getPlayersInGame().size() > getMaxPlayers()) {
            p.sendMessage(ChatColor.RED + "Game is full!");
        } else if(isGameEnding()) {
            p.sendMessage(ChatColor.RED + "Game is ending!");
        }
        getPlayersInGame().put(p, 1);
        getPlayerEnterLocations().put(p, p.getLocation());
        p.setScoreboard(getScoreboard());
        p.setGameMode(GameMode.SURVIVAL);
        main.addEntry(p.getName());
        updateHelmet(p);
        if (isGameRunning()) {
            sendMessage(ChatColor.GREEN + p.getName() + " has joined the game!", Sound.ENTITY_ARROW_HIT_PLAYER);
            spawnPlayerInArena(p, false);
        } else {
            p.teleport(getLobbySpawn());
            sendMessage(ChatColor.GREEN + p.getName() + " has joined the lobby!", Sound.ENTITY_ARROW_HIT_PLAYER);
            checkStartGame();
        }
    }

    @Override
    public void removePlayer(Player p, boolean silent) {
        p.setGameMode(GameMode.SURVIVAL);
        getScoreboard().resetScores(p.getName());
        main.removeEntry(p.getName());
        p.setHealth(20);
        p.setFoodLevel(20);
        p.teleport(getPlayerEnterLocations().get(p));
        p.getInventory().clear();
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        getPlayersInGame().remove(p);
        getPlayerEnterLocations().remove(p);
        if (!silent) {
            sendMessage(ChatColor.GREEN + p.getName() + " has left the game", Sound.ENTITY_ARROW_HIT_PLAYER);
        }
        checkEndGame();
    }

    //endGame stuff
    @Override
    public void checkEndGame() {
        if (!isGameEnding()) {
            if(getPlayersInGame().size() < 2) {
                sendMessage(ChatColor.RED + "Not enough players to continue!");
                endGame();
            }
            for (Player p : getPlayersInGame().keySet()) {
                if (getPlayersInGame().get(p) == Gun.getGuns().size() + 1) {
                    PlayerData.getData(p.getName()).addToWins();
                    sendMessage(ChatColor.DARK_PURPLE + p.getName() + " has won the game", Sound.ENTITY_ENDER_DRAGON_GROWL);
                    sendMessage(ChatColor.LIGHT_PURPLE + "Game ending in 10 seconds!");
                    startEndGame();
                }
            }
        }
    }

    @Override
    public void startEndGame() {
        if (!isGameEnding()) {
            setGameEnding(true);
            setGameRunning(false);
            setGameStarting(false);
            for (Player p : getPlayersInGame().keySet()) {
                p.getInventory().setItem(0, null);
                p.setHealth(20);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    endGame();
                }
            }.runTaskLater(WitheredControl.getPlugin(), 20 * 10);
        }
    }

    @Override
    public void endGame() {
        setGameEnding(false);
        setGameRunning(false);
        setGameStarting(false);
        getSpectatorsInGame().clear();
        getSpectatorTime().clear();
        for (Player p : new HashMap<>(getPlayersInGame()).keySet()) {
            removePlayer(p, true);
            p.getInventory().clear();
        }
        getPlayersInGame().clear();
    }

    //startGame stuff
    @Override
    public void checkStartGame() {
        if (!isGameStarting() && !isGameRunning() && !isGameEnding() && getPlayersInGame().size() >= 2) {
            startStartGame();
        }
    }

    @Override
    public void startStartGame() {
        if (!isGameStarting()) {
            setGameEnding(false);
            setGameRunning(false);
            setGameStarting(true);
            new BukkitRunnable() {
                int tick = 0;
                @Override
                public void run() {
                    if (getPlayersInGame().size() < 2 || !isGameStarting()) {
                        if (!isGameRunning()) {
                            sendMessage(ChatColor.RED + "Game canceled.");
                        }
                        setGameStarting(false);
                        cancel();
                        return;
                    }
                    if (tick == 0) {
                        sendMessage(ChatColor.GREEN + "Game starting in 60 seconds!");
                    } else if (tick == 30*20) {
                        sendMessage(ChatColor.GREEN + "Game starting in 30 seconds!");
                    } else if (tick == 50*20) {
                        sendMessage(ChatColor.GREEN + "Game starting in 10 seconds!");
                    } else if(tick == 60*20) {
                        startGame();
                        cancel();
                        return;
                    }
                    tick++;
                }
            }.runTaskTimer(WitheredControl.getPlugin(), 0, 1);
        }
    }

    @Override
    public void startGame() {
        if (!isGameRunning()) {
            setGameEnding(false);
            setGameRunning(true);
            setGameStarting(false);
            if (getScoreboard().getObjective("lead") == null) {
                getScoreboard().registerNewObjective("lead", "dummy", "§aLeaderboard:");
                getScoreboard().getObjective("lead").setDisplaySlot(DisplaySlot.SIDEBAR);
            }
            for (Player p : getPlayersInGame().keySet()) {
                spawnPlayerInArena(p, true);
            }
            sendMessage(ChatColor.GREEN + "The game has started!", Sound.ENTITY_ENDER_DRAGON_GROWL);
        }
    }

    @Override
    public void spawnPlayerInArena(Player p, boolean silent) {

        p.setHealth(20);
        p.setFoodLevel(20);
        updateGun(p);
        updateHelmet(p);
        if (!silent) {
            p.sendMessage(ChatColor.AQUA + "You have spawned!");
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }

        if (isGameRunning()) {
            for (int i = 0; i <= 10; i++) {
                Location loc = getSpawnLocations().get(WitheredUtil.random(getSpawnLocations().size()));
                if (i == 10) {
                    p.teleport(loc);
                    return;
                } else {
                    boolean spawn = true;
                    for (Player p1 : getPlayersInGame().keySet()) {
                        if (p1.getLocation().distance(loc) <= 10) {
                            spawn = false;
                            break;
                        }
                    }
                    if (spawn) {
                        p.teleport(loc);
                        return;
                    }
                }
            }
        } else {
            p.teleport(getLobbySpawn());
        }
    }

    @Override
    public void updateHelmet(Player p) {
        if (getPlayersInGame().get(p) == null || isGameEnding()) {
            p.getInventory().setHelmet(null);
            return;
        }
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta meta = item.getItemMeta();
        LeatherArmorMeta meta2 = (LeatherArmorMeta) meta;
        meta.setDisplayName(ChatColor.GRAY + "Independent");
        meta2.setColor(Color.fromRGB(140, 70, 0));
        meta2.setUnbreakable(true);
        meta2.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta2);
        item.setItemMeta(meta2);
        p.getInventory().setHelmet(item);
    }

    @Override
    public void addToKills(Player p) {
        PlayerData data = PlayerData.getData(p.getName());
        data.addToKills();
        if (data.getKillStreak() % 5 == 0) {
            sendMessage(Lang.KILLSTREAK.toString().replace("{player}", p.getName()).replace("{kills}", ""+data.getKillStreak()));
        }
        getPlayersInGame().put(p, getPlayersInGame().get(p)+1);
        Score s = getScoreboard().getObjective("lead").getScore(p);
        s.setScore(s.getScore() + 1);
        p.setHealth(20);
        updateGun(p);
        SaveFiles.savePlayerData();
        checkEndGame();
    }

    public static ArenaFFA deserialize(Map<String, Object> map) {
        try {
            ArenaFFA arena = new ArenaFFA((String) map.get("name"), ArenaType.valueOf((String) map.get("type")), (Integer) map.get("maxPlayers"));
            arena.setLobbySpawn((Location) map.get("lobby"));
            arena.setSpawnLocations((HashMap<Integer, Location>) map.get("spawnLocations"));
            WitheredUtil.info(ChatColor.GREEN + "loaded arena " + arena.getName());
            return arena;
        } catch (Exception e) {
            WitheredUtil.info(ChatColor.RED + "Failed to load Arena");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("lobby", getLobbySpawn());
        map.put("name", getName());
        map.put("type", getType().toString());
        map.put("spawnLocations", getSpawnLocations());
        map.put("maxPlayers", getMaxPlayers());
        return map;
    }
}
