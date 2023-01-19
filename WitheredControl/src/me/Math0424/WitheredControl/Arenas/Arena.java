package me.Math0424.WitheredControl.Arenas;

import me.Math0424.CoreWeapons.DamageHandler.WitheredDamage;
import me.Math0424.WitheredControl.Data.PlayerData;
import me.Math0424.WitheredControl.Kits.Kit;
import me.Math0424.WitheredControl.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Arena implements ConfigurationSerializable {

    private static ArrayList<Arena> arenas = new ArrayList<Arena>();

    private Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

    private HashMap<Player, Integer> playersInGame = new HashMap<Player, Integer>();
    private HashMap<Player, Location> playerEnterLocations = new HashMap<Player, Location>();

    private boolean gameRunning = false;
    private boolean gameStarting = false;
    private boolean gameEnding = false;

    private int timeRemaining = 100;

    private Team redTeam;
    private Team blueTeam;

    private int bluePoints;
    private int redPoints;

    //serializable
    private ArenaType type;
    private String name;

    private Location lobbySpawn;
    private Location limbo;
    private int winningPoints;

    private Location flag1;
    private Location flag2;
    private Location flag3;
    private Location flag4;
    private Location flag5;

    private Integer maxPlayers;
    private HashMap<Integer, Location> spawnLocations = new HashMap<Integer, Location>();

    public Arena(String name, ArenaType type, int maxPlayers) {
        this.type = type;
        this.name = name;
        this.maxPlayers = maxPlayers;
    }

    public Team createTeam(String s, ChatColor c, boolean friendlyFire) {
        scoreboard.registerNewTeam(s);
        Team t = scoreboard.getTeam(s);
        t.setCanSeeFriendlyInvisibles(false);
        t.setAllowFriendlyFire(friendlyFire);
        t.setColor(c);
        t.setPrefix(c.toString());
        return t;
    }

    public Team getTeam(Entity p) {
        for (Team t : scoreboard.getTeams()) {
            if (t.getEntries().contains(p.getName())) {
                return t;
            }
        }
        return null;
    }

    public void updateKit(Player p, Kit k) {



    }

    public void playerDeath(Player p) {
        if (getPlayersInGame().keySet().contains(p)) {
            PlayerData.getData(p.getName()).addToDeaths();
            WitheredDamage damage = WitheredDamage.getLastDamage(p);
            for (PotionEffect effect : p.getActivePotionEffects()){
                p.removePotionEffect(effect.getType());
            }

            //fall damage detection
            EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();
            if (cause == EntityDamageEvent.DamageCause.FALL) {
                System.out.println(damage.getDamager());
                if (damage != null && damage.getDamager() instanceof Player) {
                    addToKills((Player) damage.getDamager());
                    sendMessage(Lang.DEATHGENERICFALLESCAPE.toString().replace("{killer}", getTeam(damage.getDamager()).getColor()+damage.getDamager().getName()).replace("{killed}", getTeam(damage.getDamaged()).getColor()+damage.getDamaged().getName()));
                } else {
                    sendMessage(Lang.DEATHGENERICFALL.toString().replace("{killed}", getTeam(p).getColor() + p.getName()));
                }
                spawnPlayerInArena(p, false);
                return;
            }

            if (damage != null && damage.getDamager() instanceof Player) {
                switch (damage.getCause()) {
                    case FIRE:
                        addToKills((Player) damage.getDamager());
                        sendMessage(Lang.DEATHFIRE.toString().replace("{killer}", getTeam(damage.getDamager()).getColor()+damage.getDamager().getName()).replace("{killed}", getTeam(damage.getDamaged()).getColor()+damage.getDamaged().getName()).replace("{gun}", damage.getGun().getName()));
                        return;
                    case LASER:
                        addToKills((Player) damage.getDamager());
                        sendMessage(Lang.DEATHLASER.toString().replace("{killer}", getTeam(damage.getDamager()).getColor()+damage.getDamager().getName()).replace("{killed}", getTeam(damage.getDamaged()).getColor()+damage.getDamaged().getName()).replace("{gun}", damage.getGun().getName()));
                        return;
                    case BULLET:
                        addToKills((Player) damage.getDamager());
                        sendMessage(Lang.DEATHBULLET.toString().replace("{killer}", getTeam(damage.getDamager()).getColor()+damage.getDamager().getName()).replace("{killed}", getTeam(damage.getDamaged()).getColor()+damage.getDamaged().getName()).replace("{gun}", damage.getGun().getName()));
                        return;
                    case HEADSHOT:
                        addToKills((Player) damage.getDamager());
                        sendMessage(Lang.DEATHBULLETHEADSHOT.toString().replace("{killer}", getTeam(damage.getDamager()).getColor()+damage.getDamager().getName()).replace("{killed}", getTeam(damage.getDamaged()).getColor()+damage.getDamaged().getName()).replace("{gun}", damage.getGun().getName()));
                        return;
                    case ACID:
                        addToKills((Player) damage.getDamager());
                        sendMessage(Lang.DEATHACID.toString().replace("{killer}", getTeam(damage.getDamager()).getColor()+damage.getDamager().getName()).replace("{killed}", getTeam(damage.getDamaged()).getColor()+damage.getDamaged().getName()).replace("{gun}", damage.getGun().getName()));
                        return;
                }
            }
            sendMessage(Lang.DEATHGENERIC.toString().replace("{killed}", getTeam(p).getColor()+p.getName()));
            spawnPlayerInArena(p, false);
        }
    }

    public void sendMessage(String m, Sound s) {
        for (Player p : playersInGame.keySet()) {
            p.sendMessage(m);
            p.playSound(p.getLocation(), s, 1, 1);
        }
    }

    public void sendMessage(String m) {
        for (Player p : playersInGame.keySet()) {
            p.sendMessage(m);
        }
    }


    //Overrides
    public abstract void addPlayer(Player p, boolean silent);

    public abstract void removePlayer(Player p, boolean silent);

    public abstract void checkEndGame();
    public abstract void startEndGame();
    public abstract void endGame();

    public abstract void checkStartGame();
    public abstract void startStartGame();
    public abstract void startGame();

    public abstract void spawnPlayerInArena(Player p, boolean silent);

    public abstract void updateHelmet(Player p);

    public abstract void addToKills(Player p);



    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public boolean isGameStarting() {
        return gameStarting;
    }

    public void setGameStarting(boolean gameStarting) {
        this.gameStarting = gameStarting;
    }

    public boolean isGameEnding() {
        return gameEnding;
    }

    public void setGameEnding(boolean gameEnding) {
        this.gameEnding = gameEnding;
    }

    public HashMap<Player, Location> getPlayerEnterLocations() {
        return playerEnterLocations;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public String getName() {
        return name;
    }

    public void setType(ArenaType type) {
        this.type = type;
    }

    public ArenaType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setSpawnLocations(HashMap<Integer, Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public HashMap<Integer, Location> getSpawnLocations() {
        return spawnLocations;
    }

    public HashMap<Player, Integer> getPlayersInGame() {
        return playersInGame;
    }


    public static ArrayList<Arena> getArenas() {
        return arenas;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public static Arena getArena(Player p) {
        for (Arena a : arenas) {
            if (a.playersInGame.keySet().contains(p)) {
                return a;
            }
        }
        return null;
    }

    public static Arena getArenaByName(String s) {
        for (Arena a : arenas) {
            if (a.name.equals(s)) {
                return a;
            }
        }
        return null;
    }

}
